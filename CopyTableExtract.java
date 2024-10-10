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
