// Assuming you have a data array with objects containing 'PlanID_Text__c' and 'Call_Type__c' properties
const dataArray = [
  { PlanID_Text__c: 'SomeValue1', Call_Type__c: 'Inquiry' },
  { PlanID_Text__c: 'SomeValue2', Call_Type__c: 'Transaction' },
  { PlanID_Text__c: 'SomeValue3', Call_Type__c: 'Account Maintenance' },
  { PlanID_Text__c: 'SomeValue4', Call_Type__c: 'Forms' },
  { PlanID_Text__c: 'SomeValue5', Call_Type__c: 'Others' },
];

// Initialize a variable to store the modified data
let modifiedData = [];

// Specify the value you want to check in the planId
const desiredPlanIdValue = 'SomeValue2';

// Loop through the data array and modify the call_type based on planId
for (const row of dataArray) {
  const { PlanID_Text__c, Call_Type__c } = row;

  // Check if planId contains the desired value
  if (PlanID_Text__c && PlanID_Text__c.includes(desiredPlanIdValue)) {
    // Initialize the call_type value based on your condition
    row.Call_Type__c = 'NewCallTypeValue';
  }

  // Add the modified row to the array
  modifiedData.push(row);
}

// Log the modified data
console.log('Modified Data:', modifiedData);











// Assuming you have a data array with objects containing 'Call_Activity__c' and 'Call_Type__c' properties
const dataArray = [
  { Call_Activity__c: 'Inquiry', Call_Type__c: 'SomeValue1' },
  { Call_Activity__c: 'Transaction', Call_Type__c: 'SomeValue2' },
  { Call_Activity__c: 'Account Maintenance', Call_Type__c: 'SomeValue3' },
  { Call_Activity__c: 'Forms', Call_Type__c: 'SomeValue4' },
  { Call_Activity__c: 'Others', Call_Type__c: 'SomeValue5' },
];

// Initialize an object to store call_type values for each activity
const callTypeValues = {};

// Function to collect call_type values for each activity
function collectCallTypeValues(dataArray) {
  for (const row of dataArray) {
    const { Call_Activity__c, Call_Type__c } = row;

    if (Call_Activity__c && Call_Type__c) {
      if (!callTypeValues[Call_Activity__c]) {
        callTypeValues[Call_Activity__c] = [];
      }
      
      callTypeValues[Call_Activity__c].push(Call_Type__c);
    }
  }
}

// Call the function with your data array
collectCallTypeValues(dataArray);

// Log the collected call_type values
console.log('Call Type Values:', callTypeValues);












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








// Assuming you have an array of objects named val

let copiedarr = [];

for (let i = 0; i < val.length; i++) {
    let groupedPlanIds = groupBy(val[i], 'planId');
    let planVal = Object.values(groupedPlanIds);

    for (let j = 0; j < planVal.length; j++) {
        let uniquePlanIds = new Set();
        let cak = planVal[j].map(row => {
            if (!uniquePlanIds.has(row.planId)) {
                uniquePlanIds.add(row.planId);
                return {
                    caseNumber: row.caseNumber,
                    createdDate: row.createdDate,
                    planId: row.planId,
                    callType: row.callType, // Assuming there is a callType field in your data
                };
            }
            return {
                caseNumber: null,
                createdDate: null,
                planId: null,
                callType: null,
            };
        });
        copiedarr.push(cak);
    }
}

const flattenedArr = copiedarr.flat(1);
console.log("flattenedArr--", flattenedArr);
// Now flattenedArr contains organized data with callType values in the same row










import { LightningElement, api, track, wire } from 'lwc';
import { publish, MessageContext } from 'lightning/messageService';
import EXAMPLE_MESSAGE_CHANNEL from '@salesforce/messageChannel/ExampleMessageChannel__c';
import fetchWrapperCases from '@salesforce/apex/CaseRelatedListApex.fetchWrapperCases';

const columns = [
    { label: 'Case Number', fieldName: 'caseNumber' },
    { label: 'Date', fieldName: 'createdDate' },
    { label: 'Plan Id', fieldName: 'planId' },
    { label: 'Inquiry', fieldName: 'callTypeInquiry', columnKey: 'inq' },
    { label: 'Transactions', fieldName: 'callTypeTransaction', columnKey: 'tra' },
    { label: 'Account Maintenance', fieldName: 'callTypeAccountMaintenance', columnKey: 'accM' },
    { label: 'Forms', fieldName: 'callTypeForms', columnKey: 'for' },
    { label: 'Others', fieldName: 'callTypeOthers', columnKey: 'oth' },
];

export default class CaseHistoryLWC extends LightningElement {
    @track data = [];
    @track columns = columns;

    wiredRecords;


    connectedCallback() {
            // Parse the URL and get the 'passedValue' parameter
            const urlParams = new URLSearchParams(window.location.search);
            this.clientSSN = urlParams.get('clientSSN');
            console.log('ssn should appear here' + this.clientSSN);
            console.log('inside connected callback');
        }
        //{SSN:this.clientSSN}

    @wire(fetchWrapperCases) wiredCases(value) {
        //console.log("Columns stringified 1st  --" , this.columns);
        this.wiredRecords = value;
        const { data, error } = value;
        if (data) {

            let tempRecords = JSON.parse(JSON.stringify(data));
            let uniqueCaseNumbers = new Set();

            let uniqueCallTypesMap = new Map();



            /*	tempRecords = tempRecords.map(row => {
								if (!uniquePlanIds.has(row.planId)) {
										uniquePlanIds.add(row.planId);

										return {...row , caseNumber:row.caseNumber , createdDate:row.createdDate };
               //
										//console.log("temprecords inside -", tempRecords);
								}		return { ...row,planId: null};
						});*/
            /*	tempRecords = tempRecords.map(row => {	
						if (!uniqueCaseNumbers.has(row.caseNumber)) {
										uniqueCaseNumbers.add(row.caseNumber);

										return {...row};
               //caseNumber:row.caseNumber , createdDate:row.createdDate
										//console.log("temprecords inside -", tempRecords);
								}		return { ...row,};//caseNumber: null
						});
						console.log("uniqueCaseNumbers -", uniqueCaseNumbers);

						for(var i of uniqueCaseNumbers) {
						tempRecords = tempRecords.map(row => {
							if(row.caseNumber === i){
								if (!uniquePlanIds.has(row.planId)) {
										uniquePlanIds.add(row.planId);
										return {...row , caseNumber:row.caseNumber , createdDate:row.createdDate };
               //
										//console.log("temprecords inside -", tempRecords);
								}		return { ...row,planId: null};
						}
						});
				}*/

            function groupBy(objectArray, property) {
                return objectArray.reduce(function(acc, obj) {
                    var key = obj[property];
                    if (!acc[key]) {
                        acc[key] = [];
                    }
                    acc[key].push(obj);
                    return acc;
                }, {});
            }
            var groupedPlanIds;
            var groupedCaseNumbers = groupBy(tempRecords, 'caseNumber');
            let final = [];
            for (let i in groupedCaseNumbers) {
                //	console.log("groupedCaseNumbers!" , groupedCaseNumbers[i]);


                //	console.log("groupedCaseNumbers[i]--" , groupedCaseNumbers[i]);
            }

            //		console.log("final--" , final);
            let val = Object.values(groupedCaseNumbers);
            console.log("Object.values--", val);
            console.log("groupedCaseNumbers!", groupedCaseNumbers);
            let cak = [];
            let copiedarr = [];
            for (let i = 0; i < val.length; i++) {

                //	console.log("Object.values for palnId--" , val[i]);
                groupedPlanIds = groupBy(val[i], 'planId')
                let planVal = Object.values(groupedPlanIds)
                console.log("Object.values for grouped PlanIds--", planVal);

                for (let j = 0; j < planVal.length; j++) {
                    let uniquePlanIds = new Set();
                    console.log("planVal[j]--", planVal[j]);
                    cak = planVal[j].map(row => {
                        if (!uniquePlanIds.has(row.planId)) {
                            uniquePlanIds.add(row.planId);
                            return {...row, caseNumber: row.caseNumber, createdDate: row.createdDate };
                            //		copiedarr.push(cak);
                        }
                        return {...row, caseNumber: null, createdDate: null, planId: null };
                        //copiedarr.push(cak);
                        //console.log("copiedarr--" ,copiedarr);

                    });
                    /*		for(let k=0; k<planVal[j].length ;k++){
									//console.log("planVal[j][k]--" ,planVal[j][k]);
												}*/

                    copiedarr.push(cak);

                }

            }
            console.log("copiedarr--", copiedarr);

            for (let i = 0; i < copiedarr.length; i++) {
                let can;
                console.log("copiedarr--", copiedarr[i]);
                for (let j = 0; copiedarr[i].length; j++) {
                    //	can=[...copiedarr[i][j]];
                }
                console.log("copiedarr[i][j]--", copiedarr[i]);
            }


            const flattenedArr = copiedarr.flat(1);
            console.log("flattenedArr--", flattenedArr);
            this.data = flattenedArr;
            console.log("tempRecords!", tempRecords);
        }

        if (error) {
            console.log("error Occurred!", error);
        }

    }

}















public class CaseWrapper {
    @AuraEnabled public string caseNumber;
    @AuraEnabled public datetime createdDate;
    @AuraEnabled public string planId;
    @AuraEnabled public string callTypes; // Concatenated string for all call types

    public CaseWrapper(Case_Actions__c ca) {
        this.caseNumber = ca.Case__r.CaseNumber;
        this.createdDate = ca.Case__r.CreatedDate;
        this.planId = ca.PlanID_Text__c;

        // Initialize the concatenated string
        this.callTypes = '';

        // Concatenate Call Type values
        if (ca.Call_Type__c != null) {
            if (ca.Call_Activity__c == 'Inquiry') {
                this.callTypes += 'Inquiry: ' + ca.Call_Type__c + ', ';
            } else if (ca.Call_Activity__c == 'Transaction') {
                this.callTypes += 'Transaction: ' + ca.Call_Type__c + ', ';
            } else if (ca.Call_Activity__c == 'Account Maintenance') {
                this.callTypes += 'Account Maintenance: ' + ca.Call_Type__c + ', ';
            } else if (ca.Call_Activity__c == 'Forms') {
                this.callTypes += 'Forms: ' + ca.Call_Type__c + ', ';
            } else if (ca.Call_Activity__c == 'Hand-Off Case' || ca.Call_Activity__c == 'NIGO Callback') {
                this.callTypes += ca.Call_Activity__c + ': ' + ca.Call_Type__c + ', ';
            }
        }

        // Remove trailing comma and space if the string is not empty
        if (!String.isEmpty(this.callTypes)) {
            this.callTypes = this.callTypes.substring(0, this.callTypes.length() - 2);
        }
    }
}








public class CaseWrapper {
    @AuraEnabled public string caseNumber;
    @AuraEnabled public datetime createdDate;
    @AuraEnabled public string planId;
    @AuraEnabled public string callTypes; // Concatenated string for all call types

    public CaseWrapper(Case_Actions__c ca) {
        this.caseNumber = ca.Case__r.CaseNumber;
        this.createdDate = ca.Case__r.CreatedDate;
        this.planId = ca.PlanID_Text__c;

        // Initialize the concatenated string
        this.callTypes = '';

        // Concatenate Call Type values
        if (ca.Call_Activity__c == 'Inquiry' && ca.Call_Type__c != '') {
            this.callTypes += ca.Call_Type__c + ', ';
        }
        if (ca.Call_Activity__c == 'Transaction' && ca.Call_Type__c != '') {
            this.callTypes += ca.Call_Type__c + ', ';
        }
        // Add similar blocks for other conditions...

        // Remove trailing comma and space if the string is not empty
        if (!String.isEmpty(this.callTypes)) {
            this.callTypes = this.callTypes.substring(0, this.callTypes.length() - 2);
        }
    }
}











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



public class CaseHistoryLWC {
    
    @AuraEnabled(cacheable=true)
    public static List<CaseWrapper> fetchWrapperCases(){
        List<CaseWrapper> wrapperList = new List<CaseWrapper>();
        
        List<Case_Actions__c> caseList = [SELECT Case__r.CaseNumber, Case__r.CreatedDate, PlanID_Text__c, Call_Activity__c, Call_Type__c 
                                           FROM Case_Actions__c 
                                           WHERE (Case__r.Account.SSN__c = '010820241' AND PlanID_Text__c != '' AND Call_Type__c != null AND Call_Activity__c != null) 
                                           ORDER BY Case__r.CaseNumber];
        
        for (Case_Actions__c ca : caseList){
            wrapperList.add(new CaseWrapper(ca));
        }
        
        return wrapperList;
    }
    
    public class CaseWrapper {
        @AuraEnabled public String caseNumber;
        @AuraEnabled public Datetime createdDate;
        @AuraEnabled public String planId;
        @AuraEnabled public String callActivity;
        @AuraEnabled public String callType;

        public CaseWrapper(Case_Actions__c ca) {
            this.caseNumber = ca.Case__r.CaseNumber;
            this.createdDate = ca.Case__r.CreatedDate;
            this.planId = ca.PlanID_Text__c;
            this.callActivity = ca.Call_Activity__c;
            this.callType = ca.Call_Type__c;
        }
    }
}
