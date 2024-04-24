

public with sharing class ArticleEditSuggestController {
    public ApexPages.StandardController controller; 
	public Case articleCase{get;set;}
	public Attachment caseAttachment{get;set;}
    public string fileName{get;set;} 
	public transient Blob fileBody{get;set;}
	public String queueName{get;set;}
	public String planID{get;set;}
	public String planName{get;set;}
	public String planNumber{get;set;}
	public String pageType{get;set;}
	public String market{get;set;}
	public boolean formSaved{get;set;}
	public String profileName{get;set;}
	
    public ArticleEditSuggestController(ApexPages.StandardController con){
        this.articleCase = new Case();
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
    
    public pagereference save(){
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
