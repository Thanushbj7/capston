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
