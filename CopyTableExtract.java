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








<template>
		<div style="height: 300px;">
				<h2 slot="title">
            <lightning-icon icon-name="action:new_note"  title=" Case History"></lightning-icon>
           <b>Case History</b>
        </h2>
		
		<lightning-datatable
        data={data}
        columns={columns}
        key-field="Id"
			  show-row-number-column
			  row-number-offset={rowOffset}
			  hide-checkbox-column       
    >
    </lightning-datatable>
		</div>
		 
</template>
