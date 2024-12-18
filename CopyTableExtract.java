public class RecordCounts {
    public Integer opportunitiesCount { get; set; }
    public Integer casesCount { get; set; }
}

public static RecordCounts getCounts() {
    RecordCounts counts = new RecordCounts();

    counts.opportunitiesCount = [SELECT COUNT() FROM Opportunity WHERE ContactId != null];
    counts.casesCount = [SELECT COUNT() FROM Case WHERE ContactId != null];

    return counts;
}

// Usage
RecordCounts counts = getCounts();
System.debug('Total Opportunities: ' + counts.opportunitiesCount);
System.debug('Total Cases: ' + counts.casesCount);





Database.QueryLocator oppQueryLocator = Database.getQueryLocator('SELECT Id FROM Opportunity WHERE ContactId != null');
Integer numOfOpportunities = oppQueryLocator.getQuerySize();
System.debug('Total Opportunities: ' + numOfOpportunities);



@isTest
public class ContactTriggerHandlerTest {
    
    @isTest
    static void testHandleTrigger() {
        // Setup test data
        List<Contact> contacts = new List<Contact>();
        for (Integer i = 0; i < 5; i++) {
            contacts.add(new Contact(FirstName = 'Test', LastName = 'User' + i, Email = 'test' + i + '@example.com'));
        }
        insert contacts;

        // Insert Opportunities related to the contacts
        List<Opportunity> opportunities = new List<Opportunity>();
        for (Contact con : contacts) {
            opportunities.add(new Opportunity(Name = 'Test Opportunity', StageName = 'Prospecting', CloseDate = Date.today(), Contact_Id__c = con.Id));
        }
        insert opportunities;

        // Insert Cases related to the contacts
        List<Case> cases = new List<Case>();
        for (Contact con : contacts) {
            cases.add(new Case(Subject = 'Test Case', Status = 'New', ContactId = con.Id));
        }
        insert cases;

        // Start the test
        Test.startTest();

        // Test Insert Trigger
        ContactTriggerHandler.handleTrigger(true, false, false, true, contacts, null);

        // Update Contacts
        for (Contact con : contacts) {
            con.FirstName = 'Updated Test';
        }
        update contacts;

        // Test Update Trigger
        ContactTriggerHandler.handleTrigger(false, true, false, true, contacts, null);

        // Delete Contacts
        delete contacts;

        // Test Delete Trigger
        ContactTriggerHandler.handleTrigger(false, false, true, true, null, contacts);

        // Undelete Contacts
        undelete contacts;

        // Test Undelete Trigger
        ContactTriggerHandler.handleTrigger(false, false, false, true, contacts, null);

        // End the test
        Test.stopTest();

        // Assert email limit usage for all operations
        System.assertEquals(20, Limits.getEmailInvocations(), 'Emails should be sent for all trigger operations');
    }
}






@isTest
public class ContactTriggerHandlerTest {
    
    @isTest
    static void testHandleTrigger() {
        // Setup test data
        List<Contact> contacts = new List<Contact>();
        for (Integer i = 0; i < 5; i++) {
            contacts.add(new Contact(FirstName = 'Test', LastName = 'User' + i, Email = 'test' + i + '@example.com'));
        }
        insert contacts;

        // Insert Opportunities related to the contacts
        List<Opportunity> opportunities = new List<Opportunity>();
        for (Contact con : contacts) {
            opportunities.add(new Opportunity(Name = 'Test Opportunity', StageName = 'Prospecting', CloseDate = Date.today(), Contact_Id__c = con.Id));
        }
        insert opportunities;

        // Insert Cases related to the contacts
        List<Case> cases = new List<Case>();
        for (Contact con : contacts) {
            cases.add(new Case(Subject = 'Test Case', Status = 'New', ContactId = con.Id));
        }
        insert cases;

        // Test Insert Trigger
        Test.startTest();
        ContactTriggerHandler.handleTrigger(true, false, false, true, contacts, null);
        Test.stopTest();

        // Assert email limit usage for Insert
        System.assertEquals(5, Limits.getEmailInvocations(), 'Emails should be sent for Contact insert');

        // Update Contacts
        for (Contact con : contacts) {
            con.FirstName = 'Updated Test';
        }
        update contacts;

        // Test Update Trigger
        Test.startTest();
        ContactTriggerHandler.handleTrigger(false, true, false, true, contacts, null);
        Test.stopTest();

        // Assert email limit usage for Update
        System.assertEquals(10, Limits.getEmailInvocations(), 'Emails should be sent for Contact update');

        // Delete Contacts
        delete contacts;

        // Test Delete Trigger
        Test.startTest();
        ContactTriggerHandler.handleTrigger(false, false, true, true, null, contacts);
        Test.stopTest();

        // Assert email limit usage for Delete
        System.assertEquals(15, Limits.getEmailInvocations(), 'Emails should be sent for Contact delete');

        // Undelete Contacts
        undelete contacts;

        // Test Undelete Trigger
        Test.startTest();
        ContactTriggerHandler.handleTrigger(false, false, false, true, contacts, null);
        Test.stopTest();

        // Assert email limit usage for Undelete
        System.assertEquals(20, Limits.getEmailInvocations(), 'Emails should be sent for Contact undelete');
    }
}








@isTest
public class ContactTriggerHandlerTest {
    
    @isTest
    static void testHandleTrigger() {
        // Setup test data
        List<Contact> contacts = new List<Contact>();
        for (Integer i = 0; i < 5; i++) {
            contacts.add(new Contact(FirstName = 'Test', LastName = 'User' + i, Email = 'test' + i + '@example.com'));
        }
        insert contacts;

        // Insert Opportunities related to the contacts
        List<Opportunity> opportunities = new List<Opportunity>();
        for (Contact con : contacts) {
            opportunities.add(new Opportunity(Name = 'Test Opportunity', StageName = 'Prospecting', CloseDate = Date.today(), Contact_Id__c = con.Id));
        }
        insert opportunities;

        // Insert Cases related to the contacts
        List<Case> cases = new List<Case>();
        for (Contact con : contacts) {
            cases.add(new Case(Subject = 'Test Case', Status = 'New', ContactId = con.Id));
        }
        insert cases;

        // Test Insert Trigger
        Test.startTest();
        ContactTriggerHandler.handleTrigger(true, false, false, true, contacts, null);
        Test.stopTest();

        // Assertions for Insert
        for (Contact con : contacts) {
            List<Messaging.SingleEmailMessage> sentEmails = [SELECT Id FROM Messaging.SingleEmailMessage WHERE ToAddress = :con.Email];
            System.assertEquals(1, sentEmails.size(), 'Email should be sent for Contact insert');
        }

        // Update Contacts
        for (Contact con : contacts) {
            con.FirstName = 'Updated Test';
        }
        update contacts;

        // Test Update Trigger
        Test.startTest();
        ContactTriggerHandler.handleTrigger(false, true, false, true, contacts, null);
        Test.stopTest();

        // Assertions for Update
        for (Contact con : contacts) {
            List<Messaging.SingleEmailMessage> sentEmails = [SELECT Id FROM Messaging.SingleEmailMessage WHERE ToAddress = :con.Email];
            System.assertEquals(1, sentEmails.size(), 'Email should be sent for Contact update');
        }

        // Delete Contacts
        delete contacts;

        // Test Delete Trigger
        Test.startTest();
        ContactTriggerHandler.handleTrigger(false, false, true, true, null, contacts);
        Test.stopTest();

        // Assertions for Delete
        for (Contact con : contacts) {
            List<Messaging.SingleEmailMessage> sentEmails = [SELECT Id FROM Messaging.SingleEmailMessage WHERE ToAddress = :con.Email];
            System.assertEquals(1, sentEmails.size(), 'Email should be sent for Contact delete');
        }

        // Undelete Contacts
        undelete contacts;

        // Test Undelete Trigger
        Test.startTest();
        ContactTriggerHandler.handleTrigger(false, false, false, true, contacts, null);
        Test.stopTest();

        // Assertions for Undelete
        for (Contact con : contacts) {
            List<Messaging.SingleEmailMessage> sentEmails = [SELECT Id FROM Messaging.SingleEmailMessage WHERE ToAddress = :con.Email];
            System.assertEquals(1, sentEmails.size(), 'Email should be sent for Contact undelete');
        }
    }
                 }









public class ContactTriggerHandler {
     public static void handleTrigger(Boolean isInsert, Boolean isUpdate, Boolean isDelete, Boolean isAfter, 
                                     List<Contact> newContacts, List<Contact> oldContacts) {
        if (isAfter) {
            if (isInsert) {
                notifyCreated(newContacts);
            } else if (isUpdate) {
                notifyModified(newContacts);
            } else if (isDelete) {
                notifyDeleted(oldContacts);
            } else if (!isInsert && !isUpdate && !isDelete) {
                notifyUndelete(newContacts);
            }
        }
    }

 private static void notifyCreated(List<Contact> newContacts) {
        for (Contact contact : [SELECT Id, Name, Email FROM Contact WHERE Id IN :newContacts]) {
            if (contact.Email != null) {
               List<Opportunity> opportunities = [SELECT Id FROM Opportunity WHERE Contact_Id__c != null];
                        Integer numOfOpportunities = 0;

                  for (Opportunity opp : opportunities) {
                       numOfOpportunities++;
                    }
                List<Case> cases = [SELECT Id FROM Case WHERE ContactId != null];
                    Integer numOfCases = 0;

                  for (Case c : cases) {
                    numOfCases++;
                    }
                Integer oppCount=numOfOpportunities;
                Integer caseCount=numOfCases;
                Messaging.SingleEmailMessage email = new Messaging.SingleEmailMessage();
                email.setToAddresses(new String[] {contact.Email});
                email.setSubject('Welcome! Your Contact Record has been Created');
                email.setPlainTextBody('Dear ' + contact.Name + ',\n\nYour contact record has been created.\n\n' +
                                       'Number of Opportunities: ' + oppCount + '\n' +
                                       'Number of Cases: ' + caseCount + '\n\nBest regards,\nYour Team');
                Messaging.sendEmail(new Messaging.SingleEmailMessage[] { email });
            }
        }
    }

    private static void notifyModified(List<Contact> newContacts) {
        for (Contact contact : [SELECT Id, Name, Email FROM Contact WHERE Id IN :newContacts]) {
            if (contact.Email != null) {
                Messaging.SingleEmailMessage email = new Messaging.SingleEmailMessage();
                email.setToAddresses(new String[] {contact.Email});
                email.setSubject('Your Contact Record has been Updated');
                email.setPlainTextBody('Dear ' + contact.Name + ',\n\nYour contact record has been modified.\n\nBest regards,\nYour Team');
                Messaging.sendEmail(new Messaging.SingleEmailMessage[] { email });
            }
        }
    }

    private static void notifyDeleted(List<Contact> oldContacts) {
        for (Contact contact : oldContacts) {
            if (contact.Email != null) {
               
                List<Opportunity> opportunities = [SELECT Id FROM Opportunity WHERE Contact_Id__c != null];
                        Integer numOfOpportunities = 0;

                  for (Opportunity opp : opportunities) {
                       numOfOpportunities++;
                    }
                List<Case> cases = [SELECT Id FROM Case WHERE ContactId != null];
                    Integer numOfCases = 0;

                  for (Case c : cases) {
                    numOfCases++;
                    }
                Integer oppCount=numOfOpportunities;
                Integer caseCount=numOfCases;
                Messaging.SingleEmailMessage email = new Messaging.SingleEmailMessage();
                email.setToAddresses(new String[] {contact.Email});
                email.setSubject('Your Contact Record has been Deleted');
                email.setPlainTextBody('Dear ' + contact.FirstName + ',\n\nYour contact record has been deleted.\n\n' +
                                       'Number of Opportunities: ' + oppCount + '\n' +
                                       'Number of Cases: ' + caseCount + '\n\nBest regards,\nYour Team');
                Messaging.sendEmail(new Messaging.SingleEmailMessage[] { email });
            }
        }
    }

    private static void notifyUndelete(List<Contact> newContacts) {
        for (Contact contact : [SELECT Id, Name, Email FROM Contact WHERE Id IN :newContacts]) {
            if (contact.Email != null) {
                Messaging.SingleEmailMessage email = new Messaging.SingleEmailMessage();
                email.setToAddresses(new String[] {contact.Email});
                email.setSubject('Your Contact Record has been Restored');
                email.setPlainTextBody('Dear ' + contact.FirstName + ',\n\nYour contact record has been restored.\n\nBest regards,\nYour Team');
                Messaging.sendEmail(new Messaging.SingleEmailMessage[] { email });
            }
        }
    }
}



List<Case> cases = [SELECT Id FROM Case WHERE ContactId != null];
Integer numOfCases = 0;

for (Case c : cases) {
    numOfCases++;
}
System.debug('Total Cases: ' + numOfCases);



List<Opportunity> opportunities = [SELECT Id FROM Opportunity WHERE ContactId != null];
Integer numOfOpportunities = 0;

for (Opportunity opp : opportunities) {
    numOfOpportunities++;
}
System.debug('Total Opportunities: ' + numOfOpportunities);




Integer numOfCases = [SELECT Id FROM Case WHERE ContactId != null].size();
System.debug('Total Cases: ' + numOfCases);




Integer numOfOpportunities = [SELECT Id FROM Opportunity WHERE ContactId != null].size();
System.debug('Total Opportunities: ' + numOfOpportunities);






Integer numOfCases = 0;

AggregateResult[] caseResults = [
    SELECT COUNT(Id) totalCases 
    FROM Case 
    WHERE ContactId != null
];

if (!caseResults.isEmpty()) {
    numOfCases = (Integer) caseResults[0].get('totalCases');
}
System.debug('Total Cases: ' + numOfCases);




SELECT ContactId, COUNT(Id) NumOfCases 
FROM Case 
WHERE ContactId != null 
GROUP BY ContactId




SELECT ContactId, COUNT(Id) NumOfOpportunities 
FROM Opportunity 
WHERE ContactId != null 
GROUP BY ContactId





public static void getOpportunityCount(Id contactId) {
    // Aggregate query
    AggregateResult[] results = [
        SELECT COUNT(Id) count, Credited_RES_CRM__c
        FROM Opportunity
        WHERE Credited_RES_CRM__c = :contactId
        GROUP BY Credited_RES_CRM__c
    ];

    // Store count
    Integer oppCount = !results.isEmpty() ? (Integer)results[0].get('count') : 0;

    // Log or use the count
    System.debug('Number of Opportunities for contact: ' + oppCount);
}






SELECT count(id), Credited_RES_CRM__c FROM Opportunity GROUP BY Credited_RES_CRM__c


// Step 1: Create an Account
Account account = new Account(Name = 'Test Account');
insert account;

// Step 2: Create a Contact
Contact contact = new Contact(FirstName = 'John', LastName = 'Doe', Email = 'thanush.jagadeeshaiah@voya.com', AccountId = account.Id);
insert contact;

// Step 3: Create an Opportunity
Opportunity opportunity = new Opportunity(Name = 'New Business Opportunity', StageName = 'Prospecting', CloseDate = Date.today().addDays(30), AccountId = account.Id);
insert opportunity;

// Step 4: Create an OpportunityContactRole to link the contact to the opportunity
OpportunityContactRole contactRole = new OpportunityContactRole(ContactId = contact.Id, OpportunityId = opportunity.Id, Role = 'Decision Maker', IsPrimary = true);
insert contactRole;

// Step 5: Create a Case
Case caseRecord = new Case(Subject = 'Customer Support Request', Status = 'New', ContactId = contact.Id, AccountId = account.Id);
insert caseRecord;

// Step 6: Count Opportunities
Integer opportunityCount = [SELECT COUNT() FROM OpportunityContactRole WHERE ContactId = :contact.Id];

// Step 7: Count Cases
Integer caseCount = [SELECT COUNT() FROM Case WHERE ContactId = :contact.Id];

// Step 8: Print the Counts
System.debug('Number of Opportunities associated with the contact: ' + opportunityCount);
System.debug('Number of Cases associated with the contact: ' + caseCount);







public class ContactTriggerHandler {
    public static void handleTrigger(Boolean isInsert, Boolean isUpdate, Boolean isDelete, Boolean isAfter, 
                                     List<Contact> newContacts, List<Contact> oldContacts) {
        if (isAfter) {
            if (isInsert) {
                notifyCreated(newContacts);
            } else if (isUpdate) {
                notifyModified(newContacts);
            } else if (isDelete) {
                notifyDeleted(oldContacts);
            } else if (!isInsert && !isUpdate && !isDelete) {
                notifyUndelete(newContacts);
            }
        }
    }

    private static void notifyCreated(List<Contact> newContacts) {
        for (Contact contact : [SELECT Id, Name, Email FROM Contact WHERE Id IN :newContacts]) {
            if (contact.Email != null) {
                Integer oppCount = [SELECT COUNT() FROM Opportunity WHERE credited_RES_CRM__c = :contact.Id];
                Integer caseCount = [SELECT COUNT() FROM Case WHERE ContactId = :contact.Id];

                Messaging.SingleEmailMessage email = new Messaging.SingleEmailMessage();
                email.setToAddresses(new String[] {contact.Email});
                email.setSubject('Welcome! Your Contact Record has been Created');
                email.setPlainTextBody('Dear ' + contact.Name + ',\n\nYour contact record has been created.\n\n' +
                                       'Number of Opportunities: ' + oppCount + '\n' +
                                       'Number of Cases: ' + caseCount + '\n\nBest regards,\nYour Team');
                Messaging.sendEmail(new Messaging.SingleEmailMessage[] { email });
            }
        }
    }

    private static void notifyModified(List<Contact> newContacts) {
        for (Contact contact : [SELECT Id, Name, Email FROM Contact WHERE Id IN :newContacts]) {
            if (contact.Email != null) {
                Messaging.SingleEmailMessage email = new Messaging.SingleEmailMessage();
                email.setToAddresses(new String[] {contact.Email});
                email.setSubject('Your Contact Record has been Updated');
                email.setPlainTextBody('Dear ' + contact.Name + ',\n\nYour contact record has been modified.\n\nBest regards,\nYour Team');
                Messaging.sendEmail(new Messaging.SingleEmailMessage[] { email });
            }
        }
    }

    private static void notifyDeleted(List<Contact> oldContacts) {
        for (Contact contact : oldContacts) {
            if (contact.Email != null) {
                Integer oppCount = [SELECT COUNT() FROM Opportunity WHERE credited_RES_CRM__c = :contact.Id];
                Integer caseCount = [SELECT COUNT() FROM Case WHERE ContactId = :contact.Id];

                Messaging.SingleEmailMessage email = new Messaging.SingleEmailMessage();
                email.setToAddresses(new String[] {contact.Email});
                email.setSubject('Your Contact Record has been Deleted');
                email.setPlainTextBody('Dear ' + contact.Name + ',\n\nYour contact record has been deleted.\n\n' +
                                       'Number of Opportunities: ' + oppCount + '\n' +
                                       'Number of Cases: ' + caseCount + '\n\nBest regards,\nYour Team');
                Messaging.sendEmail(new Messaging.SingleEmailMessage[] { email });
            }
        }
    }

    private static void notifyUndelete(List<Contact> newContacts) {
        for (Contact contact : [SELECT Id, Name, Email FROM Contact WHERE Id IN :newContacts]) {
            if (contact.Email != null) {
                Messaging.SingleEmailMessage email = new Messaging.SingleEmailMessage();
                email.setToAddresses(new String[] {contact.Email});
                email.setSubject('Your Contact Record has been Restored');
                email.setPlainTextBody('Dear ' + contact.Name + ',\n\nYour contact record has been restored.\n\nBest regards,\nYour Team');
                Messaging.sendEmail(new Messaging.SingleEmailMessage[] { email });
            }
        }
    }
}








public class ContactTriggerHandler {
    public static void handleTrigger(Boolean isInsert, Boolean isUpdate, Boolean isDelete, Boolean isAfter, 
                                     List<Contact> newContacts, List<Contact> oldContacts) {
        if (isAfter) {
            if (isInsert) {
                notifyCreated(newContacts);
            } else if (isUpdate) {
                notifyModified(newContacts);
            } else if (isDelete) {
                notifyDeleted(oldContacts);
            } else if (!isInsert && !isUpdate && !isDelete) {
                notifyUndelete(newContacts);
            }
        }
    }

    private static void notifyCreated(List<Contact> newContacts) {
        for (Contact contact : [SELECT Id, Name, Owner.Email FROM Contact WHERE Id IN :newContacts]) {
            Integer oppCount = [SELECT COUNT() FROM Opportunity WHERE credited_RES_CRM__c = :contact.Id];
            Integer caseCount = [SELECT COUNT() FROM Case WHERE ContactId = :contact.Id];

            if (contact.Owner.Email != null) {
                sendEmail(contact.Owner.Email, 'Contact Created', 
                          'A new contact has been created. Opportunities: ' + oppCount + ', Cases: ' + caseCount);
            }
        }
    }

    private static void notifyModified(List<Contact> newContacts) {
        for (Contact contact : [SELECT Id, Name, Owner.Email FROM Contact WHERE Id IN :newContacts]) {
            if (contact.Owner.Email != null) {
                sendEmail(contact.Owner.Email, 'Contact Modified', 
                          'A contact has been modified: ' + contact.Name);
            }
        }
    }

    private static void notifyDeleted(List<Contact> oldContacts) {
        for (Contact contact : [SELECT Id, Name, Owner.Email FROM Contact WHERE Id IN :oldContacts]) {
            Integer oppCount = [SELECT COUNT() FROM Opportunity WHERE credited_RES_CRM__c = :contact.Id];
            Integer caseCount = [SELECT COUNT() FROM Case WHERE ContactId = :contact.Id];

            if (contact.Owner.Email != null) {
                sendEmail(contact.Owner.Email, 'Contact Deleted', 
                          'A contact has been deleted. Opportunities: ' + oppCount + ', Cases: ' + caseCount);
            }
        }
    }

    private static void notifyUndelete(List<Contact> newContacts) {
        for (Contact contact : [SELECT Id, Name, Owner.Email FROM Contact WHERE Id IN :newContacts]) {
            if (contact.Owner.Email != null) {
                sendEmail(contact.Owner.Email, 'Contact Undeleted', 
                          'A contact has been undeleted: ' + contact.Name);
            }
        }
    }

    private static void sendEmail(String toAddress, String subject, String body) {
        Messaging.SingleEmailMessage email = new Messaging.SingleEmailMessage();
        email.setToAddresses(new String[] {toAddress});
        email.setSubject(subject);
        email.setPlainTextBody(body);
        Messaging.sendEmail(new Messaging.SingleEmailMessage[] { email });
    }
}






SELECT COUNT() FROM OpportunityContactRole WHERE ContactId = 'CONTACT_ID_HERE'
SELECT COUNT() FROM Case WHERE ContactId = 'CONTACT_ID_HERE'




// Step 1: Create an Account
Account account = new Account(Name = 'Test Account');
insert account;

// Step 2: Create a Contact
Contact contact = new Contact(FirstName = 'John', LastName = 'Doe', Email = 'john.doe@example.com', AccountId = account.Id);
insert contact;

// Step 3: Create an Opportunity
Opportunity opportunity = new Opportunity(Name = 'New Business Opportunity', StageName = 'Prospecting', CloseDate = Date.today().addDays(30), AccountId = account.Id);
insert opportunity;

// Step 4: Create an OpportunityContactRole to link the contact to the opportunity
OpportunityContactRole contactRole = new OpportunityContactRole(ContactId = contact.Id, OpportunityId = opportunity.Id, Role = 'Decision Maker', IsPrimary = true);
insert contactRole;









@IsTest
public class ContactTriggerHandlerTest {
    @IsTest
    static void testContactTrigger() {
        // Test Data
        Account testAccount = new Account(Name = 'Test Account');
        insert testAccount;

        Contact testContact = new Contact(FirstName = 'Test', LastName = 'Contact', AccountId = testAccount.Id);
        
        // Test Insert
        Test.startTest();
        insert testContact;
        Test.stopTest();

        // Verify Insert Notification
        // Add assertions for email sent if appropriate

        // Test Update
        testContact.LastName = 'Updated Contact';
        Test.startTest();
        update testContact;
        Test.stopTest();

        // Verify Update Notification
        // Add assertions for email sent if appropriate

        // Test Delete
        Test.startTest();
        delete testContact;
        Test.stopTest();

        // Verify Delete Notification
        // Add assertions for email sent if appropriate

        // Test Undelete
        Test.startTest();
        undelete testContact;
        Test.stopTest();

        // Verify Undelete Notification
        // Add assertions for email sent if appropriate
    }
}






public class ContactTriggerHandler {
    public static void handleTrigger(Boolean isInsert, Boolean isUpdate, Boolean isDelete, Boolean isAfter, 
                                     List<Contact> newContacts, List<Contact> oldContacts) {
        if (isAfter) {
            if (isInsert) {
                notifyCreated(newContacts);
            } else if (isUpdate) {
                notifyModified(newContacts);
            } else if (isDelete) {
                notifyDeleted(oldContacts);
            } else if (!isInsert && !isUpdate && !isDelete) {
                notifyUndelete(newContacts);
            }
        }
    }

    private static void notifyCreated(List<Contact> newContacts) {
        for (Contact contact : newContacts) {
            Integer oppCount = [SELECT COUNT() FROM Opportunity WHERE ContactId = :contact.Id];
            Integer caseCount = [SELECT COUNT() FROM Case WHERE ContactId = :contact.Id];

            Messaging.SingleEmailMessage email = new Messaging.SingleEmailMessage();
            email.setToAddresses(new String[] {'admin@example.com'});
            email.setSubject('Contact Created');
            email.setPlainTextBody('A new contact has been created. Opportunities: ' + oppCount + ', Cases: ' + caseCount);
            Messaging.sendEmail(new Messaging.SingleEmailMessage[] { email });
        }
    }

    private static void notifyModified(List<Contact> newContacts) {
        for (Contact contact : newContacts) {
            Messaging.SingleEmailMessage email = new Messaging.SingleEmailMessage();
            email.setToAddresses(new String[] {'admin@example.com'});
            email.setSubject('Contact Modified');
            email.setPlainTextBody('A contact has been modified: ' + contact.Name);
            Messaging.sendEmail(new Messaging.SingleEmailMessage[] { email });
        }
    }

    private static void notifyDeleted(List<Contact> oldContacts) {
        for (Contact contact : oldContacts) {
            Integer oppCount = [SELECT COUNT() FROM Opportunity WHERE ContactId = :contact.Id];
            Integer caseCount = [SELECT COUNT() FROM Case WHERE ContactId = :contact.Id];

            Messaging.SingleEmailMessage email = new Messaging.SingleEmailMessage();
            email.setToAddresses(new String[] {'admin@example.com'});
            email.setSubject('Contact Deleted');
            email.setPlainTextBody('A contact has been deleted. Opportunities: ' + oppCount + ', Cases: ' + caseCount);
            Messaging.sendEmail(new Messaging.SingleEmailMessage[] { email });
        }
    }

    private static void notifyUndelete(List<Contact> newContacts) {
        for (Contact contact : newContacts) {
            Messaging.SingleEmailMessage email = new Messaging.SingleEmailMessage();
            email.setToAddresses(new String[] {'admin@example.com'});
            email.setSubject('Contact Undeleted');
            email.setPlainTextBody('A contact has been undeleted: ' + contact.Name);
            Messaging.sendEmail(new Messaging.SingleEmailMessage[] { email });
        }
    }
}






trigger ContactTrigger on Contact (after insert, after update, after delete, after undelete) {
    ContactTriggerHandler.handleTrigger(Trigger.isInsert, Trigger.isUpdate, Trigger.isDelete, Trigger.isAfter, Trigger.new, Trigger.old);
}









1.	Send notification when contact record(s) get:

a.	Created
b.	Modified
c.	Deleted
d.	Undelete

2.	Should display the count of opportunities and cases associated with the contact(s) when created and deleted.

NOTE: Request you to follow the salesforce best practice to implement and to write the apex test classes for your apex components if any.





<apex:page>
    <!-- Include the Lightning Out library -->
    <script src="/lightning/lightning.out.js"></script>
    <script>
        // Initialize Lightning Out
        $Lightning.use(
            "c:articleEditSuggestAura", // Name of your Aura Wrapper App
            function () {
                // Embed the Lightning Component
                $Lightning.createComponent(
                    "c:articleEditSuggestLWC", // LWC Name
                    {}, // Attributes to pass (if any)
                    "lightningContainer", // DOM element ID to render the component
                    function (cmp) {
                        // Save the component instance for later use
                        window.articleEditSuggestComponent = cmp;
                    }
                );
            }
        );

        // Function to call the closeTab method in the LWC
        function closeSubtabsFromVF() {
            if (window.articleEditSuggestComponent) {
                window.articleEditSuggestComponent.closeTab();
            } else {
                alert("Lightning Component is not yet ready!");
            }
        }
    </script>

    <!-- Container for the Lightning Component -->
    <div id="lightningContainer"></div>

    <!-- Button to invoke the Lightning Component method -->
    <button onclick="closeSubtabsFromVF()">Close Subtabs</button>
</apex:page>





import { LightningElement } from 'lwc';

export default class ArticleEditSuggestLWC extends LightningElement {
    // Method to close tabs
    closeTab() {
        console.log('Close Tab method called');
        // Add your logic to close tabs here
    }
}





<aura:component implements="flexipage:availableForAllPageTypes,force:appHostable" access="global">
    <c:articleEditSuggestLWC />
</aura:component>






<apex:page>
    <!-- Include the Lightning Out library -->
    <script src="/lightning/lightning.out.js"></script>
    <script>
        // Initialize Lightning Out
        $Lightning.use(
            "c:articleEditSuggestAura", // Name of your Aura Wrapper App
            function () {
                // Embed the Lightning Component
                $Lightning.createComponent(
                    "c:articleEditSuggestLWC", // LWC Name
                    {}, // Attributes to pass (if any)
                    "lightningContainer", // DOM element ID to render the component
                    function (cmp) {
                        // Save the component instance for later use
                        window.articleEditSuggestComponent = cmp;
                    }
                );
            }
        );

        // Function to call the closeTab method in the LWC
        function closeSubtabsFromVF() {
            if (window.articleEditSuggestComponent) {
                window.articleEditSuggestComponent.closeTab();
            } else {
                alert("Lightning Component is not yet ready!");
            }
        }
    </script>

    <!-- Container for the Lightning Component -->
    <div id="lightningContainer"></div>

    <!-- Button to invoke the Lightning Component method -->
    <button onclick="closeSubtabsFromVF()">Close Subtabs</button>
</apex:page>








<aura:component implements="flexipage:availableForAllPageTypes,force:appHostable" access="global">
    <aura:attribute name="workspace" type="Object" />
    <lightning:workspaceAPI aura:id="workspace" />

    <!-- Expose method for external call -->
    <aura:method name="closeAllSubtabs" action="{!c.closeSubtabs}" />

</aura:component>

    ({
    closeSubtabs: function (component, event, helper) {
        let workspaceAPI = component.find("workspace");
        workspaceAPI.getFocusedTabInfo()
            .then((response) => {
                let parentTabId = response.tabId;
                return workspaceAPI.getTabInfo({ tabId: parentTabId });
            })
            .then((tabInfo) => {
                let subtabs = tabInfo.subtabs || [];
                subtabs.forEach((subtab) => {
                    workspaceAPI.closeTab({ tabId: subtab.tabId });
                });
                console.log('All subtabs closed successfully');
            })
            .catch((error) => {
                console.error("Error closing subtabs: ", error);
            });
    }
});

<apex:page>
    <script src="/lightning/lightning.out.js"></script>
    <script>
        $Lightning.use(
            "c:CloseSubtabsApp", // Your Aura App Name
            function () {
                // Create the Lightning Component
                $Lightning.createComponent(
                    "c:CloseSubtabs", // Lightning Component Name
                    {},
                    "lightningContainer", // DOM container for the component
                    function (cmp) {
                        // Expose the component globally for further interaction
                        window.closeSubtabsComponent = cmp;
                    }
                );
            }
        );

        // Function to call the Lightning Component method
        function closeSubtabsFromVF() {
            if (window.closeSubtabsComponent) {
                window.closeSubtabsComponent.closeAllSubtabs();
            } else {
                alert("Lightning Component is not yet ready!");
            }
        }
    </script>

    <!-- Container for the Lightning Component -->
    <div id="lightningContainer"></div>

    <!-- Button to call Lightning Component method -->
    <button onclick="closeSubtabsFromVF()">Close Subtabs</button>
</apex:page>















import { LightningElement } from 'lwc';
import { NavigationMixin } from 'lightning/navigation';
import { ShowToastEvent } from 'lightning/platformShowToastEvent';

export default class CloseSubtabs extends NavigationMixin(LightningElement) {
    handleCloseSubtabs() {
        // Access the workspace API
        const workspaceAPI = this.template.querySelector('lightning-workspace-api');

        if (workspaceAPI) {
            workspaceAPI
                .getFocusedTabInfo()
                .then((focusedTab) => {
                    const parentTabId = focusedTab.tabId;
                    return workspaceAPI.getTabInfo({ tabId: parentTabId });
                })
                .then((tabInfo) => {
                    const subtabs = tabInfo.subtabs || [];
                    subtabs.forEach((subtab) => {
                        workspaceAPI.closeTab({ tabId: subtab.tabId });
                    });

                    this.dispatchEvent(
                        new ShowToastEvent({
                            title: 'Success',
                            message: 'All subtabs have been closed.',
                            variant: 'success',
                        })
                    );
                })
                .catch((error) => {
                    this.dispatchEvent(
                        new ShowToastEvent({
                            title: 'Error',
                            message: `Failed to close subtabs: ${error.message}`,
                            variant: 'error',
                        })
                    );
                });
        } else {
            this.dispatchEvent(
                new ShowToastEvent({
                    title: 'Error',
                    message: 'Workspace API is not accessible.',
                    variant: 'error',
                })
            );
        }
    }
}









// Step 1: Create a test Contact (with the current user as the owner)
Contact testContact = new Contact(
    FirstName = 'Test',
    LastName = 'Contact',
    Email = 'testcontact@example.com',
    OwnerId = UserInfo.getUserId() // Assign current user as the owner
);
insert testContact;

// Step 2: Fetch the contact and its owner's email
Contact c = [SELECT Id, Name, OwnerId FROM Contact WHERE Id = :testContact.Id];
User owner = [SELECT Email FROM User WHERE Id = :c.OwnerId];

if (owner.Email != null) {
    // Step 3: Send a test notification email
    System.Messaging.SingleEmailMessage mail = new System.Messaging.SingleEmailMessage();
    mail.setToAddresses(new String[] { owner.Email });
    mail.setSubject('Contact Deletion Notification');
    mail.setPlainTextBody('The contact "' + c.Name + '" has been deleted.');
    System.Messaging.sendEmail(new System.Messaging.SingleEmailMessage[] { mail });
}

// Step 4: Cleanup - Delete the test contact
delete testContact;

// Optional: Add debug logs to verify
System.debug('Notification email sent to: ' + owner.Email);







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
