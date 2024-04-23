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
