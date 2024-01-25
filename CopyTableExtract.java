import { LightningElement, wire } from 'lwc';
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
    @wire(MessageContext)
    messageContext;

    @track data = [];
    @track columns = columns;
    wiredRecords;

    @wire(getObject) wiredCases(value) {
        this.wiredRecords = value;
        const { data, error } = value;

        if (data) {
            let uniqueCaseNumbers = new Set();
            let uniquePlanIds = new Set();

            // Modify Case Number, Plan Id, and ensure unique Call_Type__c for each row
            let tempRecords = data.map(row => {
                if (!uniqueCaseNumbers.has(row.CaseNumber) && !uniquePlanIds.has(row.PlanID_Text__c)) {
                    uniqueCaseNumbers.add(row.CaseNumber);
                    uniquePlanIds.add(row.PlanID_Text__c);
                    return row;
                }
                // If duplicate Case Number or Plan Id, return a new object with blank Case Number, Plan Id, and Call_Type__c
                return { ...row, CaseNumber: null, PlanID_Text__c: null, Call_Type__c: null };
            });

            this.data = tempRecords;
            console.log("tempRecords!", tempRecords);
        }

        if (error) {
            console.log("error Occurred!", error);
        }
    }

    // Rest of your code remains unchanged
    // connectedCallback, sendMessage, etc.
}










import { LightningElement, wire } from 'lwc';
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
    @wire(MessageContext)
    messageContext;

    @track data = [];
    @track columns = columns;
    wiredRecords;

    @wire(getObject) wiredCases(value) {
        this.wiredRecords = value;
        const { data, error } = value;

        if (data) {
            let uniqueCaseNumbers = new Set();
            let uniquePlanIds = new Set();

            // Modify Case Number and Plan Id, keep other column values as usual
            let tempRecords = data.map(row => {
                if (!uniqueCaseNumbers.has(row.CaseNumber)) {
                    uniqueCaseNumbers.add(row.CaseNumber);
                    uniquePlanIds.add(row.PlanID_Text__c);
                    return row;
                }
                // If duplicate Case Number, return a new object with blank Case Number
                return { ...row, CaseNumber: null };
            });

            // Filter out records with duplicate Plan Ids
            tempRecords = tempRecords.map(row => {
                if (!uniquePlanIds.has(row.PlanID_Text__c)) {
                    uniquePlanIds.add(row.PlanID_Text__c);
                    return row;
                }
                // If duplicate Plan Id, return a new object with blank Plan Id
                return { ...row, PlanID_Text__c: null };
            });

            this.data = tempRecords;
            console.log("tempRecords!", tempRecords);
        }

        if (error) {
            console.log("error Occurred!", error);
        }
    }

    // Rest of your code remains unchanged
    // connectedCallback, sendMessage, etc.
}














import { LightningElement, wire } from 'lwc';
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
    hardcodedCaseNumber = 'ABC123';

    @wire(MessageContext)
    messageContext;

    @track data = [];
    @track columns = columns;
    wiredRecords;

    @wire(getObject) wiredCases(value) {
        this.wiredRecords = value;
        const { data, error } = value;

        if (data) {
            let uniquePlanIds = new Set();

            // Modify only Plan Id and keep other column values as usual
            let tempRecords = data.map(row => {
                if (!uniquePlanIds.has(row.PlanID_Text__c)) {
                    uniquePlanIds.add(row.PlanID_Text__c);
                    row.CaseNumber = this.hardcodedCaseNumber;
                    return row;
                }
                // If duplicate Plan Id, return a new object with blank Plan Id
                return { ...row, PlanID_Text__c: null };
            });

            this.data = tempRecords;
            console.log("tempRecords!", tempRecords);
        }

        if (error) {
            console.log("error Occurred!", error);
        }
    }

    // Rest of your code remains unchanged
    // connectedCallback, sendMessage, etc.
}







import { LightningElement, wire } from 'lwc';
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
    hardcodedCaseNumber = 'ABC123';

    @wire(MessageContext)
    messageContext;

    @track data = [];
    @track columns = columns;
    wiredRecords;

    @wire(getObject) wiredCases(value) {
        this.wiredRecords = value;
        const { data, error } = value;

        if (data) {
            let uniquePlanIds = new Set();

            // Filter out records with duplicate Plan Ids
            let tempRecords = data.filter(row => {
                if (!uniquePlanIds.has(row.PlanID_Text__c)) {
                    uniquePlanIds.add(row.PlanID_Text__c);
                    row.CaseNumber = this.hardcodedCaseNumber;
                    return true;
                }
                return false;
            });

            this.data = tempRecords;
            console.log("tempRecords!", tempRecords);
        }

        if (error) {
            console.log("error Occurred!", error);
        }
    }

    // Rest of your code remains unchanged
    // connectedCallback, sendMessage, etc.
}






import { LightningElement, wire } from 'lwc';
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
    hardcodedCaseNumber = 'ABC123';

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

            // Use a Set to store unique Plan Ids
            let uniquePlanIds = new Set();

            // Iterate through records and set CaseNumber only once
            tempRecords.forEach((row, index) => {
                if (!uniquePlanIds.has(row.PlanID_Text__c)) {
                    row.CaseNumber = this.hardcodedCaseNumber;
                    uniquePlanIds.add(row.PlanID_Text__c);
                } else {
                    // Clear CaseNumber for subsequent records
                    tempRecords[index].CaseNumber = null;
                }
            });

            this.data = tempRecords;
            console.log("tempRecords!", tempRecords);
        }

        if (error) {
            console.log("error Occurred!", error);
        }
    }

    // Rest of your code remains unchanged
    // connectedCallback, sendMessage, etc.
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
    @track passedValue;
    dnisNumber;
    source;
    CTIP;
    clientSSN;
    ctiVRUApp;
    ctiEDU;
    AuthenticatedFlag;
    caseOrigin;

    // Hardcoded CaseNumber
    hardcodedCaseNumber = 'ABC123';

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

            // Filter records for the specified CaseNumber
            tempRecords = tempRecords.filter((row) => row.CaseNumber === this.hardcodedCaseNumber);

            // Create a Set to store unique PlanID_Text__c values
            const uniquePlanIDs = new Set();

            // Iterate through records and add unique PlanID_Text__c values to the Set
            tempRecords.forEach((row) => {
                uniquePlanIDs.add(row.PlanID_Text__c);
            });

            // Convert Set to array and set it as the unique PlanID_Text__c values
            tempRecords.forEach((row) => {
                row.PlanID_Text__c = Array.from(uniquePlanIDs);
            });

            this.data = tempRecords;
            console.log("tempRecords!", tempRecords);
        }

        if (error) {
            console.log("error Occurred!", error);
        }
    }

    // Rest of your code remains unchanged
    // connectedCallback, sendMessage, etc.
}








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

            // Group data by CaseNumber and then by PlanID_Text__c
            const groupedData = this.groupBy(tempRecords, 'CaseNumber', 'PlanID_Text__c');

            // Transform grouped data to match datatable structure
            this.data = Object.keys(groupedData).map(caseNumber => {
                const planIdGroups = groupedData[caseNumber];

                // Nested map to group by PlanID_Text__c
                const transformedData = Object.keys(planIdGroups).map(planId => {
                    const caseActions = planIdGroups[planId];
                    return {
                        CaseNumber: caseNumber,
                        CreatedDate: caseActions[0].CreatedDate, // Assuming CreatedDate is the same for all actions
                        PlanID_Text__c: planId,
                        Inquiry: caseActions.filter(action => action.Call_Type__c === 'Inquiry'),
                        Transactions: caseActions.filter(action => action.Call_Type__c === 'Transactions'),
                        AccountMaintenance: caseActions.filter(action => action.Call_Type__c === 'Account Maintenance'),
                        Forms: caseActions.filter(action => action.Call_Type__c === 'Forms'),
                        Others: caseActions.filter(action => action.Call_Type__c === 'Others'),
                    };
                });

                return transformedData;
            });

            console.log("Grouped and Transformed Data:", this.data);
        }

        if (error) {
            console.log("Error Occurred!", error);
        }
    }

    // Existing methods (connectedCallback, sendMessage, etc.) remain unchanged

    // Custom grouping function
    groupBy(array, key1, key2) {
        return array.reduce((result, item) => {
            const groupKey1 = item[key1];
            const groupKey2 = item[key2];
            if (!result[groupKey1]) {
                result[groupKey1] = {};
            }
            if (!result[groupKey1][groupKey2]) {
                result[groupKey1][groupKey2] = [];
            }
            result[groupKey1][groupKey2].push(item);
            return result;
        }, {});
    }
                            }

