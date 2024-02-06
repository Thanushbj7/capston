Public with sharing class CaseRelatedListApex{
   
    @AuraEnabled(cacheable=true)
    public static List<Case_Actions__c> getObject(){  
        LIST< Case_Actions__c > objCaseList = new LIST< Case_Actions__c >(); 
        
        objCaseList =[SELECT  Case__r.CaseNumber,Case__r.CreatedDate,PlanID_Text__c,Call_Activity__c,Call_Type__c FROM Case_Actions__c where  Case__r.Account.SSN__c IN  ('010820241')];
        
        return  objCaseList;
        
          }
    public static List<Case_Actions__c> getcaseactionrecords(){
        
        List<Case_Actions__c> Caseaction = new List<Case_Actions__c>();
       // if((String.isBlank(Caseid) || Caseid==''||Caseid==null) &&(String.isBlank(clientStatus) || clientStatus==''||clientStatus==null)){
            Caseaction = [SELECT  Case__r.CaseNumber,Case__r.CreatedDate,PlanID_Text__c,Call_Activity__c,Call_Type__c 
                               from Case_Actions__c where  Case__r.Account.SSN__c IN  ('010820241') ];
       
       // }
        
        return generateWrapperData(Caseaction);
    }
    
    private static List<Case_Actions__c> generateWrapperData(List<Case_Actions__c> Caseactionlist) {
        List<Case_Actions__c> caseactionWrapperList = new List<Case_Actions__c>();
        
        for (Case_Actions__c Caction : Caseactionlist) {
            
            Case_Actions__c CactionWrap = new Case_Actions__c();
            
            CactionWrap.id = Caction.Id;
            CactionWrap.PlanID_Text__c = Caction.PlanID_Text__c;
            CactionWrap.Call_Activity__c = Caction.Call_Activity__c;
            CactionWrap.Call_Type__c = Caction.Call_Type__c;          
         
            caseactionWrapperList.add(CactionWrap); 
        }
        
        return caseactionWrapperList;
    }
    
}




public with sharing class CaseRelatedListApex {
   
    @AuraEnabled(cacheable=true)
    public static List<Case_Actions__c> getObject(){  
        List<Case_Actions__c> objCaseList = new List<Case_Actions__c>(); 
        
        objCaseList = [SELECT Case__r.CaseNumber, Case__r.CreatedDate, PlanID_Text__c, Call_Activity__c, Call_Type__c 
                       FROM Case_Actions__c 
                       WHERE Case__r.Account.SSN__c = '010820241'];
        
        return objCaseList;
    }

    @AuraEnabled(cacheable=true)
    public static List<Case_Actions__c> getCaseActionRecords(){
        List<Case_Actions__c> caseActionList = [SELECT Case__r.CaseNumber, Case__r.CreatedDate, PlanID_Text__c, Call_Activity__c, Call_Type__c 
                                                FROM Case_Actions__c 
                                                WHERE Case__r.Account.SSN__c = '010820241'];
       
        return generateWrapperData(caseActionList);
    }
    
    private static List<Case_Actions__c> generateWrapperData(List<Case_Actions__c> caseActionList) {
        List<Case_Actions__c> caseActionWrapperList = new List<Case_Actions__c>();
        
        for (Case_Actions__c caseAction : caseActionList) {
            Case_Actions__c caseActionWrap = new Case_Actions__c(
                Id = caseAction.Id,
                PlanID_Text__c = caseAction.PlanID_Text__c,
                Call_Activity__c = caseAction.Call_Activity__c,
                Call_Type__c = caseAction.Call_Type__c
            );
         
            caseActionWrapperList.add(caseActionWrap); 
        }
        
        return caseActionWrapperList;
    }
}
