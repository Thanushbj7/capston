 [Cannot read properties of undefined (reading 'Id')]



// ... (your existing code)

tempRecords = tempRecords.map(row => {
    if (!uniquePlanIds.has(row.PlanID_Text__c)) {
        uniquePlanIds.add(row.PlanID_Text__c);

        let Inquiry = false;
        let Transaction = false;

        switch (row.Call_Type__c) {
            case 'enquiry':
                Inquiry = true;
                break;
            case 'transaction':
                Transaction = true;
                break;
            // Add more cases as needed for other values

            // Default case handles other scenarios
            default:
                break;
        }

        return {
            ...row,
            CaseNumber: row.Case__r.CaseNumber,
            CreatedDate: row.Case__r.CreatedDate,
            Inquiry,
            Transaction,
            // Add more conditions as needed for other values
        };
    }

    return {
        ...row,
        PlanID_Text__c: null,
        Inquiry: false, // Set default values if needed
        Transaction: false, // Set default values if needed
        // Add more conditions as needed for other values
    };
});

// ... (rest of your existing code)










// ... (your existing code)

tempRecords = tempRecords.map(row => {
    if (!uniquePlanIds.has(row.PlanID_Text__c)) {
        uniquePlanIds.add(row.PlanID_Text__c);

        if (row.Call_Type__c === 'enquiry') {
            return {
                ...row,
                CaseNumber: row.Case__r.CaseNumber,
                CreatedDate: row.Case__r.CreatedDate,
                Inquiry: true,
                Transaction: false,
                // Add more conditions as needed for other values
            };
        } else if (row.Call_Type__c === 'transaction') {
            return {
                ...row,
                CaseNumber: row.Case__r.CaseNumber,
                CreatedDate: row.Case__r.CreatedDate,
                Inquiry: false,
                Transaction: true,
                // Add more conditions as needed for other values
            };
        } else {
            // Handle other cases here if needed
            return {
                ...row,
                CaseNumber: row.Case__r.CaseNumber,
                CreatedDate: row.Case__r.CreatedDate,
                Inquiry: false,
                Transaction: false,
                // Add more conditions as needed for other values
            };
        }
    }

    return {
        ...row,
        PlanID_Text__c: null,
        Inquiry: false, // Set default values if needed
        Transaction: false, // Set default values if needed
        // Add more conditions as needed for other values
    };
});

// ... (rest of your existing code)











// ... (your existing code)

tempRecords = tempRecords.map(row => {
    if (!uniquePlanIds.has(row.PlanID_Text__c)) {
        uniquePlanIds.add(row.PlanID_Text__c);

        return {
            ...row,
            CaseNumber: row.Case__r.CaseNumber,
            CreatedDate: row.Case__r.CreatedDate,
            Inquiry: row.Call_Type__c === 'enquiry',
            Transaction: row.Call_Type__c === 'transaction',
            // Add more conditions as needed for other values
        };
    }

    return {
        ...row,
        PlanID_Text__c: null,
        Inquiry: false, // Set default values if needed
        Transaction: false, // Set default values if needed
        // Add more conditions as needed for other values
    };
});

// ... (rest of your existing code)










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














<template>
    <div class="slds-box slds-theme_default" style="height: 300px;">
        <header class="slds-box_header slds-p-around_small slds-theme_default">
            <h2 class="slds-text-heading_medium">
                <lightning-icon icon-name="action:new_note" title="Case History" size="medium"></lightning-icon>
            <b>Case Historys</b>
        </h2>
    </header>

    <div class="slds-scrollable_x">
        <table class="slds-table slds-table_bordered slds-table_cell-buffer">
            <thead>
                <tr class="slds-text-title_caps">
                    <th>Case CaseNumber</th>
                    <th>Date</th>
                    <th>Plan Id</th>
                    <th>Inquiry</th>
                    <th>Transactions</th>
                    <th>Account Maintenance</th>
                    <th>Forms</th>
                    <th>Others</th>
                </tr>
            </thead>
            <tbody>
                
                <template for:each={data} for:item="record" for:index="index">
                    <tr key={record.Id}>
                        <td>{record.CaseNumber}</td>
                        <td>{record.CreatedDate}</td>
                        <td>{record.PlanID_Text__c}</td>
                        <td>{record.Call_Type__c}</td>
                        <td>{record.Call_Type__c}</td>
                        <td>{record.Call_Type__c}</td>
                        <td>{record.Call_Type__c}</td>
                        <td>{record.Call_Type__c}</td>
                    </tr>
                </template>
</tbody>
</table>
</div>
</div>
</template>
