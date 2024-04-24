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
