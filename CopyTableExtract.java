public class ContactDeletionBatch implements Database.Batchable<sObject> {
    private List<Contact> contactsToProcess;

    // Constructor to pass deleted contacts
    public ContactDeletionBatch(List<Contact> contacts) {
        this.contactsToProcess = contacts;
    }

    public Database.QueryLocator start(Database.BatchableContext context) {
        // Not querying, using the passed list of Contacts
        return Database.getQueryLocator([SELECT Id FROM Contact WHERE Id = null]); // Dummy query
    }

    public void execute(Database.BatchableContext context, List<sObject> scope) {
        List<Log__c> logs = new List<Log__c>();
        List<Messaging.SingleEmailMessage> emails = new List<Messaging.SingleEmailMessage>();

        // Map OwnerId to User Email for bulk processing
        Set<Id> ownerIds = new Set<Id>();
        for (Contact c : contactsToProcess) {
            ownerIds.add(c.OwnerId);
        }
        Map<Id, User> ownerEmails = new Map<Id, User>(
            [SELECT Id, Email FROM User WHERE Id IN :ownerIds]
        );

        for (Contact c : contactsToProcess) {
            // Create a log entry
            logs.add(new Log__c(
                Object_Name__c = 'Contact',
                Action__c = 'Delete',
                Details__c = 'Deleted Contact Name: ' + c.Name,
                Record_Id__c = c.Id
            ));

            // Send email notification if owner email exists
            if (ownerEmails.containsKey(c.OwnerId)) {
                User owner = ownerEmails.get(c.OwnerId);
                if (owner.Email != null) {
                    Messaging.SingleEmailMessage mail = new Messaging.SingleEmailMessage();
                    mail.setToAddresses(new String[] { owner.Email });
                    mail.setSubject('Contact Deletion Notification');
                    mail.setPlainTextBody('The contact "' + c.Name + '" has been deleted.');
                    emails.add(mail);
                }
            }
        }

        // Insert log records
        if (!logs.isEmpty()) {
            try {
                insert logs;
            } catch (Exception ex) {
                System.debug('Error inserting logs: ' + ex.getMessage());
            }
        }

        // Send emails
        if (!emails.isEmpty()) {
            try {
                Messaging.sendEmail(emails);
            } catch (Exception ex) {
                System.debug('Error sending emails: ' + ex.getMessage());
            }
        }
    }

    public void finish(Database.BatchableContext context) {
        System.debug('Batch processing for deleted contacts completed.');
    }
}






Line: 7, Column: 1
System.DmlException: Insert failed. First exception on row 0; first error: INVALID_OR_NULL_FOR_RESTRICTED_PICKLIST, Action: bad value for restricted picklist field: Delete: [Action__c]




public class ContactDeleteBatch implements Database.Batchable<sObject>, Database.Stateful {
    private List<Contact> deletedContacts;

    // Constructor to accept deleted Contacts
    public ContactDeleteBatch(List<Contact> contacts) {
        this.deletedContacts = contacts;
    }

    // Start method
    public Database.QueryLocator start(Database.BatchableContext BC) {
        // No query needed as the records are passed directly
        return Database.getQueryLocator('SELECT Id FROM Contact LIMIT 1');
    }

    // Execute method
    public void execute(Database.BatchableContext BC, List<sObject> scope) {
        try {
            Map<Id, String> ownerEmails = new Map<Id, String>();

            // Get the owner emails for the deleted contacts
            Set<Id> ownerIds = new Set<Id>();
            for (Contact c : deletedContacts) {
                if (c.OwnerId != null) {
                    ownerIds.add(c.OwnerId);
                }
            }

            // Query User records to retrieve owner emails
            for (User u : [SELECT Id, Email FROM User WHERE Id IN :ownerIds]) {
                ownerEmails.put(u.Id, u.Email);
            }

            // Log details and send notifications to owners
            for (Contact c : deletedContacts) {
                System.debug('Deleted Contact - Id: ' + c.Id + ', Name: ' + c.Name);

                // Log record creation
                Log__c log = new Log__c(
                    Object_Name__c = 'Contact',
                    Action__c = 'Delete',
                    Details__c = 'Deleted Contact Name: ' + c.Name + ', Owner Email: ' + ownerEmails.get(c.OwnerId)
                );
                insert log;

                // Send email notification to the Contact Owner
                if (ownerEmails.containsKey(c.OwnerId)) {
                    Messaging.SingleEmailMessage email = new Messaging.SingleEmailMessage();
                    email.setToAddresses(new String[] {ownerEmails.get(c.OwnerId)});
                    email.setSubject('Contact Deletion Notification');
                    email.setPlainTextBody('The contact "' + c.Name + '" has been deleted. Please review.');
                    Messaging.sendEmail(new Messaging.SingleEmailMessage[] {email});
                }
            }

        } catch (Exception e) {
            // Handle exceptions and log errors
            System.debug('Error occurred: ' + e.getMessage());
            Log__c errorLog = new Log__c(
                Object_Name__c = 'Contact',
                Action__c = 'Delete',
                Details__c = 'Error: ' + e.getMessage()
            );
            insert errorLog;
        }
    }

    // Finish method
    public void finish(Database.BatchableContext BC) {
        System.debug('Batch Job Finished');
    }
}







execute the batch when contact record(s) get deleted and capture below items:

1.	Log details
2.	Exception Handling
3.	Send notifications





https://developer.salesforce.com/blogs/developer
relations/2017/01/lightning-visualforce-communication.html.




trigger ContactAddressSync on Contact (before update) {
    // Prevent recursive execution
    if (!ContactAddressSyncHelper.isTriggerActive) {
        return;
    }
    
    try {
        // Set trigger as inactive to prevent recursion
        ContactAddressSyncHelper.isTriggerActive = false;

        System.debug('ContactAddressSync trigger fired');

        // Maps to track contacts to update by email
        Map<String, List<Contact>> contactsByEmail = new Map<String, List<Contact>>();
        List<Contact> contactsToUpdate = new List<Contact>();

        // Step 1: Collect emails of contacts where address fields have changed
        Set<String> changedEmails = new Set<String>();

        for (Contact contact : Trigger.new) {
            Contact oldContact = Trigger.oldMap.get(contact.Id);
            // Check if any of the address fields have changed
            if (contact.Email != null && (contact.MailingStreet != oldContact.MailingStreet || 
                                          contact.MailingCity != oldContact.MailingCity || 
                                          contact.MailingPostalCode != oldContact.MailingPostalCode ||
                                          contact.MailingState != oldContact.MailingState ||
                                          contact.MailingCountry != oldContact.MailingCountry)) {
                changedEmails.add(contact.Email);
            }
        }

        // Step 2: Query all contacts with the same email as changed contacts
        if (!changedEmails.isEmpty()) {
            for (Contact relatedContact : [SELECT Id, Email, MailingStreet, MailingCity, MailingPostalCode, MailingState, MailingCountry 
                                           FROM Contact WHERE Email IN :changedEmails]) {
                if (!contactsByEmail.containsKey(relatedContact.Email)) {
                    contactsByEmail.put(relatedContact.Email, new List<Contact>());
                }
                contactsByEmail.get(relatedContact.Email).add(relatedContact);
            }
        }

        // Step 3: Sync address fields for contacts with the same email
        for (Contact contact : Trigger.new) {
            if (contact.Email != null && contactsByEmail.containsKey(contact.Email)) {
                List<Contact> relatedContacts = contactsByEmail.get(contact.Email);

                // Get the updated address fields
                String updatedStreet = contact.MailingStreet;
                String updatedCity = contact.MailingCity;
                String updatedPostalCode = contact.MailingPostalCode;
                String updatedState = contact.MailingState;
                String updatedCountry = contact.MailingCountry;

                for (Contact relatedContact : relatedContacts) {
                    // Skip if this is the contact being updated
                    if (relatedContact.Id == contact.Id) continue;

                    // Update address fields if they differ from the updated contact
                    if (relatedContact.MailingStreet != updatedStreet || 
                        relatedContact.MailingCity != updatedCity || 
                        relatedContact.MailingPostalCode != updatedPostalCode ||
                        relatedContact.MailingState != updatedState ||
                        relatedContact.MailingCountry != updatedCountry) {

                        relatedContact.MailingStreet = updatedStreet;
                        relatedContact.MailingCity = updatedCity;
                        relatedContact.MailingPostalCode = updatedPostalCode;
                        relatedContact.MailingState = updatedState;
                        relatedContact.MailingCountry = updatedCountry;
                        contactsToUpdate.add(relatedContact); // Add to list for bulk update
                        
                        System.debug('Updating Contact address for Email ' + relatedContact.Email + ': ' + relatedContact);
                    }
                }
            }
        }

        // Step 4: Perform bulk update of contacts with changed addresses
        if (!contactsToUpdate.isEmpty()) {
            System.debug('Updating contacts with synchronized addresses: ' + contactsToUpdate);
            update contactsToUpdate;
        }
    } finally {
        // Reset the trigger control flag
        ContactAddressSyncHelper.isTriggerActive = true;
    }
}












trigger UserAddressTrigger on User (before insert, before update) {
    System.debug('UserContactSyncTrigger fired');
    // Maps to hold user and contact data
    Map<String, Contact> contactsByEmail = new Map<String, Contact>();
    Map<String, User> usersByEmail = new Map<String, User>();

    // List to hold new contacts to be inserted after the loop
    List<Contact> newContacts = new List<Contact>();

    // Step 1: Populate the maps with existing Contacts and Users by email
    if (Trigger.isInsert || Trigger.isUpdate) {
        Set<String> userEmails = new Set<String>();
        
        for (User user : Trigger.new) {
            if (user.Email != null) {
                userEmails.add(user.Email);
            }
        }

        // Query existing contacts with the same emails
        for (Contact contact : [SELECT Id, Email, MailingStreet, MailingCity, MailingPostalCode, MailingState, MailingCountry FROM Contact WHERE Email IN :userEmails]) {
            contactsByEmail.put(contact.Email, contact);
        }
        
        // Query existing users with the same emails
        for (User existingUser : [SELECT Id, Email, Street, City, PostalCode, State, Country FROM User WHERE Email IN :userEmails]) {
            usersByEmail.put(existingUser.Email, existingUser);
        }
    }

    // Step 2: Create or Update User, and synchronize with Contact
    for (User user : Trigger.new) {
        // Skip processing if email is null
        if (user.Email == null) {
            continue;
        }

        if (Trigger.isInsert || (Trigger.isUpdate && user.Email != null)) {
            if (usersByEmail.containsKey(user.Email)) {
                // User exists, update the address fields
                User existingUser = usersByEmail.get(user.Email);
                existingUser.Street = user.Street;
                existingUser.City = user.City;
                existingUser.PostalCode = user.PostalCode;
                existingUser.State = user.State;
                existingUser.Country = user.Country;
            }

            // Step 3: Check for Contact by Email and sync address if needed
            if (contactsByEmail.containsKey(user.Email)) {
                // Contact exists, update the address fields if they have changed
                Contact existingContact = contactsByEmail.get(user.Email);
                
                existingContact.Contact_Address_Updated__c = false;
                if (existingContact.MailingStreet != user.Street || existingContact.MailingCity != user.City || 
                    existingContact.MailingPostalCode != user.PostalCode || existingContact.MailingState != user.State || 
                    existingContact.MailingCountry != user.Country) {
                    
                    existingContact.MailingStreet = user.Street;
                    existingContact.MailingCity = user.City;
                    existingContact.MailingPostalCode = user.PostalCode;
                    existingContact.MailingState = user.State;
                    existingContact.MailingCountry = user.Country;
                    existingContact.Contact_Address_Updated__c = true;
                }

                if (existingContact.Contact_Address_Updated__c) {
                    // Update existing contact in the map
                    contactsByEmail.put(existingContact.Email, existingContact);
                }

                // Log the existing contact
                System.debug('Updating existing Contact: ' + existingContact);
            } else {
                // No Contact exists, create a new Contact with User details
                Contact newContact = new Contact(
                    FirstName = user.FirstName,
                    LastName = user.LastName,
                    Email = user.Email,
                    MailingStreet = user.Street,
                    MailingCity = user.City,
                    MailingPostalCode = user.PostalCode,
                    MailingState = user.State,
                    MailingCountry = user.Country
                );

                // Check if Email is filled out before adding to newContacts list
                if (String.isBlank(newContact.Email)) {
                    user.addError('The Email field is required for creating a new Contact.');
                } else {
                    contactsByEmail.put(user.Email, newContact); // Track created contact
                    newContacts.add(newContact); // Add to list for insertion
                    
                    // Log the new contact
                    System.debug('Creating new Contact: ' + newContact);
                }
            }
        }
    }

    // Insert all new contacts after the loop
    if (!newContacts.isEmpty()) {
        System.debug('Inserting new Contacts: ' + newContacts);
        insert newContacts;
    }

    // Update contacts with changes in address fields
    if (!contactsByEmail.isEmpty()) {
        System.debug('Updating Contacts with changed addresses: ' + contactsByEmail.values());
        update contactsByEmail.values();
    }
}







trigger UserAddressTrigger on User (before  insert, before  update) {
     System.debug('UserContactSyncTrigger fired');
    // Maps to hold user and contact data
    Map<String, Contact> contactsByEmail = new Map<String, Contact>();
    Map<String, User> usersByEmail = new Map<String, User>();

    // List to hold new contacts to be inserted after the loop
    List<Contact> newContacts = new List<Contact>();

    // Step 1: Populate the maps with existing Contacts and Users by email
    if (Trigger.isInsert || Trigger.isUpdate) {
        Set<String> userEmails = new Set<String>();
        
        for (User user : Trigger.new) {
            if (user.Email != null) {
                userEmails.add(user.Email);
            }
        }

        // Query existing contacts with the same emails
        for (Contact contact : [SELECT Id, Email, MailingStreet, MailingCity, MailingPostalCode FROM Contact WHERE Email IN :userEmails]) {
            contactsByEmail.put(contact.Email, contact);
        }
        
        // Query existing users with the same emails
        for (User existingUser : [SELECT Id, Email, Street, City, PostalCode FROM User WHERE Email IN :userEmails]) {
            usersByEmail.put(existingUser.Email, existingUser);
        }
    }

    // Step 2: Create or Update User, and synchronize with Contact
    for (User user : Trigger.new) {
        // Skip processing if email is null
        if (user.Email == null) {
            continue;
        }

        if (Trigger.isInsert || (Trigger.isUpdate && user.Email != null)) {
            if (usersByEmail.containsKey(user.Email)) {
                // User exists, update the address fields
                User existingUser = usersByEmail.get(user.Email);
                existingUser.Street = user.Street;
                existingUser.City = user.City;
                existingUser.PostalCode = user.PostalCode;
            }

            // Step 3: Check for Contact by Email and sync address if needed
            if (contactsByEmail.containsKey(user.Email)) {
                // Contact exists, update the address fields if they have changed
                Contact existingContact = contactsByEmail.get(user.Email);
                
                existingContact.Contact_Address_Updated__c = false;
                if (existingContact.MailingStreet != user.Street || existingContact.MailingCity != user.City || existingContact.MailingPostalCode != user.PostalCode) {
                    existingContact.MailingStreet = user.Street;
                    existingContact.MailingCity = user.City;
                    existingContact.MailingPostalCode = user.PostalCode;
                    existingContact.Contact_Address_Updated__c = true;
                }

                if (existingContact.Contact_Address_Updated__c) {
                    // Update existing contact in the map
                    contactsByEmail.put(existingContact.Email, existingContact);
                }

                // Log the existing contact
                System.debug('Updating existing Contact: ' + existingContact);
            } else {
                // No Contact exists, create a new Contact with User details
                Contact newContact = new Contact(
                    FirstName = user.FirstName,
                    LastName = user.LastName,
                    Email = user.Email,
                    MailingStreet = user.Street,
                    MailingCity = user.City,
                    MailingPostalCode = user.PostalCode
                );

                // Check if Email is filled out before adding to newContacts list
                if (String.isBlank(newContact.Email)) {
                    user.addError('The Email field is required for creating a new Contact.');
                } else {
                    contactsByEmail.put(user.Email, newContact); // Track created contact
                    newContacts.add(newContact); // Add to list for insertion
                    
                    // Log the new contact
                    System.debug('Creating new Contact: ' + newContact);
                }
            }
        }
    }

    // Insert all new contacts after the loop
    if (!newContacts.isEmpty()) {
        System.debug('Inserting new Contacts: ' + newContacts);
        insert newContacts;
    }

    // Update contacts with changes in address fields
    if (!contactsByEmail.isEmpty()) {
        System.debug('Updating Contacts with changed addresses: ' + contactsByEmail.values());
        update contactsByEmail.values();
    }
}




trigger ContactAddressSync on Contact (before update) {
    // Prevent recursive execution
    if (!ContactAddressSyncHelper.isTriggerActive) {
        return;
    }
    
    try {
        // Set trigger as inactive to prevent recursion
        ContactAddressSyncHelper.isTriggerActive = false;

        System.debug('ContactAddressSync trigger fired');

        // Maps to track contacts to update by email
        Map<String, List<Contact>> contactsByEmail = new Map<String, List<Contact>>();
        List<Contact> contactsToUpdate = new List<Contact>();

        // Step 1: Collect emails of contacts where address fields have changed
        Set<String> changedEmails = new Set<String>();

        for (Contact contact : Trigger.new) {
            Contact oldContact = Trigger.oldMap.get(contact.Id);
            // Check if any of the address fields have changed
            if (contact.Email != null && (contact.MailingStreet != oldContact.MailingStreet || 
                                          contact.MailingCity != oldContact.MailingCity || 
                                          contact.MailingPostalCode != oldContact.MailingPostalCode)) {
                changedEmails.add(contact.Email);
            }
        }

        // Step 2: Query all contacts with the same email as changed contacts
        if (!changedEmails.isEmpty()) {
            for (Contact relatedContact : [SELECT Id, Email, MailingStreet, MailingCity, MailingPostalCode 
                                           FROM Contact WHERE Email IN :changedEmails]) {
                if (!contactsByEmail.containsKey(relatedContact.Email)) {
                    contactsByEmail.put(relatedContact.Email, new List<Contact>());
                }
                contactsByEmail.get(relatedContact.Email).add(relatedContact);
            }
        }

        // Step 3: Sync address fields for contacts with the same email
        for (Contact contact : Trigger.new) {
            if (contact.Email != null && contactsByEmail.containsKey(contact.Email)) {
                List<Contact> relatedContacts = contactsByEmail.get(contact.Email);

                // Get the updated address fields
                String updatedStreet = contact.MailingStreet;
                String updatedCity = contact.MailingCity;
                String updatedPostalCode = contact.MailingPostalCode;

                for (Contact relatedContact : relatedContacts) {
                    // Skip if this is the contact being updated
                    if (relatedContact.Id == contact.Id) continue;

                    // Update address fields if they differ from the updated contact
                    if (relatedContact.MailingStreet != updatedStreet || 
                        relatedContact.MailingCity != updatedCity || 
                        relatedContact.MailingPostalCode != updatedPostalCode) {

                        relatedContact.MailingStreet = updatedStreet;
                        relatedContact.MailingCity = updatedCity;
                        relatedContact.MailingPostalCode = updatedPostalCode;
                        contactsToUpdate.add(relatedContact); // Add to list for bulk update
                        
                        System.debug('Updating Contact address for Email ' + relatedContact.Email + ': ' + relatedContact);
                    }
                }
            }
        }

        // Step 4: Perform bulk update of contacts with changed addresses
        if (!contactsToUpdate.isEmpty()) {
            System.debug('Updating contacts with synchronized addresses: ' + contactsToUpdate);
            update contactsToUpdate;
        }
    } finally {
        // Reset the trigger control flag
        ContactAddressSyncHelper.isTriggerActive = true;
    }
}
