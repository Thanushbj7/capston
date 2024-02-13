global static ResponseWrapper populatePAAG() {
        System.debug('POPULATE PAAG');
        
        //For testing puposed the XML Data is read from Static resource instead pulling from request post
        //StaticResource sr = [Select Body From StaticResource Where Name = 'SETITDATA' LIMIT 1];
        RestRequest req = RestContext.request;
        
        //String xmlData = sr.body.toString();
        String xmlData = req.requestBody.toString();
        if(!String.isBlank(xmlData)){
            initSchemaFieldMap();
            
            Map<String ,PAAG__c> paagMap = new Map<String ,PAAG__c>();
            for(PAAGSetting__c  paagSetting : PAAGSetting__c.getall().values()){
                paagSettingMap.put(paagSetting.API_Name__c,paagSetting);
                
            }
           
           Map<String ,GenericWithdrawal__c> GWcustomMap = new Map<String ,GenericWithdrawal__c>();
            for(GenericWithdrawals__c  GWCustomSetting : GenericWithdrawals__c.getall().values()){
              if(GWCustomSetting.API_Name__c!= null){
                GWSettingMap.put(GWCustomSetting.API_Name__c,GWCustomSetting);
               system.debug('venki 53'+GWSettingMap);
            }
       
            }
            
           //  system.debug('PAAGpaagSettingMap>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>' +paagSettingMap.get('a2ve00000015YwNAAU'));
            system.debug('PAAG Size' +paagMap.size() );
            parseXMLData(xmlData, paagMap);
            system.debug('PAAG Size' +paagMap.size() );
           
            addPlanPAAGInfo(paagMap);
        }
        
        ResponseWrapper resWrapper = new ResponseWrapper();
        if(paagResultMap.size() > 0){
            resWrapper.resultInfos = paagResultMap.values();
        }
        return resWrapper;
        
    }

if(GWCustomSetting.API_Name__c!= null){
                GWSettingMap.put(GWCustomSetting.API_Name__c,GWCustomSetting);



@isTest
public class TestGWCustomMap {

    @isTest
    static void testNullCheck() {
        // Test scenario: Check for null values in API_Name__c field

        // Create test data with a GenericWithdrawals__c record having null API_Name__c
        GenericWithdrawals__c testGWRecord = new GenericWithdrawals__c();
        // Set other required fields as needed
        // ...

        // Insert the test record
        insert testGWRecord;

        // Ensure that the GWcustomMap is populated with non-null values
        Map<String, GenericWithdrawal__c> GWcustomMap = new Map<String, GenericWithdrawal__c>();
        for (GenericWithdrawals__c GWCustomSetting : GenericWithdrawals__c.getAll().values()) {
            if (GWCustomSetting.API_Name__c != null) {
                GWcustomMap.put(GWCustomSetting.API_Name__c, GWCustomSetting);
            }
        }

        // Perform the assertion to check that GWcustomMap contains the expected values
        System.assertEquals(0, GWcustomMap.size(), 'Null values in API_Name__c should not be added to GWcustomMap');
    }
}
