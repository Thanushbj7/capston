<template>
    <!-- Add any necessary imports here -->
    <lightning-card title="{if: true}" variant="Narrow">
        <div if:true={pageType === 'PAAG'}>
            <p>Plan: {planNumber}</p>
            <p>Plan Name: {planName}</p>
        </div>
        <div if:true={articleCase.Article_Url__c}>
            <p>Url: {articleCase.Article_Url__c}</p>
        </div>
        <div if:true={pageType === 'PAAG'}>
            <lightning-input type="checkbox" label="PAAG Section Applicable" onchange={handlePsaChange} checked={articleCase.PAAG_Section_Applicable__c}></lightning-input>
        </div>
        <div if:true={articleCase.Article_Url__c}>
            <p>Article Url: {articleCase.Article_Url__c}</p>
        </div>
        <lightning-textarea label="Comments" value={articleCase.Article_Comments__c} onchange={handleCommentsChange}></lightning-textarea>
        <template if:true={pageType !== 'PAAG'}>
            <lightning-combobox label="Edit Request Type" options={editRequestTypeOptions} value={articleCase.Edit_Request_Type__c} onchange={handleEditRequestTypeChange}></lightning-combobox>
        </template>
        <template if:true={profileName !== 'CSA'}>
            <lightning-input label="Priority" type="text" value={articleCase.Priority} onchange={handlePriorityChange}></lightning-input>
        </template>
        <template if:true={profileName === 'CSA'}>
            <p>Priority: {articleCase.Priority}</p>
        </template>
        <lightning-input type="file" label="Attach File" onchange={handleFileChange}></lightning-input>
        <div if:true={formSaved} style="font-weight:bold;margin-left:100px;margin-top:40px">
            <p>Change Request has been Submitted.</p>
        </div>
        <div if:false={formSaved}>
            <lightning-button label="Save" onclick={save} variant="brand"></lightning-button>
            <lightning-button label="Cancel" onclick={closeTab} variant="neutral"></lightning-button>
        </div>
    </lightning-card>
</template>








  import { LightningElement, track } from 'lwc';

export default class ArticleEditSuggest extends LightningElement {
    @track articleCase = {};
    @track fileName;
    @track fileBody;
    @track formSaved = false;
    @track pageType;
    @track planNumber;
    @track planName;
    @track profileName;

    connectedCallback() {
        // Initialize component properties here
        // Fetch data from Apex controller or parameters
    }

    handlePsaChange(event) {
        this.articleCase.PAAG_Section_Applicable__c = event.target.checked;
    }

    handleCommentsChange(event) {
        this.articleCase.Article_Comments__c = event.target.value;
    }

    handleEditRequestTypeChange(event) {
        this.articleCase.Edit_Request_Type__c = event.target.value;
    }

    handlePriorityChange(event) {
        this.articleCase.Priority = event.target.value;
    }

    handleFileChange(event) {
        this.fileName = event.target.files[0].name;
        this.fileBody = event.target.files[0];
    }

    save() {
        // Implement save logic here
        this.performSave();
        setTimeout(() => {
            this.closeTab();
        }, 3000);
    }

    closeTab() {
        // Implement close tab logic here
        if (this.pageType === 'PAAG') {
            // Close tab logic for PAAG
        } else {
            // Close tab logic for other page types
        }
    }
                     }
