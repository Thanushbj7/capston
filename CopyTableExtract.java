https://www.forcetalks.com/blog/file-upload-in-lightning-web-component-lwc-all-you-need-to-know/









<!-- savrButton.html -->
<template>
    <lightning-input type="text" label="Type something" onchange={handleChange}></lightning-input>
    <lightning-button 
        label="Savr" 
        class={isGrey ? 'grey-button' : 'blue-button'} 
        onclick={handleClick}>
    </lightning-button>
</template>







https://trailhead.salesforce.com/trailblazer-community/feed/0D54V00007T4FbUSAV




public with sharing class ArticleEditSuggestController {
    @AuraEnabled(cacheable=true)
    public static void initializeArticleEditSuggestPlan(String pageType, String planId, String paagID, String planNumber, String planName, String market) {
        // Your initialization logic here...
    }

    @AuraEnabled
    public static void save() {
        // Save logic...
    }
}




import { LightningElement, api, track } from 'lwc';

export default class LwcComponent extends LightningElement {
    @api pageType;
    @api planNumber;
    @api planName;
    @api profileName;
    @track articleCase = {};
    @track fileName;
    @track fileBody;
    @track formSaved = false;

    handlePsaChange(event) {
        // Handle PAAG Section Applicable change
    }

    handleCommentsInput(event) {
        // Handle comments input change
        this.articleCase.Article_Comments__c = event.target.value;
    }

    handleEditRequestTypeChange(event) {
        // Handle Edit Request Type change
        this.articleCase.Edit_Request_Type__c = event.target.value;
    }

    handlePriorityChange(event) {
        // Handle Priority change
        this.articleCase.Priority = event.target.value;
    }

    handleFileChange(event) {
        // Handle file input change
        this.fileName = event.target.files[0].name;
        this.fileBody = event.target.files[0];
    }

    save() {
        // Perform save operation
        // Call Apex method to save data
        // Set this.formSaved = true; after successful save
    }

    closeTab() {
        // Close tab logic
    }
}





<template>
    <div if:true={formSaved} style="font-weight:bold;margin-left:100px !important;margin-top:40px !important;">Change Request has been Submitted.</div>
    <div if:false={formSaved}>
        <div if:true={pageType === 'PAAG'}>
            <div style="font-color:#4a4a56;padding-left:197px;">Plan</div>
            <div style="padding-left:5px;">{planNumber}</div>
            <div style="font-color:#4a4a56;padding-left:50px;">Plan Name</div>
            <div style="padding-left:5px;">{planName}</div>
        </div>
        <div>Url: {articleCase.Article_Url__c}</div>
        <template if:true={pageType === 'PAAG'}>
            <lightning-input type="checkbox" label="PAAG Section Applicable" onchange={handlePsaChange}></lightning-input>
        </template>
        <template if:true={articleCase.Article_Url__c}>
            <div>Article Url: {articleCase.Article_Url__c}</div>
        </template>
        <div>Comments: <textarea value={articleCase.Article_Comments__c} oninput={handleCommentsInput}></textarea></div>
        <template if:true={pageType !== 'PAAG'}>
            <lightning-input label="Edit Request Type" value={articleCase.Edit_Request_Type__c} onchange={handleEditRequestTypeChange}></lightning-input>
        </template>
        <template if:true={profileName !== 'CSA'}>
            <lightning-input label="Priority" value={articleCase.Priority} onchange={handlePriorityChange}></lightning-input>
        </template>
        <template if:true={profileName === 'CSA'}>
            <div>Priority: {articleCase.Priority}</div>
        </template>
        <div>Attach File: <input type="file" onchange={handleFileChange}></div>
    </div>
    <div>
        <lightning-button label="Save" onclick={save} variant="brand" if:false={formSaved}></lightning-button>
        <lightning-button label="Cancel" onclick={closeTab} if:false={formSaved}></lightning-button>
    </div>
</template>






<apex:page showHeader="false" sidebar="false"  standardController="Case" tabStyle="Account" standardStylesheets="true"  extensions="ArticleEditSuggestController">
 
<apex:includeScript value="/support/console/40.0/integration.js"/>
<script src="../../soap/ajax/32.0/connection.js" type="text/javascript"></script>
<script src="{!$Resource.jquery}"></script>
<script src="{!$Resource.html5LocalCache}"></script>
<script src="{!$Resource.persist_console_data}"></script>
 <script>
        function closeTab() {
            //First find the ID of the current tab to close it
            if(sforce.console.isInConsole())
                sforce.console.getEnclosingTabId(closeSubtab);
            else
                closeWindow();
            
        }
        
        function closeWindow() {
            window.close();
        }
        var closeSubtab = function closeSubtab(result) {
            //Now that we have the tab ID, we can close it
            var tabId = result.id;
            sforce.console.closeTab(tabId);
        };
        
        function save() {
           performSave();
           setTimeout(function(){closeTab()}, 3000);
        }
        
        $( document ).ready(function() { 
            // Initiate Data Persistence Framework
            initDataPersistence($);
                
            //Work around to close the tab on case is submitted
            var reload = {!formSaved};
            if(reload)
                closeTab();
        }); 
 </script>
 <!--[if gt IE 7]>
            <script>
                 function closeWindow() {
                    this.focus();
                    self.opener = this;
                    self.close();
                }
            </script>
 <![endif]-->
 <apex:form id="frm">
        <apex:pageBlock >
            <apex:pageBlockSection title="{!IF(pageType = 'PAAG','PAAG','Article')} Edit Suggestion" columns="1" collapsible="false">
               <apex:outputPanel rendered="{!IF(pageType = 'PAAG','true','false')}">
                    <apex:outputLabel style="font-color:#4a4a56;padding-left:197px;" value="Plan"/>     
                    <apex:outputlabel style="padding-left:5px;"  value="{!planNumber}"/>
               
                    <apex:outputLabel style="font-color:#4a4a56;padding-left:50px;"  value="Plan Name"/>     
                    <apex:outputlabel style="padding-left:5px;" value="{!planName}"/>
               
                </apex:outputPanel>
                <apex:pageBlockSectionItem >
                    <apex:outputLabel value="Url"/>     
                    <apex:outputlabel value="{!articleCase.Article_Url__c}"/>
                </apex:pageBlockSectionItem>
                <apex:pageBlockSectionItem rendered="{!IF(pageType = 'PAAG','true','false')}">
                    <apex:outputLabel value="PAAG Section Applicable"/>     
                    <apex:inputfield value="{!articleCase.PAAG_Section_Applicable__c}" id="psa" styleClass="persistconsoledata"/>
                </apex:pageBlockSectionItem>
                <apex:pageBlockSectionItem rendered="{IF(articleCase.Article_Url__c !=null)}">
                    <apex:outputLabel value="Article Url"/>     
                    <apex:outputlabel value="{!articleCase.Article_Url__c}"/>
                </apex:pageBlockSectionItem>
                <apex:pageBlockSectionItem >
                    <apex:outputLabel value="Comments"/>
                    <apex:inputtextArea value="{!articleCase.Article_Comments__c}" cols="100" rows="10" id="comments" styleClass="persistconsoledata"/>    
                </apex:pageBlockSectionItem>
                 <apex:pageBlockSectionItem rendered="{!IF(pageType != 'PAAG','true','false')}">
                    <apex:outputLabel value="Edit Request Type"/>
                    <apex:inputField value="{!articleCase.Edit_Request_Type__c}"/>    
                </apex:pageBlockSectionItem>
                 <apex:pageBlockSectionItem rendered="{!IF(profileName != 'CSA','true','false')}">
                    <apex:outputLabel value="Priority"/>
                    <apex:inputField value="{!articleCase.Priority}" styleClass="persistconsoledata"/>
                </apex:pageBlockSectionItem>
                <apex:pageBlockSectionItem rendered="{!IF(profileName != 'CSA','false','true')}">
                    <apex:outputLabel value="Priority"/>
                    <apex:outputField value="{!articleCase.Priority}"/>    
                </apex:pageBlockSectionItem>
                <apex:pageBlockSectionItem >  
                     <apex:outputLabel value="Attach File"/>                       
                    <apex:inputFile id="fileToUpload" value="{!fileBody}" filename="{!fileName}" styleClass="input-file"/>                            
                </apex:pageBlockSectionItem> 
            </apex:pageBlockSection>     
            
         <apex:pageBlockButtons >
            <apex:outputText rendered="{!formSaved}" style="font-weight:bold;margin-left:100px !important;margin-top:40px !important;" value="Change Request has been Submitted.">
            </apex:outputText>  
            <apex:commandButton rendered="{!NOT(formSaved)}" value="Save" action="{!save}" id="save"/>
            <apex:commandButton rendered="{!NOT(formSaved)}" onclick="closeTab();return false" value="Cancel" id="closeButton"/>
        </apex:pageBlockButtons>          
       </apex:pageBlock>     

    </apex:form>
</apex:page>










public with sharing class  ArcticleEditSuggestPlanController {
//public ApexPages.StandardController controller; 
	 @AuraEnabled public static Case articleCase{get;set;}
	@AuraEnabled public static Attachment caseAttachment{get;set;}
   @AuraEnabled public static string fileName{get;set;} 
	@AuraEnabled public static transient Blob fileBody{get;set;}
	@AuraEnabled public static String queueName{get;set;}
	@AuraEnabled public static String planID{get;set;}
	@AuraEnabled public static String planName{get;set;}
	@AuraEnabled public static String planNumber{get;set;}
	@AuraEnabled public static String pageType{get;set;}
	@AuraEnabled public static String market{get;set;}
	@AuraEnabled public static boolean formSaved{get;set;}
	@AuraEnabled public static String profileName{get;set;}
    
    
    @AuraEnabled(cacheable=true)
     public static void initializeArticleEditSuggestPlan() {
   // Case articleCase = new Case();
    
    pageType = ApexPages.currentPage().getParameters().get('pageType');
    Profile p = [SELECT Name FROM Profile WHERE Id = :UserInfo.getProfileId()];
    profileName = p.Name;
    System.debug('pageType: ' + pageType);
    
    if (pageType != null && pageType == 'PAAG') {
        RecordType caseRecType = [SELECT Id, Name, SobjectType FROM RecordType WHERE SObjectType = 'Case' AND Name = 'PAAG Modification Request' LIMIT 1];
        articleCase.RecordTypeId = caseRecType.Id;
        planID = ApexPages.currentPage().getParameters().get('planId');
        System.debug('planId: ' + planId);
        String paagId = ApexPages.currentPage().getParameters().get('paagID');
        System.debug('paagID: ' + paagId);
        planNumber = ApexPages.currentPage().getParameters().get('planNumber');
        System.debug('planNumber: ' + planNumber);
        planName = ApexPages.currentPage().getParameters().get('planName');
        market = ApexPages.currentPage().getParameters().get('market');
        
        System.debug('planName: ' + planName);
        articleCase.Plan_ID__c = planID;
        articleCase.Subject = 'PAAG Edit Request';
        articleCase.Article_Url__c = 'https://' + ApexPages.currentPage().getHeaders().get('Host') + '/' + paagId;
        articleCase.Market__c = market;
        queueName = 'PAAG & Article Edit Request Queue';
    } else {
        RecordType caseRecType = [SELECT Id, Name, SobjectType FROM RecordType WHERE SObjectType = 'Case' AND Name = 'CCC KM Article Update Request' LIMIT 1];
        articleCase.RecordTypeId = caseRecType.Id;
        // articleCase.Article_Url__c = ApexPages.currentPage().getParameters().get('articleUrl');
        articleCase.Article_Name__c = ApexPages.currentPage().getParameters().get('articleName');
        System.debug('Article Name: ' + articleCase.Article_Name__c);
        String articleType = ApexPages.currentPage().getParameters().get('articleType');
        System.debug('Article Type: ' + articleType);
        if ('Job_Aid__kav'.equals(articleType)) {
            queueName = 'Job Aid Article Edit Queue';
            articleCase.Subject = 'Job Aid Edit Request';
        } else {
            queueName = 'PAAG & Article Edit Request Queue';
            articleCase.Subject = 'Plan Article Edit Request';
        }
        articleType = articleType.substring(0, articleType.length() - 5);
        articleCase.Article_Type__c = articleType;
        articleCase.Article_Url__c = 'https://' + ApexPages.currentPage().getHeaders().get('Host') + '/articles/' + articleType + '/' + articleCase.Article_Name__c;
    }
    articleCase.Status = 'New';
    ApexPages.currentPage().getHeaders().put('X-UA-Compatible', 'IE=edge');
}
    
    // @AuraEnabled
     @AuraEnabled(cacheable=true)
    public  static String getsave() {
        User user = [select Id, Name, UserRole.Name  from User where id = :UserInfo.getUserId()];
       	articleCase.User_Role__c = user.UserRole.Name;
       	System.debug('QueueName' + queueName);
        List<Group> groupList = [select Id from Group where Name = :queueName and Type = 'Queue'];
        System.debug('Queue List Size' + groupList.size());
        if(groupList.size() > 0){
       		articleCase.OwnerId = groupList.get(0).Id;
        }
       
        if(String.isBlank(articleCase.id))
       		insert articleCase;
       	if(fileBody != null){
       		Attachment attachment  = new Attachment();
	       	attachment.Body = fileBody;
       	   	attachment.Name = fileName;
	       	attachment.ParentId = articleCase.id;             
	       	insert attachment; 
       	}                
       	fileBody = null;
       	//pagereference pr = new pagereference('/'+articleCase.id);      
       	formSaved = true;                     
       	return null;
    }
}
