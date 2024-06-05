@isTest
private class RolloverStrategyTrigger_Test
{    
    static testMethod void RolloverStrategyTriggerTest()
    {
        User testUser = TestUtilsSBR.createUser('System Administrator');  
        System.runAs(testUser)
        {

        //RecordType[] rt =[Select Id, IsPersonType, Name, SobjectType from RecordType where  SobjectType ='Account' and Name ='Client' and IsPersonType = true];
        
        //Getting Client RecordType ID
        String clientRecTypeId = Schema.SObjectType.Account.RecordTypeInfosByName.get('Client').RecordTypeId;
        system.debug('###clientRecTypeId'+clientRecTypeId);
        //Creating Test Account record
        Account accObj = new Account(firstName='null',lastName = 'test',ssn__c = '112131415', RecordTypeId = clientRecTypeId, PersonMailingCity = 'test', PersonMailingState = 'test', Batch_Id__c  = '123',ING_Alternate_User_Guest__c ='test');
        insert accObj;
        
        system.assertEquals(accObj.PersonMailingState,'test');
        system.assertNotEquals(accObj.RecordTypeId,'');
        system.debug('###accObj.RecordTypeId'+accObj.RecordTypeId);

        //Creating Test Plan record
        Plan__c planObj = new Plan__c(name='tktestplan', Producer_TIN_ist__c='121231234');
        insert planObj;

        //Creating Test Opportunity record
        Opportunity oppObj = new Opportunity(Outbound_Lead__c='',name='tktest_111111111',Opportunity_Status__c='Closed - Won',leadsource='test',stagename='test',closedate=Date.today(), plan__c=planObj.id, at_Risk__c=12345,Account=accObj);
        insert oppObj;
        
        DateTime dt = System.now();
        
        //Creating Test Extended_Authentication__c record
        Extended_Authentication__c extAuthObj = new Extended_Authentication__c(Auth_Begin_Date__c=dt, Auth_Expire_Date__c=dt, Client__c=accObj.Id);
        
        insert extAuthObj;

        //Performing DML Operations on Rollover_Strategy__c object
        Test.startTest();
        Rollover_Strategy__c rollOvrStrtgyObj= new Rollover_Strategy__c();
        rollOvrStrtgyObj.Opportunity__c = oppObj.Id;
        insert rollOvrStrtgyObj;
        
        if(rollOvrStrtgyObj.Code_Verified__c != true)
        rollOvrStrtgyObj.Code_Verified__c = true;
        else
        rollOvrStrtgyObj.Code_Verified__c = false;
        rollOvrStrtgyObj.Consent_Status__c = 'Yes';
        
        update rollOvrStrtgyObj;
        
        rollOvrStrtgyObj.Consent_Status__c = 'No';
        update rollOvrStrtgyObj;
        
        rollOvrStrtgyObj.Consent_Status__c = 'Yes';
        update rollOvrStrtgyObj;
        
        delete rollOvrStrtgyObj;
        
        undelete rollOvrStrtgyObj;
        
        //Covering remaining methods and variables of RolloverStrategyTriggerHandler
        RolloverStrategyTriggerHandler rollOvrStrtgyHandlrObj= new RolloverStrategyTriggerHandler(true,10);
        
        rollOvrStrtgyHandlrObj.isBefore = true;
        rollOvrStrtgyHandlrObj.isAfter = true;
        rollOvrStrtgyHandlrObj.isInsert = true;
        rollOvrStrtgyHandlrObj.isUpdate = true;
        
        system.debug('###IsVisualforcePageContext'+rollOvrStrtgyHandlrObj.IsVisualforcePageContext);
        system.debug('###IsWebServiceContext'+rollOvrStrtgyHandlrObj.IsWebServiceContext);
        system.debug('###IsExecuteAnonymousContext'+rollOvrStrtgyHandlrObj.IsExecuteAnonymousContext);
        
        Test.stopTest();
    }
    
    }
}








/*
 * @author : Deshraj Kumawat
 * @version : 1.0
 * @date: 01/26/2016
 */
trigger RolloverStrategyTrigger on Rollover_Strategy__c (before insert, before update) {
    
    RolloverStrategyTriggerHandler handler = new RolloverStrategyTriggerHandler(Trigger.isExecuting, Trigger.size);
    
    //set trigger event type variable
	handler.isBefore = Trigger.isBefore;
	handler.isAfter = Trigger.isAfter;
	
	handler.isInsert = Trigger.isInsert;
	handler.isUpdate = Trigger.isUpdate;
	
	if(Trigger.isInsert && Trigger.isBefore){
		handler.OnBeforeInsert(Trigger.new);
	}
	else if(Trigger.isInsert && Trigger.isAfter){
		//handler.OnAfterInsert(Trigger.new);
	}
	else if(Trigger.isUpdate && Trigger.isBefore){
		handler.OnBeforeUpdate(Trigger.new, Trigger.old, Trigger.newMap, Trigger.oldMap);
	}
	else if(Trigger.isUpdate && Trigger.isAfter){
		//handler.OnAfterUpdate(Trigger.new, Trigger.old, Trigger.newMap, Trigger.oldMap);
	}
	else if(Trigger.isDelete && Trigger.isBefore){
		//handler.OnBeforeDelete(Trigger.old, Trigger.oldMap);
	}
	else if(Trigger.isDelete && Trigger.isAfter){
		//handler.OnAfterDelete(Trigger.old, Trigger.oldMap);
	}
	else if(Trigger.isUnDelete){
		//handler.OnUndelete(Trigger.new);	
	}
}











@isTest
public class OpportunityProcessorTest {
    @isTest
    static void testUpdateOpportunityWithAgent() {
        // Create test data
        RecordType producerRecType = new RecordType(
            Name = 'Producer', 
            sObjectType = 'Account', 
            DeveloperName = 'Producer'
        );
        insert producerRecType;

        Account producerAccount = new Account(
            Name = 'Test Producer',
            Producer_SSN__c = '123-45-6789',
            RecordTypeId = producerRecType.Id
        );
        insert producerAccount;

        Opportunity newOpp = new Opportunity(
            Name = 'Test Opportunity',
            CloseDate = Date.today(),
            StageName = 'Prospecting'
        );

        // Initialize OpportunityProcessor
        OpportunityProcessor processor = new OpportunityProcessor();
        processor.selectedRepTIN = '123-45-6789';
        processor.newOpp = newOpp;

        // Invoke the method
        Test.startTest();
        processor.updateOpportunityWithAgent();
        Test.stopTest();

        // Verify the result
        System.assertEquals(producerAccount.Id, processor.newOpp.Agent_Name__c, 'The Agent_Name__c field should be set correctly');
    }

    @isTest
    static void testUpdateOpportunityWithAgent_NoMatch() {
        // Create test data
        RecordType producerRecType = new RecordType(
            Name = 'Producer', 
            sObjectType = 'Account', 
            DeveloperName = 'Producer'
        );
        insert producerRecType;

        Opportunity newOpp = new Opportunity(
            Name = 'Test Opportunity',
            CloseDate = Date.today(),
            StageName = 'Prospecting'
        );

        // Initialize OpportunityProcessor
        OpportunityProcessor processor = new OpportunityProcessor();
        processor.selectedRepTIN = '000-00-0000'; // No matching account
        processor.newOpp = newOpp;

        // Invoke the method
        Test.startTest();
        processor.updateOpportunityWithAgent();
        Test.stopTest();

        // Verify the result
        System.assertEquals(null, processor.newOpp.Agent_Name__c, 'The Agent_Name__c field should be null when no matching account is found');
    }

    @isTest
    static void testUpdateOpportunityWithAgent_NullTIN() {
        // Create test data
        Opportunity newOpp = new Opportunity(
            Name = 'Test Opportunity',
            CloseDate = Date.today(),
            StageName = 'Prospecting'
        );

        // Initialize OpportunityProcessor
        OpportunityProcessor processor = new OpportunityProcessor();
        processor.selectedRepTIN = null; // Null TIN
        processor.newOpp = newOpp;

        // Invoke the method
        Test.startTest();
        processor.updateOpportunityWithAgent();
        Test.stopTest();

        // Verify the result
        System.assertEquals(null, processor.newOpp.Agent_Name__c, 'The Agent_Name__c field should be null when selectedRepTIN is null');
    }

    @isTest
    static void testUpdateOpportunityWithAgent_EmptyTIN() {
        // Create test data
        Opportunity newOpp = new Opportunity(
            Name = 'Test Opportunity',
            CloseDate = Date.today(),
            StageName = 'Prospecting'
        );

        // Initialize OpportunityProcessor
        OpportunityProcessor processor = new OpportunityProcessor();
        processor.selectedRepTIN = ''; // Empty TIN
        processor.newOpp = newOpp;

        // Invoke the method
        Test.startTest();
        processor.updateOpportunityWithAgent();
        Test.stopTest();

        // Verify the result
        System.assertEquals(null, processor.newOpp.Agent_Name__c, 'The Agent_Name__c field should be null when selectedRepTIN is empty');
    }
}







if(this.selectedRepTIN != null && this.selectedRepTIN != '') {
            RecordType producerRecType = [Select Id From RecordType Where sObjectType = 'Account' and Name = 'Producer' limit 1];
            List<Account> producerObjList = [select Id from Account where Producer_SSN__c = :this.selectedRepTIN and RecordTypeId = :producerRecType.Id];
            
            if(producerObjList != null && producerObjList.size() > 0)
                newOpp.Agent_Name__c = producerObjList[0].Id;
        }





// Assuming you have an HTML table with id "myTable" and the cell you want to add the dropdown to has id "dropdownCell"

// Define the options for the dropdown list
const options = ['Option 1', 'Option 2', 'Option 3'];

// Get the cell where you want to add the dropdown list
const dropdownCell = document.getElementById('dropdownCell');

// Create a select element (dropdown list)
const selectList = document.createElement('select');

// Add options to the dropdown list dynamically
options.forEach(option => {
    const optionElement = document.createElement('option');
    optionElement.text = option;
    selectList.appendChild(optionElement);
});

// Append the dropdown list to the cell
dropdownCell.appendChild(selectList);

// Add the ">" symbol to the dropdown list
const greaterThanSymbol = document.createElement('span');
greaterThanSymbol.textContent = '>';
greaterThanSymbol.classList.add('greater-than-symbol');
dropdownCell.appendChild(greaterThanSymbol);








https://www.salesforcetroop.com/custom_file_upload_using_lwc


https://www.forcetalks.com/blog/file-upload-in-lightning-web-component-lwc-all-you-need-to-know/


https://salesforce.stackexchange.com/questions/407281/upload-file-content-via-lwc-to-apex






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
