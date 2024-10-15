<template for:each={itemList} for:item="item" for:index="index">
    <tr class="slds-hint-parent" key={item.id}>
        <!-- Plan Id Combobox -->
        <td data-label="Plan Ids">
            <lightning-combobox 
                name="planId" 
                data-index={index}
                value={item.planId} 
                options={planIds} 
                onchange={handlePlanIdsChange}>
            </lightning-combobox>
            <div class="error">{planIdError}</div>
        </td>
        
        <!-- Call Activity Combobox -->
        <td data-label="Call Activities">
            <lightning-combobox 
                name="callActivity" 
                data-index={index}
                value={item.callActivity} 
                options={callActivitiesOptions} 
                onchange={handlecallActivitiesChange}>
            </lightning-combobox>
            <div class="error">{callActivityError}</div>
        </td>
        
        <!-- Call Type Combobox -->
        <td data-label="Call Types">
            <lightning-combobox 
                name="callType" 
                data-index={index}
                value={item.callType} 
                options={callTypesOptions} 
                onchange={handlecallTypesChange}>
            </lightning-combobox>
            <div class="error">{callTypeError}</div>
        </td>

        <!-- Add and Delete Icons -->
        <td class="slds-cell_action-mode" role="gridcell">
            <!-- Add Row Icon -->
            <lightning-icon 
                icon-name="action:new" 
                alternative-text="Add Row" 
                size="small" 
                title="Add Row" 
                onclick={addRow}>
            </lightning-icon>
            &nbsp; &nbsp;
            <!-- Delete Row Icon -->
            <lightning-icon 
                icon-name="action:delete" 
                alternative-text="Delete Row" 
                size="small" 
                title="Delete Row" 
                id={index} 
                onclick={removeRow}>
            </lightning-icon>
        </td>
    </tr>
</template>








import { LightningElement, track } from 'lwc';

export default class CaseTrackingComponent extends LightningElement {
    @track itemList = [
        { id: 1, planId: '', callActivity: '', callType: '' } // Initial row data
    ];

    // Handle adding a new row
    addRow(event) {
        // Generate a unique ID for the new row
        let newId = this.itemList.length + 1;
        
        // Push a new empty row object to itemList
        this.itemList = [...this.itemList, {
            id: newId,
            planId: '',
            callActivity: '',
            callType: ''
        }];
    }

    // Handle row removal
    removeRow(event) {
        const index = event.currentTarget.id;
        this.itemList = this.itemList.filter((_, idx) => idx != index);
    }

    // Handle planId changes
    handlePlanIdsChange(event) {
        const index = event.target.dataset.index;
        this.itemList[index].planId = event.detail.value;
    }

    // Handle callActivity changes
    handlecallActivitiesChange(event) {
        const index = event.target.dataset.index;
        this.itemList[index].callActivity = event.detail.value;
    }

    // Handle callType changes
    handlecallTypesChange(event) {
        const index = event.target.dataset.index;
        this.itemList[index].callType = event.detail.value;
    }
}










import { LightningElement, api, track, wire } from 'lwc';
// Import your Apex methods and other necessary modules

export default class CCCaseAction extends LightningElement {
    @track columns = columns;
    @track data = [];
    @track itemList = [
        {
            id: 0,
            planId: '',
            callActivity: '',
            callType: '',
            planIdError: '',
            callActivityError: '',
            callTypeError: '',
            callTypesOptions: []
        }
    ];
    keyIndex = 0;

    // Other variables and wire methods

    addRow() {
        ++this.keyIndex;
        const newItem = {
            id: this.keyIndex,
            planId: '',
            callActivity: '',
            callType: '',
            planIdError: '',
            callActivityError: '',
            callTypeError: '',
            callTypesOptions: []
        };
        this.itemList = [...this.itemList, newItem];
    }

    removeRow(event) {
        if (this.itemList.length > 1) {
            const itemId = parseInt(event.target.accessKey);
            this.itemList = this.itemList.filter(item => item.id !== itemId);
        }
    }

    handlePlanIdsChange(event) {
        const itemId = parseInt(event.target.dataset.id);
        const newValue = event.target.value;
        this.itemList = this.itemList.map(item => {
            if (item.id === itemId) {
                return { ...item, planId: newValue };
            }
            return item;
        });
    }

    handlecallActivitiesChange(event) {
        const itemId = parseInt(event.target.dataset.id);
        const newValue = event.target.value;

        let key = this.callTypesData.controllerValues[newValue];
        let callTypesOptions = this.callTypesData.values.filter(opt => opt.validFor.includes(key));

        this.itemList = this.itemList.map(item => {
            if (item.id === itemId) {
                return { 
                    ...item, 
                    callActivity: newValue,
                    callTypesOptions
                };
            }
            return item;
        });
    }

    handlecallTypesChange(event) {
        const itemId = parseInt(event.target.dataset.id);
        const newValue = event.target.value;
        this.itemList = this.itemList.map(item => {
            if (item.id === itemId) {
                return { ...item, callType: newValue };
            }
            return item;
        });
    }

    async onCreateCaseAction() {
        try {
            let isValid = true;
            // Validate each item
            this.itemList = this.itemList.map(item => {
                let planIdError = item.planId ? '' : 'Plan Id is required.';
                let callActivityError = item.callActivity ? '' : 'Call Activity is required.';
                let callTypeError = item.callType ? '' : 'Call Type is required.';
                if (planIdError || callActivityError || callTypeError) {
                    isValid = false;
                }
                return {
                    ...item,
                    planIdError,
                    callActivityError,
                    callTypeError
                };
            });

            if (isValid) {
                // Initialize data array
                this.data = [];

                for (const item of this.itemList) {
                    await this.createCaseActionForItem(item);
                }

                // Clear the form
                this.itemList = [
                    {
                        id: 0,
                        planId: '',
                        callActivity: '',
                        callType: '',
                        planIdError: '',
                        callActivityError: '',
                        callTypeError: '',
                        callTypesOptions: []
                    }
                ];
            }
        } catch (error) {
            console.error(error);
        }
    }

    async createCaseActionForItem(item) {
        const { planId, callActivity, callType } = item;
        const csId = await getCaseId({ clientId: this.recordId });
        let caseId;
        
        if (!csId) {
            const newCaseId = await createNewCase({ 
                clientId: this.recordId, 
                planId, 
                callActivity, 
                callType 
            });
            caseId = newCaseId;
        } else {
            caseId = csId;
        }

        // Create the case action
        const caseActionId = await createCaseActions({ 
            clientId: this.recordId, 
            caseId, 
            planId, 
            callActivity, 
            callType 
        });

        // Fetch and update the data for display
        const caseActionToBeDisplayed = await getRelatedCaseActions({ 
            caseId, 
            caseActionIdSet: [caseActionId] 
        });

        let arrFinal = caseActionToBeDisplayed.map(row => ({
            PlanID_Text__c: row.PlanID_Text__c,
            Call_Activity__c: row.Call_Activity__c,
            Call_Type__c: row.Call_Type__c
        }));
        this.data = [...this.data, ...arrFinal];
    }

    // Other methods and connectedCallback
		}













import { LightningElement, api, track, wire } from 'lwc';
import createCaseActions from '@salesforce/apex/lightningPopController.createCaseActions';
import createNewCase from '@salesforce/apex/lightningPopController.createNewCase';
import { ShowToastEvent } from "lightning/platformShowToastEvent";
import getPlans from '@salesforce/apex/lightningPopController.initializeAndLoadPlanData';
import getCurrentCase from '@salesforce/apex/lightningPopController.getCurrentCase';
import updateCase from '@salesforce/apex/lightningPopController.updateCase';
import getRelatedCaseActions from '@salesforce/apex/lightningPopController.getRelatedCaseActions';
import getCaseId from '@salesforce/apex/lightningPopController.getCaseId';
import profileid from '@salesforce/label/c.TestForTM';
import CALL_TYPE from '@salesforce/schema/Case_Actions__c.Call_Type__c';
import CALL_ACTIVITY from '@salesforce/schema/Case_Actions__c.Call_Activity__c';
import CASE_ACTION_OBJECT from '@salesforce/schema/Case_Actions__c';
import { getObjectInfo, getPicklistValues } from 'lightning/uiObjectInfoApi';
import { getObjectInfos } from "lightning/uiObjectInfoApi";
import { RefreshEvent } from 'lightning/refresh';
import PAAGTOCASEACTION_MESSAGE_CHANNELL from '@salesforce/messageChannel/paagToCaseAction__c';
import { MessageContext, subscribe } from 'lightning/messageService';
import CALLER_TYPE from '@salesforce/schema/Case.Caller_Type__c';
import CASE_ORIGIN from '@salesforce/schema/Case.Origin';
import CASE_OBJECT from '@salesforce/schema/Case';
import { refreshApex } from "@salesforce/apex";
import LightningConfirm from 'lightning/confirm';
const columns = [
    { label: 'Plan Id', fieldName: 'PlanID_Text__c' },
    { label: 'Call Activity', fieldName: 'Call_Activity__c' },
    { label: 'Call Type', fieldName: 'Call_Type__c' },
];


export default class CCCaseAction extends LightningElement {
    //filter=profileid;
    //	profileid;
    @track columns = columns;
    @track data = [];

    caseActionList = [];
    @track receivedMessage = '';
    planId;
    callActivity;
    callType;
    @track subscription = null;
    @track callTypeError = '';
    @track callActivityError = '';
    @track planIdError = '';
    @track commentError = '';
    callActivity = '';
    @track callerTpeError = '';
    @track caseOriginError = '';
    caseIds;
    @track callerTypeOptions;
    callerType = 'Participant';
    @track callActivities;
    @track planIds;
    @track callTypes;
    @track addOneRow = false;
    @track accountList = [];
    @track index = 0;
    caseNum;
    clientSSN = '';
    ctiVRUApp;
    callTypesData;;
    @track caseOriginOptions;
    caseOrigin = 'Phone';
    Id;
    caseId;
    @wire(MessageContext)
    messageContext;
    @api recordId;
    @track checkSubmit = false;

    keyIndex = 0;
    @track itemList = [{
        id: 0
    }];

    addRow() {
        ++this.keyIndex;
        var newItem = [{ id: this.keyIndex }];
        this.itemList = this.itemList.concat(newItem);
    }

    removeRow(event) {
        if (this.itemList.length >= 2) {
            this.itemList = this.itemList.filter(function(element) {
                return parseInt(element.id) !== parseInt(event.target.accessKey);
            });
        }
    }
    @wire(getObjectInfo, { objectApiName: CASE_ACTION_OBJECT })
    objectInfo;
    @wire(getPicklistValues, {
        // objectApiName: OPPORTUNITY_OBJECT,
        recordTypeId: '$objectInfo.data.defaultRecordTypeId',

        fieldApiName: CALL_TYPE
            //RESPONSE
    })
    callTypeInfo({ data, error }) {
        if (data) this.callTypesData = data;
        // console.log('this is call type', JSON.stringify(data));
        console.log('recordTypeId', this.recordTypeId);
        // console.log('this.callTypeOptions', JSON.stringify(this.callTypeData));
    }
    if (error) {
        console.log('error', error);
    }
    @wire(getPicklistValues, {
        // objectApiName: OPPORTUNITY_OBJECT,
        recordTypeId: '$objectInfo.data.defaultRecordTypeId',

        fieldApiName: CALL_ACTIVITY
            //RESPONSE
    })
    callActivityInfo({ data, error }) {
        if (data) this.callActivitiesOptions = data.values;
        // console.log('this is call Activity', JSON.stringify(data));
        console.log('recordTypeId', this.recordTypeId);
        // console.log('this.callTypeOptions', JSON.stringify(this.callTypeData));
    }
    if (error) {
        console.log('error', error);
    }
    handlePlanIdsChange(event) {
        this.planId = event.target.value;
    }
    handlecallActivitiesChange(event) {
        let key = this.callTypesData.controllerValues[event.target.value];
        console.log('Value of key is', this.key);
        this.callTypesOptions = this.callTypesData.values.filter(opt => opt.validFor.includes(key));
        // console.log('this is handleChange', JSON.stringify(this.responseReasonOptions));
        this.callActivity = event.target.value;
        //  alert(this.fields);
        // console.log('Hey', acc);
        // this.callActivity = event.target.value;

    }
    handlecallTypesChange(event) {
        this.callType = event.target.value;
    }
    @wire(getObjectInfo, { objectApiName: CASE_OBJECT })
    objectInfo1;
    @wire(getPicklistValues, {
        // objectApiName: OPPORTUNITY_OBJECT,
        recordTypeId: '$objectInfo1.data.defaultRecordTypeId',
        //recordTypeId: '$Campaignrecordtypeid',
        fieldApiName: CASE_ORIGIN
            //RESPONSE
    })
    caseOriginInfo({ data, error }) {
        if (data) this.caseOriginOptions = data.values;
        //  console.log('this is response Reason', JSON.stringify(data));
        // console.log('this.responseReasonOptions', JSON.stringify(this.responseReasonData));
    }



    @wire(getPicklistValues, {
        // objectApiName: OPPORTUNITY_OBJECT,
        recordTypeId: '$objectInfo1.data.defaultRecordTypeId',
        //recordTypeId: '$Campaignrecordtypeid',
        fieldApiName: CALLER_TYPE
            //RESPONSE
    })
    callerTypeInfo({ data, error }) {
        if (data) this.callerTypeOptions = data.values;
        // console.log('this is response', JSON.stringify(data));
    }
    handleCallerTypeChange(event) {
        this.callerType = event.target.value;
        this.checkSubmit = false;
        console.log('this.callerType', this.callerType);
        //event.target.value;
    }
    handleCaseOriginChange(event) {
        this.caseOrigin = event.target.value;
        this.checkSubmit = false;
        console.log('this.caseOrigin', this.caseOrigin);
        //event.target.value;
    }


    async onCreateCaseAction() {


        try {
            //Validate Plan Ids
            if (!this.planId) {
                this.planIdError = 'Plan Id is required.';
            } else {
                this.planIdError = '';
            }

            //Validate Plan Ids
            if (!this.callActivity) {
                this.callActivityError = 'Call Activity is required.';
            } else {
                this.callActivityError = '';
            }

            //Validate Plan Ids
            if (!this.callType) {
                this.callTypeError = 'Call Type is required.';
            } else {
                this.callTypeError = '';
            }

            if (!this.callTypeError && !this.callActivityError && !this.planIdError) {

                console.log('onCreateCaseAction recordid ', this.recordId)
                const csId = await getCaseId({ clientId: this.recordId });
                console.log('onCreateCaseAction csId', csId);

                if (csId === null) {

                    const NewcaseId = await createNewCase({ clientId: this.recordId, planId: this.planId, callActivity: this.callActivity, callType: this.callType })
                    console.log('onCreateCase csId', NewcaseId);
                    const res = await getCurrentCase({ Id: NewcaseId });
                    console.log('nCreateCas res', res);
                    if (res.length == 1) {
                        this.caseNum = res[0].CaseNumber;

                    }
                    const caseActionId = await createCaseActions({ clientId: this.recordId, caseId: NewcaseId, planId: this.planId, callActivity: this.callActivity, callType: this.callType })
                    const caseActionIdStr = JSON.stringify(caseActionId)
                    let caseActionIdSet = [];
                    caseActionIdSet.push(caseActionId);
                    const caseActionToBeDisplayed = await getRelatedCaseActions({ caseId: NewcaseId, caseActionId: caseActionIdSet })
                    let arrFinal = caseActionToBeDisplayed;
                    console.log("onCreateCaseAction array: ", arrFinal);
                    arrFinal = arrFinal.map(row => {
                        return { PlanID_Text__c: row.PlanID_Text__c, Call_Activity__c: row.Call_Activity__c, Call_Type__c: row.Call_Type__c };

                    });
                    this.data = arrFinal;
                    console.log("onCreateCaseAction final", arrFinal);
                } else {


                    console.log('onCreateCaseAction this.planId', this.planId);
                    console.log('onCreateCaseAction this.callActivity', this.callActivity);
                    console.log('onCreateCaseAction this.callType', this.callType);
                    const caseActionId = await createCaseActions({ clientId: this.recordId, caseId: csId, planId: this.planId, callActivity: this.callActivity, callType: this.callType })

                    console.log('onCreateCaseAction save : ' + caseActionId);
                    const caseActionIdStr = JSON.stringify(caseActionId)
                    console.log('onCreateCaseActionstr : ' + caseActionIdStr);

                    let caseActionIdSet = [];
                    caseActionIdSet.push(caseActionId);


                    const caseActionToBeDisplayed = await getRelatedCaseActions({ caseId: csId, caseActionId: caseActionIdSet })
                    console.log("onCreateCaseAction get ------: ", caseActionToBeDisplayed);


                    //  this.caseActionList.push(caseActionToBeDisplayed);
                    //  console.log("onCreateCaseAction list : ", this.caseActionList);

                    let arrFinal = caseActionToBeDisplayed;

                    console.log("onCreateCaseAction array: ", arrFinal);
                    arrFinal = arrFinal.map(row => {

                        return { PlanID_Text__c: row.PlanID_Text__c, Call_Activity__c: row.Call_Activity__c, Call_Type__c: row.Call_Type__c };

                    });
                    this.data = arrFinal;
                    console.log("onCreateCaseAction final", arrFinal);
                }
                this.callType = '';
                this.callActivity = '';
                this.planId = '';
            }
        } catch (error) {
            console.error(error);
        }


    }

    handleCommentsChange(event) {
        this.comment = event.target.value;
        this.checkSubmit = false;
    }


    async onCaseSave() {
        try {

            //Validate Comments
            if (!this.comment) {
                this.commentError = 'Comment is required.';
            } else {
                this.commentError = '';
            }
            if (!this.callerType) {
                this.callerTpeError = 'callerType is required.';
            } else {
                this.callerTpeError = '';
            }
            if (!this.caseOrigin) {
                this.caseOriginError = 'caseOrigin is required.';
            } else {
                this.caseOriginError = '';
            }

            if (!this.commentError && !this.callerTpeError && !this.caseOriginError) {
                this.checkSubmit = true;

                const csId = await getCaseId({ clientId: this.recordId });
                this.CseNumber = csId;
                console.log('Oncase save', csId);
                if (csId === null) {
                    const NewcaseId = await createNewCase({ clientId: this.recordId, planId: this.planId, callActivity: this.callActivity, callType: this.callType })

                    const rescase = await getCurrentCase({ Id: NewcaseId });
                    if (rescase.length == 1) {
                        this.caseNum = rescase[0].CaseNumber;
                        this.CseNumber = rescase[0].Id;
                        const res = await updateCase({ Id: this.CseNumber, Comment: this.comment, callerType: this.callerType, origin: this.caseOrigin });

                        const toast = await this.showToast(res);
                        this.dispatchEvent(new CustomEvent('recordChange'));
                    }

                    //}
                } else {
                    const res = await updateCase({ Id: this.CseNumber, Comment: this.comment, callerType: this.callerType, origin: this.caseOrigin });
                    console.log('inside onCaseSave this.callerType', this.callerType);
                    console.log('inside onCaseSave this.caseOrigin', this.caseOrigin);
                    console.log('inside onCaseSave tres', res);
                    //  this.callerType = 'Participant';
                    // this.caseOrigin = 'Phone';
                    // this.comment = '';
                    const toast = await this.showToast(res);
                    // const re = await this.reset();
                    this.dispatchEvent(new CustomEvent('recordChange'));
                }

            }
        } catch (e) {
            console.log("error ", e);
            this.showToast(e);
        }
    }
    reset() {

        this.refs.textArea.value = '';
    }

    showToast(comm) {
        const event = new ShowToastEvent({
            title: 'The below comment has been added to the Case',
            message: comm,
            variant: 'success',
            mode: 'dismissable'
        });
        this.dispatchEvent(event);
    }
    async handleSubscribe2() {
            console.log('handleSubscribe2 started');
            /*if (this.subscription) {
                 console.log('Inside IF');
                 return;
             }*/
            if (this.subscription) {
                return;
            }
            this.subscription = subscribe(
                this.messageContext,
                PAAGTOCASEACTION_MESSAGE_CHANNELL,
                (message) => this.displayMessage(message)
            );

            const csId = await getCaseId({ clientId: this.recordId });
            console.log(' case id', csId);
            //this.caseIds = csId;        
            let caseActionIdSet = [];
            //  caseActionIdSet.push(message.caseAction);

            const caseActionToBeDisplayed = await getRelatedCaseActions({ caseId: csId, caseActionId: caseActionIdSet })
            console.log("handle caseaction get ------: ", caseActionToBeDisplayed);

            let arrFinal = caseActionToBeDisplayed;

            console.log("handle CaseAction array: ", arrFinal);
            if (arrFinal != null) {
                arrFinal = arrFinal.map(row => {

                    return { PlanID_Text__c: row.PlanID_Text__c, Call_Activity__c: row.Call_Activity__c, Call_Type__c: row.Call_Type__c };

                });
                this.data = arrFinal;
                console.log("handle CaseAction final", arrFinal);
            }

        }
        /*unsubscribe() {
            unsubscribe(this.subscription);
            this.subscription = null;
        }*/
    async displayMessage(message) {
        // this.receivedMessage = message ? JSON.stringify(message, null, '\t') : 'no message payload';

        const csId = await getCaseId({ clientId: this.recordId });
        console.log('res1 after updating the case', csId);
        //this.caseIds = csId;        
        let caseActionIdSet = [];
        caseActionIdSet.push(message.caseAction);

        const caseActionToBeDisplayed = await getRelatedCaseActions({ caseId: csId, caseActionId: caseActionIdSet })
        console.log("onload caseaction get ------: ", caseActionToBeDisplayed);

        let arrFinal = caseActionToBeDisplayed;

        console.log("onload CaseAction array: ", arrFinal);
        if (arrFinal != null) {
            arrFinal = arrFinal.map(row => {

                return { PlanID_Text__c: row.PlanID_Text__c, Call_Activity__c: row.Call_Activity__c, Call_Type__c: row.Call_Type__c };

            });
            this.data = arrFinal;
            console.log("onload CaseAction final", arrFinal);
        }

    }

    async connectedCallback() {

        //calling the casaction from controller
        const csId = await getCaseId({ clientId: this.recordId });
        let caseActionIdSet = [];
        const caseActionToBeDisplayed = await getRelatedCaseActions({ caseId: csId, caseActionId: caseActionIdSet })
        console.log("Connected caseaction get ------: ", caseActionToBeDisplayed);
        let arrFinal = caseActionToBeDisplayed;
        console.log("connected CaseAction array: ", arrFinal);
        if (arrFinal != null) {
            arrFinal = arrFinal.map(row => {
                return { PlanID_Text__c: row.PlanID_Text__c, Call_Activity__c: row.Call_Activity__c, Call_Type__c: row.Call_Type__c };

            });
            this.data = arrFinal;
            console.log("connected CaseAction final", arrFinal);
        }

        //End
        this.handleSubscribe2();

        // Parse the URL and get the 'passedValue' parameter
        console.log('checking profileid  ', profileid);

        var attached = '[' + profileid + ']';
        console.log('Attached profileid ', attached);
        console.log("Columns stringified 1st  OOOOO--in connectedcallback csalwc", this.recordId);
        try {
            const urlParams = new URLSearchParams(window.location.search);
            this.passedValue = urlParams.get('passedValue') || 'No value passed';

            this.clientSSN = urlParams.get('clientSSN');
            this.ctiVRUApp = urlParams.get('ctiVRUApp');
            this.Id = urlParams.get('Id');

            const res1 = await getCaseId({ clientId: this.recordId });
            console.log('res1', res1);
            const res = await getCurrentCase({ Id: res1 });
            console.log('res', res);
            if (res.length == 1) {
                this.caseNum = res[0].CaseNumber;
            }
            // this.callerType = res[0].Caller_Type__c;
            //this.caseOrigin =   res[0].Origin;


            const result = await getPlans({ clientId: this.recordId });
            console.log('plan result now', result);

            if (result.length == 2) {
                console.log('Array size --', result.length);
                this.planId = result[0].planId;
            }
            console.log('planid in caseaction', this.planId)



            this.planIds = result.map(plan => {
                return {
                    label: plan.planId,
                    value: plan.planId //JSON.stringify(plan)
                };
            });
            this.planIds.reverse()
            console.log('planIds in caseaction', this.planIds)
        } catch (e) {
            console.log("error  ", e);

        }


    }



}











<template>
		  

	<!--	<lightning-card title="Current Case/Call Tracking" icon-name="standard:case" >-->
		<lightning-card icon-name="standard:case" >
				<h1 slot="title" class="no-wrap">Current Case : {caseNum}</h1>
		<!--		<div  class="slds-grid slds-border_top "> -->
						
					
						<div class="slds-p-horizontal_xxx-small">

								
					<!--				<div>	<h2 slot="title" class="" style="background-color:#F0F0F0;">
												<font size="3">	<strong class="customm-class">{caseNum}</strong>
												</font>	</h2>
								</div>-->
								<lightning-card  >
										<div>	<h2 slot="title" class="" style="background-color:#F0F0F0;">
												<font size="3">	<strong class="customm-class">Case Information </strong>
												</font>	</h2>

										</div>
										<div class="demo-only demo-only--sizing slds-grid slds-wrap slds-m-top_xx-small">
												<div class="slds-size_1-of-2">
														<div class="slds-m-horizontal_x-small " >
															<lightning-combobox required="true" onchange={handleCallerTypeChange}  name="CallerType" label="Caller Type" value={callerType} options={callerTypeOptions} ></lightning-combobox>    
															<div class="error">{callerTpeError}</div>
																<!--<lightning-input label="Caller Type" value={callerType}> </lightning-input>-->
														</div>
												</div>
												<div class="slds-size_1-of-2">
														<div class="slds-m-horizontal_x-small">
															<lightning-combobox required="true" onchange={handleCaseOriginChange}  name="CaseOrigin" label="Case Origin" value={caseOrigin}  options={caseOriginOptions} ></lightning-combobox>    
															<div class="error">{caseOriginError}</div>
															<!--<lightning-input label="Case Origin" value={caseOrigin}> </lightning-input>-->
														</div>
												</div>
										</div>

										<div class="slds-m-horizontal_x-small">
												<lightning-textarea lwc:ref="textArea" required="true" name="input1" label="Comments" onchange={handleCommentsChange}  value={comment} ></lightning-textarea>
												<div class="error">{commentError}</div>
										</div>


										<div class="slds-m-top_small " >
												<lightning-button  variant="brand" label="Save" title="Save" onclick={onCaseSave} disabled={checkSubmit} class="slds-m-horizontal_x-small"></lightning-button>
										</div>
								</lightning-card>

								<lightning-card >
										<div>	<h2 slot="title" class="slds-m-top_medium" style="background-color:#F0F0F0;">
												<font size="3">	<strong class="customm-class">Action Information </strong>
												</font>	</h2>

										</div>
										
										<div class="demo-only demo-only--sizing slds-grid slds-wrap slds-m-top_xx-small"> 
											<table class="slds-table slds-table_cell-buffer slds-table_bordered" aria-labelledby="element-with-table-label other-element-with-table-label">
												<thead>
												  <tr class="slds-line-height_reset">
													<th class="" scope="col">
														<div class="slds-truncate" title="Plan Ids">Plan Ids</div>
													</th>
													<th class="" scope="col">
														<div class="slds-truncate" title="Call Activities">Call Activities</div>
													  </th>
													  <th class="" scope="col">
														<div class="slds-truncate" title="Call Types">Call Types</div>
													
													  </th>
													</tr>
													</thead>
													  <tbody>
														
														<template for:each={itemList} for:item="item" for:index="index">
															<tr class="slds-hint-parent" key={item.id}>
															
																
															<td data-label="Plan Ids">
												
														
																<lightning-combobox name="planId"  value={planId} options={planIds} 
																										onchange={handlePlanIdsChange} ></lightning-combobox>
																<div class="error">{planIdError}</div>
														
												
											</td>
											<td data-label="Call Activities">
												
																<lightning-combobox name="callActivity"  value={callActivity} options={callActivitiesOptions}
																										onchange={handlecallActivitiesChange} ></lightning-combobox>
																<div class="error">{callActivityError}</div>
														
											</td>
											<td data-label="Call Types">
												
																<lightning-combobox name="callType"  value={callType} options={callTypesOptions}
																										onchange={handlecallTypesChange} ></lightning-combobox>
																<div class="error">{callTypeError}</div>

														
											</td>
										
<td class="slds-cell_action-mode" role="gridcell">
	<lightning-icon icon-name="action:new" access-key={item.id} id={index}
	alternative-text="Add Row" size="small" title="Add Row" onclick={addRow}>
</lightning-icon>
&nbsp; &nbsp;
<lightning-icon icon-name="action:delete" access-key={item.id} id={index}
	alternative-text="Delete Row" size="small" title="Delete Row" onclick={removeRow}>
</lightning-icon>
</td>


</tr>

</template>
<!--<template for:each={itemList} for:item="item" for:index="index">
<tr class="slds-hint-parent" key={item.id}>
<td data-label="Plan Ids">
												
														
	<lightning-combobox name="planId"  value={planId} options={planIds} 
											onchange={handlePlanIdsChange} ></lightning-combobox>
	<div class="error">{planIdError}</div>


</td>
<td data-label="Call Activities">

	<lightning-combobox name="callActivity"  value={callActivity} options={callActivitiesOptions}
											onchange={handlecallActivitiesChange} ></lightning-combobox>
	<div class="error">{callActivityError}</div>

</td>
<td data-label="Call Types">

	<lightning-combobox name="callType"  value={callType} options={callTypesOptions}
											onchange={handlecallTypesChange} ></lightning-combobox>
	<div class="error">{callTypeError}</div>


</td>

<td class="slds-cell_action-mode" role="gridcell">
	<lightning-icon icon-name="action:new" access-key={item.id} id={index}
	alternative-text="Add Row" size="small" title="Add Row" onclick={addRow}>
</lightning-icon>
&nbsp; &nbsp;
<lightning-icon icon-name="action:delete" access-key={item.id} id={index}
	alternative-text="Delete Row" size="small" title="Delete Row" onclick={removeRow}>
</lightning-icon>
</td>
</tr>
</template>-->
</tbody>
</table>



<div class="slds-m-top_small slds-m-horizontal_xxx-small">
    <lightning-button variant="brand" label="Create Case Action" title="Create Case Action" onclick={onCreateCaseAction} class="slds-m-horizontal_x-small"></lightning-button>
</div>

</div>


<div style="height: 200px;">
    <div class="slds-m-top_small slds-m-horizontal_x-small">
        <lightning-datatable key-field="Id" data={data} columns={columns} hide-checkbox-column="true">
        </lightning-datatable>
    </div>
</div>


</lightning-card>
</div>
<!--		</div>		-->

</lightning-card>

</template>

























keyIndex = 0;
@track itemList = [
    {
        id: 0,
        planId: '',
        callActivity: '',
        callType: '',
        planIdError: '',
        callActivityError: '',
        callTypeError: ''
    }
];

addRow() {
    ++this.keyIndex;
    const newItem = { id: this.keyIndex, planId: '', callActivity: '', callType: '' };
    this.itemList = [...this.itemList, newItem];
}

removeRow(event) {
    if (this.itemList.length > 1) {
        this.itemList = this.itemList.filter(item => item.id !== parseInt(event.target.accessKey));
    }
}

handlePlanIdsChange(event) {
    const itemId = parseInt(event.target.dataset.id);
    const newValue = event.target.value;
    this.itemList = this.itemList.map(item => 
        item.id === itemId ? { ...item, planId: newValue } : item
    );
}

handlecallActivitiesChange(event) {
    const itemId = parseInt(event.target.dataset.id);
    const newValue = event.target.value;
    this.itemList = this.itemList.map(item => 
        item.id === itemId ? { ...item, callActivity: newValue } : item
    );
}

handlecallTypesChange(event) {
    const itemId = parseInt(event.target.dataset.id);
    const newValue = event.target.value;
    this.itemList = this.itemList.map(item => 
        item.id === itemId ? { ...item, callType: newValue } : item
    );
}

async onCreateCaseAction() {
    try {
        let isValid = true;
        this.itemList = this.itemList.map(item => {
            let planIdError = item.planId ? '' : 'Plan Id is required.';
            let callActivityError = item.callActivity ? '' : 'Call Activity is required.';
            let callTypeError = item.callType ? '' : 'Call Type is required.';
            if (planIdError || callActivityError || callTypeError) {
                isValid = false;
            }
            return { ...item, planIdError, callActivityError, callTypeError };
        });

        if (isValid) {
            // Proceed with case creation logic
        } else {
            console.error("Validation failed");
        }
    } catch (error) {
        console.error(error);
    }
}









<template for:each={itemList} for:item="item" for:index="index">
    <tr class="slds-hint-parent" key={item.id}>
        <td data-label="Plan Ids">
            <lightning-combobox name="planId" value={item.planId} options={planIds} 
                                onchange={handlePlanIdsChange} data-id={item.id}></lightning-combobox>
            <div class="error">{item.planIdError}</div>
        </td>
        <td data-label="Call Activities">
            <lightning-combobox name="callActivity" value={item.callActivity} options={callActivitiesOptions}
                                onchange={handlecallActivitiesChange} data-id={item.id}></lightning-combobox>
            <div class="error">{item.callActivityError}</div>
        </td>
        <td data-label="Call Types">
            <lightning-combobox name="callType" value={item.callType} options={callTypesOptions}
                                onchange={handlecallTypesChange} data-id={item.id}></lightning-combobox>
            <div class="error">{item.callTypeError}</div>
        </td>
        <td class="slds-cell_action-mode" role="gridcell">
            <lightning-icon icon-name="action:new" access-key={item.id} id={index}
                            alternative-text="Add Row" size="small" title="Add Row" onclick={addRow}>
            </lightning-icon>
            &nbsp; &nbsp;
            <lightning-icon icon-name="action:delete" access-key={item.id} id={index}
                            alternative-text="Delete Row" size="small" title="Delete Row" onclick={removeRow}>
            </lightning-icon>
        </td>
    </tr>
</template>













<div class="demo-only demo-only--sizing slds-grid slds-wrap slds-m-top_xx-small"> 
											<table class="slds-table slds-table_cell-buffer slds-table_bordered" aria-labelledby="element-with-table-label other-element-with-table-label">
												<thead>
												  <tr class="slds-line-height_reset">
													<th class="" scope="col">
														<div class="slds-truncate" title="Plan Ids">Plan Ids</div>
													</th>
													<th class="" scope="col">
														<div class="slds-truncate" title="Call Activities">Call Activities</div>
													  </th>
													  <th class="" scope="col">
														<div class="slds-truncate" title="Call Types">Call Types</div>
													
													  </th>
													</tr>
													</thead>
													  <tbody>
														
														<template for:each={itemList} for:item="item" for:index="index">
															<tr class="slds-hint-parent" key={item.id}>
															
																
															<td data-label="Plan Ids">
												
														
																<lightning-combobox name="planId"  value={planId} options={planIds} 
																										onchange={handlePlanIdsChange} ></lightning-combobox>
																<div class="error">{planIdError}</div>
														
												
											</td>
											<td data-label="Call Activities">
												
																<lightning-combobox name="callActivity"  value={callActivity} options={callActivitiesOptions}
																										onchange={handlecallActivitiesChange} ></lightning-combobox>
																<div class="error">{callActivityError}</div>
														
											</td>
											<td data-label="Call Types">
												
																<lightning-combobox name="callType"  value={callType} options={callTypesOptions}
																										onchange={handlecallTypesChange} ></lightning-combobox>
																<div class="error">{callTypeError}</div>

														
											</td>
										
<td class="slds-cell_action-mode" role="gridcell">
	<lightning-icon icon-name="action:new" access-key={item.id} id={index}
	alternative-text="Add Row" size="small" title="Add Row" onclick={addRow}>
</lightning-icon>
&nbsp; &nbsp;
<lightning-icon icon-name="action:delete" access-key={item.id} id={index}
	alternative-text="Delete Row" size="small" title="Delete Row" onclick={removeRow}>
</lightning-icon>
</td>


</tr>

</template>
    </tbody>
</table>



<div class="slds-m-top_small slds-m-horizontal_xxx-small">
    <lightning-button variant="brand" label="Create Case Action" title="Create Case Action" onclick={onCreateCaseAction} class="slds-m-horizontal_x-small"></lightning-button>
</div>

</div>


    keyIndex = 0;
    @track itemList = [{
        id: 0
    }];

    addRow() {
        ++this.keyIndex;
        var newItem = [{ id: this.keyIndex }];
        this.itemList = this.itemList.concat(newItem);
    }

    removeRow(event) {
        if (this.itemList.length >= 2) {
            this.itemList = this.itemList.filter(function(element) {
                return parseInt(element.id) !== parseInt(event.target.accessKey);
            });
        }
    }

async onCreateCaseAction() {


        try {
            //Validate Plan Ids
            if (!this.planId) {
                this.planIdError = 'Plan Id is required.';
            } else {
                this.planIdError = '';
            }

            //Validate Plan Ids
            if (!this.callActivity) {
                this.callActivityError = 'Call Activity is required.';
            } else {
                this.callActivityError = '';
            }

            //Validate Plan Ids
            if (!this.callType) {
                this.callTypeError = 'Call Type is required.';
            } else {
                this.callTypeError = '';
            }

            if (!this.callTypeError && !this.callActivityError && !this.planIdError) {

                console.log('onCreateCaseAction recordid ', this.recordId)
                const csId = await getCaseId({ clientId: this.recordId });
                console.log('onCreateCaseAction csId', csId);

                if (csId === null) {

                    const NewcaseId = await createNewCase({ clientId: this.recordId, planId: this.planId, callActivity: this.callActivity, callType: this.callType })
                    console.log('onCreateCase csId', NewcaseId);
                    const res = await getCurrentCase({ Id: NewcaseId });
                    console.log('nCreateCas res', res);
                    if (res.length == 1) {
                        this.caseNum = res[0].CaseNumber;

                    }
                    const caseActionId = await createCaseActions({ clientId: this.recordId, caseId: NewcaseId, planId: this.planId, callActivity: this.callActivity, callType: this.callType })
                    const caseActionIdStr = JSON.stringify(caseActionId)
                    let caseActionIdSet = [];
                    caseActionIdSet.push(caseActionId);
                    const caseActionToBeDisplayed = await getRelatedCaseActions({ caseId: NewcaseId, caseActionId: caseActionIdSet })
                    let arrFinal = caseActionToBeDisplayed;
                    console.log("onCreateCaseAction array: ", arrFinal);
                    arrFinal = arrFinal.map(row => {
                        return { PlanID_Text__c: row.PlanID_Text__c, Call_Activity__c: row.Call_Activity__c, Call_Type__c: row.Call_Type__c };

                    });
                    this.data = arrFinal;
                    console.log("onCreateCaseAction final", arrFinal);
                } else {


                    console.log('onCreateCaseAction this.planId', this.planId);
                    console.log('onCreateCaseAction this.callActivity', this.callActivity);
                    console.log('onCreateCaseAction this.callType', this.callType);
                    const caseActionId = await createCaseActions({ clientId: this.recordId, caseId: csId, planId: this.planId, callActivity: this.callActivity, callType: this.callType })

                    console.log('onCreateCaseAction save : ' + caseActionId);
                    const caseActionIdStr = JSON.stringify(caseActionId)
                    console.log('onCreateCaseActionstr : ' + caseActionIdStr);

                    let caseActionIdSet = [];
                    caseActionIdSet.push(caseActionId);


                    const caseActionToBeDisplayed = await getRelatedCaseActions({ caseId: csId, caseActionId: caseActionIdSet })
                    console.log("onCreateCaseAction get ------: ", caseActionToBeDisplayed);


                    //  this.caseActionList.push(caseActionToBeDisplayed);
                    //  console.log("onCreateCaseAction list : ", this.caseActionList);

                    let arrFinal = caseActionToBeDisplayed;

                    console.log("onCreateCaseAction array: ", arrFinal);
                    arrFinal = arrFinal.map(row => {

                        return { PlanID_Text__c: row.PlanID_Text__c, Call_Activity__c: row.Call_Activity__c, Call_Type__c: row.Call_Type__c };

                    });
                    this.data = arrFinal;
                    console.log("onCreateCaseAction final", arrFinal);
                }
                this.callType = '';
                this.callActivity = '';
                this.planId = '';
            }
        } catch (error) {
            console.error(error);
        }


    }












keyIndex = 0;
@track itemList = [
    {
        id: 0
    }
];

addRow() {
    ++this.keyIndex;
    var newItem = [{ id: this.keyIndex }];
    this.itemList = this.itemList.concat(newItem);
}

removeRow(event) {
    if (this.itemList.length >= 2) {
        this.itemList = this.itemList.filter(function (element) {
            return parseInt(element.id) !== parseInt(event.target.accessKey);
        });
    }
}

handleSubmit() {
    var isVal = true;
    this.template.querySelectorAll('lightning-input-field').forEach(element => {
        isVal = isVal && element.reportValidity();
    });
    if (isVal) {
        this.template.querySelectorAll('lightning-record-edit-form').forEach(element => {
            element.submit();
        });
        this.dispatchEvent(
            new ShowToastEvent({
                title: 'Success',
                message: 'Contacts successfully created',
                variant: 'success',
            }),
        );
        // Navigate to the Account home page
        this[NavigationMixin.Navigate]({
            type: 'standard__objectPage',
            attributes: {
                objectApiName: 'Contact',
                actionName: 'home',
            },
        });
    } else {
        this.dispatchEvent(
            new ShowToastEvent({
                title: 'Error creating record',
                message: 'Please enter all the required fields',
                variant: 'error',
            }),
        );
    }
















<template for:each={itemList} for:item="item" for:index="index">
        <lightning-record-edit-form key={item.id} object-api-name="Contact">
            <lightning-messages> </lightning-messages>
            <lightning-layout multiple-rows>

                <lightning-layout-item size="12" small-device-size="6" medium-device-size="4" large-device-size="2"
                    padding="around-small">
                    <lightning-input-field field-name="FirstName" variant="label-stacked" >
                    </lightning-input-field>
                </lightning-layout-item>

                <lightning-layout-item size="12" small-device-size="6" medium-device-size="4" large-device-size="2"
                    padding="around-small">
                    <lightning-input-field field-name="LastName" variant="label-stacked" required>
                    </lightning-input-field>
                </lightning-layout-item>

                <lightning-layout-item size="12" small-device-size="6" medium-device-size="4" large-device-size="2"
                    padding="around-small">
                    <lightning-input-field field-name="Title" variant="label-stacked" >
                    </lightning-input-field>
                </lightning-layout-item>

                <lightning-layout-item size="12" small-device-size="6" medium-device-size="4" large-device-size="2"
                    padding="around-small">
                    <lightning-input-field field-name="Phone" variant="label-stacked" >
                    </lightning-input-field>
                </lightning-layout-item>

                <lightning-layout-item size="12" small-device-size="6" medium-device-size="4" large-device-size="2"
                    padding="around-small">
                    <lightning-input-field field-name="Email" variant="label-stacked" required>
                    </lightning-input-field>
                </lightning-layout-item>
                
                <lightning-layout-item size="12" small-device-size="6" medium-device-size="4" large-device-size="2" padding="around-small">
                    <div class="slds-p-top_medium">

                        <lightning-icon icon-name="action:new" access-key={item.id} id={index}
                            alternative-text="Add Row" size="small" title="Add Row" onclick={addRow}>
                        </lightning-icon>
                        &nbsp; &nbsp;
                        <lightning-icon icon-name="action:delete" access-key={item.id} id={index}
                            alternative-text="Delete Row" size="small" title="Delete Row" onclick={removeRow}>
                        </lightning-icon>

                    </div>
                </lightning-layout-item>
            </lightning-layout>

        </lightning-record-edit-form>
    </template>
    </br>
    <lightning-layout>
        <div class="slds-align_absolute-center">
            <lightning-button variant="success" onclick={handleSubmit} name="submit" label="Submit">
            </lightning-button>
        </div>
    </lightning-layout>
