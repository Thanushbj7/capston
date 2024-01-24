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

export default class CaseHistoryLWC extends LightningElement {
    @track passedValue;
    dnisNumber;
    source;
    CTIP;
    clientSSN;
    ctiVRUApp;
    ctiEDU;
    AuthenticatedFlag;
    caseOrigin;

    @wire(MessageContext)
    messageContext;

    @track data = [];
    @track columns = columns;
    wiredRecords;

    @wire(getObject) wiredCases(value) {
        this.wiredRecords = value;
        const { data, error } = value;

        if (data) {
            let tempRecords = JSON.parse(JSON.stringify(data));

            // Group data by CaseNumber
            const groupedData = this.groupBy(tempRecords, 'CaseNumber');

            // Transform grouped data to match datatable structure
            this.data = Object.keys(groupedData).map(caseNumber => {
                const caseActions = groupedData[caseNumber];
                return {
                    CaseNumber: caseNumber,
                    CreatedDate: caseActions[0].CreatedDate, // Assuming CreatedDate is the same for all actions
                    PlanID_Text__c: caseActions[0].PlanID_Text__c, // Assuming PlanID_Text__c is the same for all actions
                    Inquiry: caseActions.filter(action => action.Call_Type__c === 'Inquiry'),
                    Transactions: caseActions.filter(action => action.Call_Type__c === 'Transactions'),
                    AccountMaintenance: caseActions.filter(action => action.Call_Type__c === 'Account Maintenance'),
                    Forms: caseActions.filter(action => action.Call_Type__c === 'Forms'),
                    Others: caseActions.filter(action => action.Call_Type__c === 'Others'),
                };
            });
            console.log("Grouped and Transformed Data:", this.data);
        }

        if (error) {
            console.log("Error Occurred!", error);
        }
    }

    // Existing methods (connectedCallback, sendMessage, etc.) remain unchanged

    // Custom grouping function
    groupBy(array, key) {
        return array.reduce((result, item) => {
            const groupKey = item[key];
            if (!result[groupKey]) {
                result[groupKey] = [];
            }
            result[groupKey].push(item);
            return result;
        }, {});
    }
  }
