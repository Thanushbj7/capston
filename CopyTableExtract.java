public class caseHistoryLWC {
    
     @AuraEnabled(cacheable=true)
    public static List<CaseWrapper> fetchWrapperCases(){
        
        List<CaseWrapper> wrapperList= new List<CaseWrapper>();
        List<Case_Actions__c>  caseList =[SELECT  Case__r.CaseNumber,Case__r.CreatedDate,PlanID_Text__c,Call_Activity__c,Call_Type__c FROM Case_Actions__c where ( Case__r.Account.SSN__c ='010820241' and PlanID_Text__c!='' and Call_Type__c!=null and Call_Activity__c!=null)  ORDER BY Case__r.CaseNumber];
        for( Case_Actions__c ca :caseList){
            wrapperList.add( new CaseWrapper(ca));
        }
            system.debug('wrapperList '+wrapperList);
   
        return  wrapperList;
    }
    
    public class CaseWrapper{
        @AuraEnabled public string caseNumber;
        @AuraEnabled  public datetime createdDate;
        @AuraEnabled public string planId;
        @AuraEnabled public string callTypeInquiry;
        @AuraEnabled public string callTypeTransaction;
        @AuraEnabled public string callTypeAccountMaintenance;
        @AuraEnabled public string callTypeForms;
         @AuraEnabled public string callTypeOthers;
         
        
     /*   @AuraEnabled public map<String , String> callTypeInquiry;
        @AuraEnabled public map<String , String> callTypeTransaction;
        @AuraEnabled public map<String , String> callTypeAccountmaintenace;
        @AuraEnabled public map<String , String> callTypeForms;
        @AuraEnabled public map<String , String> callTypeOthers;*/
        
        
        public CaseWrapper(Case_Actions__c ca){
            this.caseNumber=ca.Case__r.CaseNumber;
            this.createdDate=ca.Case__r.CreatedDate;
            this.planId=ca.PlanID_Text__c;
            
             if(ca.Call_Activity__c=='Inquiry' && ca.Call_Type__c !=''){
                this.callTypeInquiry = ca.Call_Type__c ;
            } 
            if(ca.Call_Activity__c=='Transaction' && ca.Call_Type__c !=''){
                this.callTypeTransaction = ca.Call_Type__c ;
            } 
            if(ca.Call_Activity__c=='Account Maintenance' && ca.Call_Type__c !=''){
                this.callTypeAccountMaintenance = ca.Call_Type__c ;
            } 
            if(ca.Call_Activity__c=='Forms' && ca.Call_Type__c !=''){
                this.callTypeForms = ca.Call_Type__c ;
            } 
            if((ca.Call_Activity__c=='Hand-Off Case' || ca.Call_Activity__c=='NIGO Callback') && ca.Call_Type__c !=''){
                this.callTypeOthers = ca.Call_Type__c ;
            } 
         
         /*   if(ca.Call_Activity__c=='Transaction'){
                this.callTypeInquiry.put('Inquiry',ca.Call_Type__c );
            }  
            if(ca.Call_Activity__c=='Transaction'){
                this.callTypeInquiry.put('Inquiry',ca.Call_Type__c );
            }  
            if(ca.Call_Activity__c=='Transaction'){
                this.callTypeInquiry.put('Inquiry',ca.Call_Type__c );
            }  
            if(ca.Call_Activity__c=='Transaction'){
                this.callTypeInquiry.put('Inquiry',ca.Call_Type__c );
            }  */
         
        }
        
    }
    
}
