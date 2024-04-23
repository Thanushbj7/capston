@isTest
static void testInitializeAndLoadPlanData() {
    // Create a mock Account record
    Account mockAccount = new Account(Name = 'Test Account'); // Example Account data
    insert mockAccount;

    // Create a mock CTI_Console_Pop__c record linked to the Account
    CTI_Console_Pop__c mockCTIConsolePop = new CTI_Console_Pop__c(
        Account__c = mockAccount.Id, // Link the CTI_Console_Pop__c record to the Account
        CTI_Params__c = 'clientId:123456789; VRUAPP:Test Value' // Example CTI_Params__c value
        // Add more fields as needed
    );
    insert mockCTIConsolePop;

    // Create a mock Case record
    Case mockCase = new Case(
        AccountId = mockAccount.Id, // Link the Case record to the Account
        Offers_Available__c = true // Example Offers_Available__c value
        // Add more fields as needed
    );
    insert mockCase;

    // Call the method being tested
    List<UltimatePopControllerHelper.SearchResult> results = YourClassName.initializeAndLoadPlanData(mockAccount.Id);

    // Perform assertions
    System.assertNotEquals(null, results); // Ensure that results are not null
    // Add more assertions as needed
}






@isTest
static void testInitializeAndLoadPlanData() {
    // Create a mock CTI_Console_Pop__c record
    CTI_Console_Pop__c mockCTIConsolePop = new CTI_Console_Pop__c(
        Account__c = '001XXXXXXXXXXXX', // Example Account Id
        CTI_Params__c = 'clientId:123456789; VRUAPP:Test Value', // Example CTI_Params__c value
        // Add more fields as needed
    );
    insert mockCTIConsolePop;

    // Create a mock Case record
    Case mockCase = new Case(
        Id = '001XXXXXXXXXXXX', // Example Case Id
        Offers_Available__c = true // Example Offers_Available__c value
        // Add more fields as needed
    );
    insert mockCase;

    // Call the method being tested
    List<UltimatePopControllerHelper.SearchResult> results = YourClassName.initializeAndLoadPlanData(mockCTIConsolePop.Account__c);

    // Perform assertions
    System.assertNotEquals(null, results); // Ensure that results are not null
    // Add more assertions as needed
}







public static List<UltimatePopControllerHelper.SearchResult> initializeAndLoadPlanData(String clientId){
    Case currentCase = new Case();
    system.debug('Acc Id '+clientId);
	String clientSSN = '';
	String test = '';

   
    
    CTI_Console_Pop__c  ctiConsolePop = [select Id, Account__r.SSN__c, CTI_Params__c, DC_Serialized_Result__c, Case__c, 
        Case__r.Id, Case__r.CaseNumber, Offer_Pop__c, Offer_Pop__r.Id from CTI_Console_Pop__c 
        where account__c = :clientId order by LastModifiedDate  desc limit 1];
        String[] keyValuePairs = ctiConsolePop.CTI_Params__c.split(';');
        for(String pair : keyValuePairs) {
            String[] keyValue = pair.split(':');
            if(keyValue.size() == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
        
                if(key == 'clientId') {
                    clientSSN = value;
                } else if(key == 'VRUAPP') {
                    test = value;
                }
            }
        }
        
        System.debug('clientSSN: ' + clientSSN);
        System.debug('test: ' + test);
    Account client = UltimatePopControllerHelper.getSFDCClientInfo(ctiConsolePop.Account__r.SSN__c, true);
    
    List<Case> currentCaseList = [select Id, CaseNumber, Offers_Available__c from Case where Id = :ctiConsolePop.Case__r.Id];
    if(currentCaseList != null && currentCaseList.size() > 0)
        currentCase = currentCaseList.get(0);
    	
    String ctiParams = ctiConsolePop.CTI_Params__c;
    CurCase=ctiConsolePop.Case__r.Id;
    system.debug('=================================== ctiParams ' + ctiParams);
   
    
    return initializeAvailablePlans(currentCase,clientSSN,test,client);
    






}
