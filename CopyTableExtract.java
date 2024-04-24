public ArcticleEditSuggestPlanController() {
        //this.articleCase = new Case();
         Case articleCase=new Case();
        
     	pageType =  ApexPages.currentPage().getParameters().get('pageType');
     	Profile p = [Select Name from Profile where Id =: userinfo.getProfileid()];
		profileName = p.name;
     	system.debug('pageType'+ pageType);
     	if(pageType != null && pageType == 'PAAG'){
     		RecordType caseRecType = [Select Id, Name, SobjectType from RecordType where (SobjectType ='Case') and (Name ='PAAG Modification Request') limit 1];
       		articleCase.RecordTypeId = caseRecType.Id;
     	 	planID = ApexPages.currentPage().getParameters().get('planId');
     	 	system.debug('planId'+ planId);
     	 	String paagId = ApexPages.currentPage().getParameters().get('paagID');
     	 	system.debug('paagID'+ paagId);
     	 	planNumber = ApexPages.currentPage().getParameters().get('planNumber');
     	 	system.debug('planNumber'+ planNumber);
     	 	planName = ApexPages.currentPage().getParameters().get('planName');
     	 	market =  ApexPages.currentPage().getParameters().get('market');
     	 	
     	 	system.debug('planName'+ planName);
     	 	articleCase.Plan_ID__c = planID;
     	 	articleCase.subject = 'PAAG Edit Request';
     	 	articleCase.Article_Url__c = 'https://' + ApexPages.currentPage().getHeaders().get('Host')+ '/' + paagId;
     	 	articleCase.Market__c = market;
     	 	queueName = 'PAAG & Article Edit Request Queue';
     	}
     	else {
     	 	RecordType caseRecType = [Select Id, Name, SobjectType from RecordType where (SobjectType ='Case') and (Name ='CCC KM Article Update Request') limit 1];
       		articleCase.RecordTypeId = caseRecType.Id;
	    	//articleCase.Article_Url__c = ApexPages.currentPage().getParameters().get('articleUrl');
	     	articleCase.Article_Name__c = ApexPages.currentPage().getParameters().get('articleName');
	     	System.debug('Article Name' + articleCase.Article_Name__c);
	     	String articleType =  ApexPages.currentPage().getParameters().get('articleType');
	     	System.debug('Article Type' + articleType);
	     	if('Job_Aid__kav'.equals(articleType)){
	     		queueName = 'Job Aid Article Edit Queue';
	     	 	articleCase.subject = 'Job Aid Edit Request';
	     	}
	     	else {
	     	 	queueName = 'PAAG & Article Edit Request Queue';
	     	 	articleCase.subject =  'Plan Article Edit Request';
	     	}
	     	articleType =  articleType.Substring(0, articleType.length()-5);
	     	articleCase.Article_Type__c = articleType;     	
	     	articleCase.Article_Url__c = 'https://' + ApexPages.currentPage().getHeaders().get('Host')+ '/articles/' + articleType + '/' + articleCase.Article_Name__c;
     	}
        articleCase.Status = 'New';
     	Apexpages.currentPage().getHeaders().put('X-UA-Compatible', 'IE=edge');
    }









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
