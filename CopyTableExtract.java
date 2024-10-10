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
