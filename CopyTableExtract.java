Map<String ,GenericWithdrawal__c> GWcustomMap = new Map<String ,GenericWithdrawal__c>();
            for(GenericWithdrawals__c  GWCustomSetting : GenericWithdrawals__c.getall().values()){
              if(GWCustomSetting.API_Name__c!= null){
                GWSettingMap.put(GWCustomSetting.API_Name__c,GWCustomSetting);
               system.debug('venki 53'+GWSettingMap);
            }
       
            }



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
