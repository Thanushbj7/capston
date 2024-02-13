Map<String ,GenericWithdrawal__c> GWcustomMap = new Map<String ,GenericWithdrawal__c>();
            for(GenericWithdrawals__c  GWCustomSetting : GenericWithdrawals__c.getall().values()){
              if(GWCustomSetting.API_Name__c!= null){
                GWSettingMap.put(GWCustomSetting.API_Name__c,GWCustomSetting);
               system.debug('venki 53'+GWSettingMap);
            }
       
            }
