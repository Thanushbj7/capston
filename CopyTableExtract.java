public with sharing class ArticleEditSuggestController {
    @AuraEnabled public Case articleCase { get; set; }
    @AuraEnabled public Attachment caseAttachment { get; set; }
    @AuraEnabled public String fileName { get; set; }
    @AuraEnabled public transient Blob fileBody { get; set; }
    @AuraEnabled public String queueName { get; set; }
    @AuraEnabled public String planID { get; set; }
    @AuraEnabled public String planName { get; set; }
    @AuraEnabled public String planNumber { get; set; }
    @AuraEnabled public String pageType { get; set; }
    @AuraEnabled public String market { get; set; }
    @AuraEnabled public Boolean formSaved { get; set; }
    @AuraEnabled public String profileName { get; set; }

    public ArticleEditSuggestController() {
        this.articleCase = new Case();
        this.pageType = ApexPages.currentPage().getParameters().get('pageType');
        Profile p = [SELECT Name FROM Profile WHERE Id = :UserInfo.getProfileId()];
        this.profileName = p.Name;
        System.debug('pageType: ' + this.pageType);

        if (this.pageType != null && this.pageType == 'PAAG') {
            RecordType caseRecType = [SELECT Id, Name, SObjectType FROM RecordType WHERE SObjectType = 'Case' AND Name = 'PAAG Modification Request' LIMIT 1];
            this.articleCase.RecordTypeId = caseRecType.Id;
            this.planID = ApexPages.currentPage().getParameters().get('planId');
            System.debug('planId: ' + this.planID);
            String paagId = ApexPages.currentPage().getParameters().get('paagID');
            System.debug('paagID: ' + paagId);
            this.planNumber = ApexPages.currentPage().getParameters().get('planNumber');
            System.debug('planNumber: ' + this.planNumber);
            this.planName = ApexPages.currentPage().getParameters().get('planName');
            this.market = ApexPages.currentPage().getParameters().get('market');
            System.debug('planName: ' + this.planName);
            this.articleCase.Plan_ID__c = this.planID;
            this.articleCase.Subject = 'PAAG Edit Request';
            this.articleCase.Article_Url__c = 'https://' + ApexPages.currentPage().getHeaders().get('Host') + '/' + paagId;
            this.queueName = 'PAAG & Article Edit Request Queue';
        } else {
            RecordType caseRecType = [SELECT Id, Name, SObjectType FROM RecordType WHERE SObjectType = 'Case' AND Name = 'CCC KM Article Update Request' LIMIT 1];
            this.articleCase.RecordTypeId = caseRecType.Id;
            this.articleCase.Article_Name__c = ApexPages.currentPage().getParameters().get('articleName');
            System.debug('Article Name: ' + this.articleCase.Article_Name__c);
            String articleType = ApexPages.currentPage().getParameters().get('articleType');
            System.debug('Article Type: ' + articleType);
            if ('Job_Aid__kav'.equals(articleType)) {
                this.queueName = 'Job Aid Article Edit Queue';
                this.articleCase.Subject = 'Job Aid Edit Request';
            } else {
                this.queueName = 'PAAG & Article Edit Request Queue';
                this.articleCase.Subject = 'Plan Article Edit Request';
            }
            articleType = articleType.substring(0, articleType.length() - 5);
            this.articleCase.Article_Type__c = articleType;
            this.articleCase.Article_Url__c = 'https://' + ApexPages.currentPage().getHeaders().get('Host') + '/articles/' + articleType + '/' + this.articleCase.Article_Name__c;
        }
        this.articleCase.Status = 'New';
        ApexPages.currentPage().getHeaders().put('X-UA-Compatible', 'IE=edge');
    }

    @AuraEnabled
    public void save() {
        User user = [SELECT Id, Name, UserRole.Name FROM User WHERE Id = :UserInfo.getUserId()];
        this.articleCase.User_Role__c = user.UserRole.Name;
        System.debug('QueueName: ' + this.queueName);
        List<Group> groupList = [SELECT Id FROM Group WHERE Name = :this.queueName AND Type = 'Queue'];
        System.debug('Queue List Size: ' + groupList.size());
        if (groupList.size() > 0) {
            this.articleCase.OwnerId = groupList.get(0).Id;
        }

        if (String.isBlank(this.articleCase.Id)) {
            insert this.articleCase;
        }
        if (this.fileBody != null) {
            Attachment attachment = new Attachment();
            attachment.Body = this.fileBody;
            attachment.Name = this.fileName;
            attachment.ParentId = this.articleCase.Id;
            insert attachment;
        }
        this.fileBody = null;
        this.formSaved = true;
    }
          }
