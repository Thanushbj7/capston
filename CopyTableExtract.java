@isTest
private class YourApexClassTest {

    @isTest
    static void positiveTest() {
        // Positive Test Case: Valid GWCustomSetting with non-null API_Name__c
        GenericWithdrawals__c validSetting = new GenericWithdrawals__c(API_Name__c = 'ValidAPIName');
        insert validSetting;

        // Call the method or trigger that utilizes the provided code segment

        // Isolate the GWSettingMap.put operation
        GWSettingMap.put(validSetting.API_Name__c, validSetting);

        // Assert that GWSettingMap is correctly populated
        System.assertEquals(expectedValue, actualValue);
    }

    @isTest
    static void negativeTest() {
        // Negative Test Case: GWCustomSetting with null API_Name__c
        GenericWithdrawals__c nullSetting = new GenericWithdrawals__c(API_Name__c = null);
        insert nullSetting;

        // Call the method or trigger that utilizes the provided code segment

        // Isolate the GWSettingMap.put operation
        GWSettingMap.put(nullSetting.API_Name__c, nullSetting);

        // Assert that GWSettingMap remains unchanged or is empty
        System.assertEquals(expectedValue, actualValue);
    }
}








@isTest
private class YourApexClassTest {

    @isTest
    static void positiveTest() {
        // Positive Test Case: Valid GWCustomSetting with non-null API_Name__c
        GenericWithdrawals__c validSetting = new GenericWithdrawals__c(API_Name__c = 'ValidAPIName');
        insert validSetting;

        // Call the method or trigger that utilizes the provided code segment

        // Assert that GWSettingMap is correctly populated
        System.assertEquals(expectedValue, actualValue);
    }

    @isTest
    static void negativeTest() {
        // Negative Test Case: GWCustomSetting with null API_Name__c
        GenericWithdrawals__c nullSetting = new GenericWithdrawals__c(API_Name__c = null);
        insert nullSetting;

        // Call the method or trigger that utilizes the provided code segment

        // Assert that GWSettingMap remains unchanged or is empty
        System.assertEquals(expectedValue, actualValue);
    }
}








static testMethod void testRest() {
            String xml= '<sfplan>'+
                '<plan><planid>102201</planid><Plan_Name_ist__c>Plan Hai par Nahi Hai</Plan_Name_ist__c><General_Loan_Detail__c>test1</General_Loan_Detail__c><Residential_Loan_Detail__c> </Residential_Loan_Detail__c><General_Minimum_Loan_Duration_in_months__c> </General_Minimum_Loan_Duration_in_months__c><Residential_Loan_Minimum__c> </Residential_Loan_Minimum__c><General_Loan_Maximum_Duration_in_months__c> The lesser of: $ or $50,000 less the highest outstanding loan balance in the past 12 months. </General_Loan_Maximum_Duration_in_months__c><Residential_Loan_Max_Duration_in_months__c> The lesser of: $SO_MAXIMUM_LOAN_AMOUNT or $50,000 less the highest outstanding loan balance in the past 12 months. </Residential_Loan_Max_Duration_in_months__c><General_Purpose_Loan_Duration__c> NA </General_Purpose_Loan_Duration__c><Residential_Loan_Duration__c> NA </Residential_Loan_Duration__c><General_Loan_Frequency__c> No Restrictions </General_Loan_Frequency__c><Residential_Loan_Frequency__c> No Restrictions </Residential_Loan_Frequency__c><Display_Local_Office_Subsection__c>true</Display_Local_Office_Subsection__c><Display_Local_Office_Subsection>  <Local_Office_Contact_Name__c>PHOENIX, AZ</Local_Office_Contact_Name__c><Local_Office_Address_Line_1__c /><Local_Office_Address_Line_2__c /><Local_Office_Address_Line_3__c /><Local_Office_City__c /> <Local_Office_State__c>AZ</Local_Office_State__c> <Local_Office_Zip__c /> <Local_Office_Phone__c /><Local_Office_Toll_Free__c /> <Local_Office_Fax__c /> </Display_Local_Office_Subsection></plan>'
                +
                '<plan><planid>2020202</planid><General_Loan_Detail__c>test2</General_Loan_Detail__c><Client_Id__c>testLMP</Client_Id__c><PAAG_Configuration__c>LMP</PAAG_Configuration__c><Residential_Loan_Detail__c> </Residential_Loan_Detail__c><General_Minimum_Loan_Duration_in_months__c> </General_Minimum_Loan_Duration_in_months__c><Residential_Loan_Minimum__c> </Residential_Loan_Minimum__c><General_Loan_Maximum_Duration_in_months__c> The lesser of: $ or $50,000 less the highest outstanding loan balance in the past 12 months. </General_Loan_Maximum_Duration_in_months__c><Residential_Loan_Max_Duration_in_months__c> The lesser of: $SO_MAXIMUM_LOAN_AMOUNT or $50,000 less the highest outstanding loan balance in the past 12 months. </Residential_Loan_Max_Duration_in_months__c><General_Purpose_Loan_Duration__c> NA </General_Purpose_Loan_Duration__c><Residential_Loan_Duration__c> NA </Residential_Loan_Duration__c><General_Loan_Frequency__c> No Restrictions </General_Loan_Frequency__c><Residential_Loan_Frequency__c> No Restrictions </Residential_Loan_Frequency__c><Display_Rep_Information_Subsection__c>true</Display_Rep_Information_Subsection__c><Display_Rep_Information_Subsection><Rep_Name__c>WILLIAM (BILL) DIANA</Rep_Name__c><Rep_Role__c>BROKER</Rep_Role__c><Rep_Local_Phone__c>201-845-6600</Rep_Local_Phone__c><Rep_Toll_Free__c /><Rep_Cell_Phone__c /><Rep_Email__c>BDIANA@MMA-NE.COM</Rep_Email__c><Rep_Pass_Code__c /><Rep_Access_Code__c>160214</Rep_Access_Code__c></Display_Rep_Information_Subsection></plan>'
                +
                '<plan><planid>6060606</planid><General_Loan_Detail__c>test2</General_Loan_Detail__c><Residential_Loan_Detail__c> </Residential_Loan_Detail__c><General_Minimum_Loan_Duration_in_months__c> </General_Minimum_Loan_Duration_in_months__c><Residential_Loan_Minimum__c> </Residential_Loan_Minimum__c><General_Loan_Maximum_Duration_in_months__c> The lesser of: $ or $50,000 less the highest outstanding loan balance in the past 12 months. </General_Loan_Maximum_Duration_in_months__c><Residential_Loan_Max_Duration_in_months__c> The lesser of: $SO_MAXIMUM_LOAN_AMOUNT or $50,000 less the highest outstanding loan balance in the past 12 months. </Residential_Loan_Max_Duration_in_months__c><General_Purpose_Loan_Duration__c> NA </General_Purpose_Loan_Duration__c><Residential_Loan_Duration__c> NA </Residential_Loan_Duration__c><General_Loan_Frequency__c> No Restrictions </General_Loan_Frequency__c><Residential_Loan_Frequency__c> No Restrictions </Residential_Loan_Frequency__c></plan>'
                +
                '</sfplan>';
     
        RestRequest req = new RestRequest(); // Build the REST Request for testing
       
        req.addHeader('Content-Type', 'application/xml'); // Add a JSON Header as it is validated
        req.requestURI = '/services/apexrest/SETIT-Conversion/*';  
        req.httpMethod = 'POST';        // Perform a POST
        req.requestBody = Blob.valueof(xml); // Add JSON Message as a POST
        
        RestResponse res = new RestResponse();
        RestContext.request = req;
        RestContext.response = res;
        
        PAAGSetting__c setting= new PAAGSetting__c(Name='AAME',API_Name__c='AAME__c',Object__c='PAAG__c',Value_1__c='test',Value_2__c='test',Value_LMP_2__c='test',Value_LMP_3__c='test',Value_LMP_4__c='test',Value_LMP_5__c='test',Value_LMP__c='test');        
        insert setting;

        SetITDataConversion.populatePAAG();
       
        
    }







@isTest
public class TestPopulatePAAG {

    @isTest
    static void testGWSettingMapPopulation() {
        // Test scenario: Check if GWSettingMap is populated correctly

        // Create test data with a GenericWithdrawals__c record having non-null API_Name__c
        GenericWithdrawals__c testGWRecord = new GenericWithdrawals__c(API_Name__c = 'TestAPIName');
        // Set other required fields as needed
        // ...

        // Insert the test record
        insert testGWRecord;

        // Call the populatePAAG method
        Test.startTest();
        // Assuming populatePAAG is in a class named MyClass
        MyClass.populatePAAG();
        Test.stopTest();

        // Ensure that GWSettingMap is populated with the expected values
        // Adjust the assertions based on your specific requirements
        System.assertEquals(1, MyClass.GWSettingMap.size(), 'GWSettingMap should be populated with one record');
        System.assertEquals(testGWRecord, MyClass.GWSettingMap.get('TestAPIName'), 'GWSettingMap should contain the correct record');
    }
}







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
