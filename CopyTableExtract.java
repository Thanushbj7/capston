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

const uniqueColumnsSet = new Set();

const filteredColumns = columns.filter(column => {
    if (!uniqueColumnsSet.has(column.fieldName)) {
        uniqueColumnsSet.add(column.fieldName);
        return true;
    }
    return false;
});

export default class CaseHistoryLWC extends LightningElement {
    // ... rest of your component code
    @track columns = filteredColumns;
    // ... rest of your component code
}








@wire(getObject) wiredCases(value) {
    // existing code...

    if (data) {
        let tempRecords = JSON.parse(JSON.stringify(data));

        tempRecords = tempRecords.map(row => {
            let uniqueValues = new Set();
            Object.keys(row).forEach(key => {
                if (!uniqueValues.has(row[key])) {
                    uniqueValues.add(row[key]);
                } else {
                    row[key] = null; // Set duplicate values to null
                }
            });
            return row;
        });

        this.data = tempRecords;
        console.log("tempRecords!", tempRecords);
    }

    // existing code...
}








@wire(getObject) wiredCases(value) {
    // existing code...

    if (data) {
        let tempRecords = JSON.parse(JSON.stringify(data));

        let uniqueRows = new Set();

        tempRecords = tempRecords.filter(row => {
            const rowKey = Object.values(row).join('-'); // Concatenate all values into a unique key
            if (!uniqueRows.has(rowKey)) {
                uniqueRows.add(rowKey);
                return true; // Include unique rows
            }
            return false; // Skip duplicate rows
        });

        this.data = tempRecords;
        console.log("tempRecords!", tempRecords);
    }

    // existing code...
}











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

            let uniqueCaseNumbers = new Set();
            let uniquePlanIds = new Set();
            let uniqueCallTypesMap = new Map();

            tempRecords = tempRecords.map(row => {


                if (!uniquePlanIds.has(row.PlanID_Text__c)) {
                    uniquePlanIds.add(row.PlanID_Text__c);


                    return {...row, CaseNumber: row.Case__r.CaseNumber, CreatedDate: row.Case__r.CreatedDate };
                    //console.log(tempRecords);
                }

                return {...row, PlanID_Text__c: null };
            });



            this.data = tempRecords;
            console.log("tempRecords!", tempRecords);


        }

        if (error) {
            console.log("error Occurred!", error);
        }
    }






}
