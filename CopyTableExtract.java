import { LightningElement, api, track, wire } from 'lwc';
import { publish, MessageContext } from 'lightning/messageService';
import EXAMPLE_MESSAGE_CHANNEL from '@salesforce/messageChannel/ExampleMessageChannel__c';
import getObject from '@salesforce/apex/CaseRelatedListApex.getObject';

const columns = [
    { label: 'Case Number', fieldName: 'CaseNumber' },
    { label: 'Date', fieldName: 'CreatedDate' },
    { label: 'Plan Id', fieldName: 'PlanID_Text__c' },
    { label: 'Inquiry', fieldName: 'Call_Type__c' },
    { label: 'Transactions', fieldName: 'Call_Type__c' },
    { label: 'Account Maintenance', fieldName: 'Call_Type__c' },
    { label: 'Forms', fieldName: 'Call_Type__c' },
    { label: 'Others', fieldName: 'Call_Type__c' },
];

export default class CaseHistoryLWC extends LightningElement {
    @track data = [];
    @track columns = columns;
    wiredRecords;

    @wire(getObject) wiredCases(value) {
        this.wiredRecords = value;
        const { data, error } = value;
        if (data) {
            let tempRecords = JSON.parse(JSON.stringify(data));
            let uniqueCallTypesMap = new Map();

            tempRecords = tempRecords.map(row => {
                const callType = row.Call_Type__c;
                if (!uniqueCallTypesMap.has(callType)) {
                    uniqueCallTypesMap.set(callType, true);

                    return {
                        ...row,
                        CaseNumber: row.Case__r.CaseNumber,
                        CreatedDate: row.Case__r.CreatedDate
                    };
                }

                return null;
            }).filter(row => row !== null);

            this.data = tempRecords;
            console.log("tempRecords!", tempRecords);
        }

        if (error) {
            console.log("error Occurred!", error);
        }
    }
}












import { LightningElement,api,track,wire } from 'lwc';
import { publish, MessageContext } from 'lightning/messageService';
import EXAMPLE_MESSAGE_CHANNEL from '@salesforce/messageChannel/ExampleMessageChannel__c';
import getObject from '@salesforce/apex/CaseRelatedListApex.getObject';


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

export default class CaseHistoryLWC extends LightningElement {
	
	
	
	@track data = [];
@track columns = columns;
wiredRecords;
	@wire(getObject) wiredCases(value){
		this.wiredRecords=value;
			const {data, error}= value;
			if(data){
					
				let tempRecords= JSON.parse(JSON.stringify(data));
				
					let uniqueCaseNumbers = new Set();
		let uniquePlanIds = new Set();
		let uniqueCallTypesMap = new Map();
		
		tempRecords = tempRecords.map(row => {
			
			if (!uniquePlanIds.has(row.PlanID_Text__c)) {
				uniquePlanIds.add(row.PlanID_Text__c);
				
				
				return {...row,CaseNumber:row.Case__r.CaseNumber , CreatedDate:row.Case__r.CreatedDate};
			}
			
			return { ...row,PlanID_Text__c: null};
		});

		
					
					this.data= tempRecords;
						console.log("tempRecords!" , tempRecords);
						
						
			}
			
			if(error){
					console.log("error Occurred!" , error);
			}
	}


	

	

}






