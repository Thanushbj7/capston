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




				  import { LightningElement,api,track,wire } from 'lwc';
import { publish, MessageContext } from 'lightning/messageService';
import EXAMPLE_MESSAGE_CHANNEL from '@salesforce/messageChannel/ExampleMessageChannel__c';
import getObject from '@salesforce/apex/CaseRelatedListApex.getObject';
//import getcaseactionrecords from '@salesforce/apex/CaseRelatedListApex.getcaseactionrecords';

const columns = [
    { label: 'Case Number', fieldName: 'CaseNumber' },
    { label: 'Date', fieldName: 'CreatedDate' },
    { label: 'Plan Id', fieldName: 'PlanID_Text__c' },
    { label: 'Inquiry', fieldName: 'Call_Type__c' },
    { label: 'Transactions', fieldName: 'Call_Type__c' },
		 { label: 'Account Maintenance', fieldName: 'Call_Type__c' },
		 { label: 'Forms', fieldName: 'Call_Type__c'},
		{ label: 'Others', fieldName: 'Call_Type__c'},
];



import { LightningElement, api, track, wire } from 'lwc';
import { publish, MessageContext } from 'lightning/messageService';
import EXAMPLE_MESSAGE_CHANNEL from '@salesforce/messageChannel/ExampleMessageChannel__c';
import getObject from '@salesforce/apex/CaseRelatedListApex.getObject';

const columns = [
    { label: 'Case Number', fieldName: 'CaseNumber' },
    { label: 'Date', fieldName: 'CreatedDate' },
    { label: 'Plan Id', fieldName: 'PlanID_Text__c' },
    { label: 'Inquiry', fieldName: 'Inquiry' },
    { label: 'Transactions', fieldName: 'Transactions' },
    { label: 'Account Maintenance', fieldName: 'AccountMaintenance' },
    { label: 'Forms', fieldName: 'Forms' },
    { label: 'Others', fieldName: 'Others' },
];

export default class YourLWC extends LightningElement {
    @track data = [];

    @wire(getObject)
    wiredData({ error, data }) {
        if (data) {
            // Transform the data structure to align with the datatable
            this.data = Object.keys(data).map(caseNumber => {
                const caseActionsMap = data[caseNumber];
                return {
                    CaseNumber: caseNumber,
                    CreatedDate: caseActionsMap['CreatedDate'][0], // Assuming the CreatedDate is the same for all actions
                    PlanID_Text__c: caseActionsMap['PlanID_Text__c'][0], // Assuming the PlanID_Text__c is the same for all actions
                    Inquiry: caseActionsMap['Inquiry'].join('\n'),
                    Transactions: caseActionsMap['Transactions'].join('\n'),
                    AccountMaintenance: caseActionsMap['AccountMaintenance'].join('\n'),
                    Forms: caseActionsMap['Forms'].join('\n'),
                    Others: caseActionsMap['Others'].join('\n'),
                };
            });
        }
    }
	}
