@AuraEnabled(cacheable=true)
    public static String getProfileId(){
        List<ProfileNames__c> profileIds= ProfileNames__c.getAll().values();
        for(ProfileNames__c profileId:profileIds){
            
        }
        return profileIds[0].Profile_Id__c;
    }




@isTest
static void testGetQuickLinks() {
    // Create mock Console Quick Links records
    List<Console_Quick_Links__c> quickLinks = new List<Console_Quick_Links__c>{
        new Console_Quick_Links__c(
            Name = 'Quick Link 1', // Example quick link name
            // Add more fields as needed
        ),
        new Console_Quick_Links__c(
            Name = 'Quick Link 2', // Example quick link name
            // Add more fields as needed
        )
        // Add more mock records as needed
    };
    insert quickLinks;

    // Call the method being tested
    List<Console_Quick_Links__c> result = YourClassName.getQuickLinks();

    // Perform assertions
    System.assertEquals(2, result.size()); // Assuming two quick links are returned based on the mock data
    // Add more assertions as needed
}






@AuraEnabled(cacheable=true)
    public static  List<Console_Quick_Links__c> getQuickLinks(){
        List<Console_Quick_Links__c> quickLinks= Console_Quick_Links__c.getAll().values();
      
        return quickLinks;
    }



@isTest
static void testGetOpenClientOffers() {
    // Create a mock Campaign Offer Summary record
    Campaign_Offer_Summary__c offerSummary = new Campaign_Offer_Summary__c(
        Customer_SSN__c = '123456789', // Example SSN
        OfferCode__c = 'OFFER001', // Example Offer Code
        Planid_Text__c = 'Plan123' // Example Plan ID
        // Add more fields as needed
    );
    insert offerSummary;

    // Create a mock Client Offer record
    Client_Offer__c clientOffer = new Client_Offer__c(
        Status_OFFER001__c = 'Open', // Example status for the specific offer code
        Score_OFFER001__c = 80, // Example score for the specific offer code
        PlanId_OFFER001__c = 'Plan123' // Example Plan ID
        // Add more fields as needed
    );
    insert clientOffer;

    // Create a mock Campaign record
    Campaign campaign = new Campaign(
        Name = 'Campaign123', // Example campaign name
        Offer_Code__c = 'OFFER001', // Example Offer Code
        Offer_Priority__c = 1 // Example Offer Priority
        // Add more fields as needed
    );
    insert campaign;

    // Call the method being tested
    List<vfClientOffer> offers = YourClassName.getOpenClientOffers('123456789'); // Pass the SSN used in the test data

    // Perform assertions
    System.assertEquals(1, offers.size()); // Assuming only one offer is returned based on the mock data
    vfClientOffer offer = offers[0];
    System.assertEquals('OFFER001', offer.offerCode); // Verify offer code
    System.assertEquals('Campaign123', offer.offerName); // Verify offer name
    System.assertEquals(1, offer.offerPriority); // Verify offer priority
    System.assertEquals(80, offer.offerScore); // Verify offer score
    System.assertEquals('Plan123', offer.offerPlanId); // Verify offer plan ID
    // Add more assertions as needed
}






private static List<vfClientOffer> getOpenClientOffers(String ssn) {
        List<vfClientOffer> vfClientOfferList = new List<vfClientOffer>();
        
        List<Client_Offer__c> clientOpenOfferList = dynamicClientOfferQuery(new Set<String>{ssn});
        if(clientOpenOfferList == null || clientOpenOfferList.size() == 0)
            return vfClientOfferList;
        
        Client_Offer__c co = clientOpenOfferList[0];
        
        Map<String, Campaign_Offer_Summary__c> campaignOfferDoNotShowOfferMap = getCampaignOfferDoNotShowOfferMap(new Set<String>{ssn});
        
        System.debug('co ::::: ' + co);
        String planId = null, key = null;
        Campaign_Offer_Summary__c offerSummary = null;
        for (Campaign c : [select id, name, offer_code__c, offer_priority__c from campaign where offer_code__c != null limit 50]) {
            try { 
                //Check data in Campaign Offer Summary Object
                //Match on PlanID and Offer Code
                
                planId = (String)co.get('PlanId_' + c.offer_code__c + '__c');
                key = ssn + ConstantUtils.UNIQUE_SEPERATOR + planId + ConstantUtils.UNIQUE_SEPERATOR + c.offer_code__c;
                
                offerSummary = campaignOfferDoNotShowOfferMap.get(key);
                //Case# 16921 && Case# 11899 - Rahul Sahay - 07/17/2013 
                if(offerSummary != null && planId == offerSummary.Planid_Text__c && c.offer_code__c == offerSummary.OfferCode__c)
                        continue;
                
                if (((String)co.get('status_' + c.offer_code__c + '__c') == 'Open') && ((Decimal)co.get('score_' + c.offer_code__c + '__c') != null)) {
                    VfClientOffer vf = new VfClientOffer();
                    vf.offerCode = c.offer_code__c;
                    vf.offerName = c.name;                    
                    vf.offerPriority = c.offer_priority__c;
                    vf.offerScore = (Decimal)co.get('score_' + c.offer_code__c + '__c');
                    vf.offerPlanId = (String)co.get('planid_' + c.offer_code__c + '__c');
                    vf.offerPlanName = (String)co.get('PlanName_' + c.offer_code__c + '__c');
                    
                    //Case # 00011325: Added new field to show the Avtive Mailer information on the Targeted Messages list on the Offer Page.
                    vf.activeMailer = (String)co.get('Active_Mailer_' + c.offer_code__c + '__c');
                   
                    vf.offerCampaign = c.id;
                    vfClientOfferList.add(vf);//---this is need to display in UI
                }
            } catch (Exception e) {
                // this exception means the user has inserted a campaign with an offer_code__c but has not inserted the corresponding fields on the client_offer__c object
                // swallow 
            }
        }        
        
        System.debug('vfClientOfferList :::: ' + vfClientOfferList);
        
        return vfClientOfferList;
    }










@isTest
static void testCaseWrapperInitialization() {
    // Create a mock Case record
    Case caseRecord = new Case(
        CaseNumber = 'CAS-001',
        Description = 'Test Description'
        // Add more fields as needed
    );
    insert caseRecord;

    // Create a mock Case_Actions__c record and associate it with the Case record
    Case_Actions__c caseAction = new Case_Actions__c(
        Case__c = caseRecord.Id,
        Date_Time_c__c = Datetime.now(),
        PlanID_Text__c = 'Plan123',
        Call_Activity__c = 'Inquiry',
        Call_Type__c = 'TypeA'
        // Add more fields as needed
    );
    insert caseAction;

    // Debug statement to check the value of caseAction
    System.debug('Value of caseAction: ' + caseAction);

    // Call the constructor of CaseWrapper class with the mock Case_Actions__c record
    CaseWrapper wrapper = new CaseWrapper(caseAction);

    // Verify that the wrapper fields are correctly populated
    System.assertEquals(caseRecord.CaseNumber, wrapper.caseNumber);
    System.assertEquals(caseRecord.Description, wrapper.comment);
    System.assertEquals('Plan123', wrapper.planId);
    System.assertEquals('TypeA', wrapper.callTypeInquiry);

    // Add more assertions for other scenarios and fields
}






@isTest
public class CaseWrapperTest {

    @isTest
    static void testCaseWrapperInitialization() {
        // Create a mock Case record
        Case caseRecord = new Case(
            CaseNumber = 'CAS-001',
            Description = 'Test Description'
            // Add more fields as needed
        );
        insert caseRecord;

        // Create a mock Case_Actions__c record and associate it with the Case record
        Case_Actions__c caseAction = new Case_Actions__c(
            Case__c = caseRecord.Id,
            Date_Time_c__c = Datetime.now(),
            PlanID_Text__c = 'Plan123',
            Call_Activity__c = 'Inquiry',
            Call_Type__c = 'TypeA'
            // Add more fields as needed
        );
        insert caseAction;

        // Call the constructor of CaseWrapper class with the mock Case_Actions__c record
        CaseWrapper wrapper = new CaseWrapper(caseAction);

        // Verify that the wrapper fields are correctly populated
        System.assertEquals(caseRecord.CaseNumber, wrapper.caseNumber);
        System.assertEquals(caseRecord.Description, wrapper.comment);
        System.assertEquals('Plan123', wrapper.planId);
        System.assertEquals('TypeA', wrapper.callTypeInquiry);

        // Add more assertions for other scenarios and fields
    }
}







@isTest
public class CaseWrapperTest {

    @isTest
    static void testCaseWrapperInitialization() {
        // Create a mock Case record
        Case caseRecord = new Case(
            CaseNumber = 'CAS-001',
            Description = 'Test Description'
            // Add more fields as needed
        );
        insert caseRecord;

        // Create a mock Case_Actions__c record and associate it with the Case record
        Case_Actions__c caseAction = new Case_Actions__c(
            Case__c = caseRecord.Id,
            Date_Time_c__c = Datetime.now(),
            PlanID_Text__c = 'Plan123',
            Call_Activity__c = 'Inquiry',
            Call_Type__c = 'TypeA'
            // Add more fields as needed
        );
        insert caseAction;

        // Call the constructor of CaseWrapper class with the mock Case_Actions__c record
        CaseWrapper wrapper = new CaseWrapper(caseAction);

        // Verify that the wrapper fields are correctly populated
        // Note: Since CaseNumber is not accessible, we'll skip asserting its value
        System.assertEquals(caseAction.Date_Time_c__c.date(), wrapper.createdDate);
        System.assertEquals('Plan123', wrapper.planId);
        System.assertEquals(caseRecord.Id, wrapper.Id);
        System.assertEquals('Test Description', wrapper.comment);
        System.assertEquals('TypeA', wrapper.callTypeInquiry);

        // Add more assertions for other scenarios and fields
    }
}






@isTest
public class CaseWrapperTest {

    @isTest
    static void testCaseWrapperInitialization() {
        // Create a mock Case_Actions__c record
        Case_Actions__c caseAction = new Case_Actions__c(
            // Add necessary fields, but exclude CaseNumber
            Date_Time_c__c = Datetime.now(),
            PlanID_Text__c = 'Plan123',
            Call_Activity__c = 'Inquiry',
            Call_Type__c = 'TypeA'
            // Add more fields as needed
        );
        // Insert the mock record
        insert caseAction;

        // Create a mock Case record related to Case_Actions__c
        Case caseRecord = new Case(
            CaseNumber = 'CAS-001', 
            Description = 'Test Description'
            // Add more fields as needed
        );
        // Associate the mock Case record with the Case_Actions__c record
        caseAction.Case__c = caseRecord.Id;
        update caseAction;

        // Call the constructor of CaseWrapper class with the mock Case_Actions__c record
        CaseWrapper wrapper = new CaseWrapper(caseAction);

        // Verify that the wrapper fields are correctly populated
        // Note: Since CaseNumber is not accessible, we'll skip asserting its value
        System.assertEquals(caseAction.Date_Time_c__c.date(), wrapper.createdDate);
        System.assertEquals('Plan123', wrapper.planId);
        System.assertEquals(caseAction.Case__r.Id, wrapper.Id);
        System.assertEquals('Test Description', wrapper.comment);
        System.assertEquals('TypeA', wrapper.callTypeInquiry);

        // Add more assertions for other scenarios and fields
    }
}






@isTest
public class CaseWrapperTest {

    @isTest
    static void testCaseWrapperInitialization() {
        // Create test data
        Case_Actions__c caseAction = new Case_Actions__c(
            Case__r = new Case(CaseNumber = 'CAS-001', Description = 'Test Description'),
            Date_Time_c__c = Datetime.now(),
            PlanID_Text__c = 'Plan123',
            Call_Activity__c = 'Inquiry',
            Call_Type__c = 'TypeA'
            // Add more fields as needed
        );
        insert caseAction;

        // Call the constructor of CaseWrapper class with test data
        CaseWrapper wrapper = new CaseWrapper(caseAction);

        // Verify that the wrapper fields are correctly populated
        System.assertEquals('CAS-001', wrapper.caseNumber);
        System.assertEquals(caseAction.Date_Time_c__c.date(), wrapper.createdDate);
        System.assertEquals('Plan123', wrapper.planId);
        System.assertEquals(caseAction.Case__r.Id, wrapper.Id);
        System.assertEquals('Test Description', wrapper.comment);
        System.assertEquals('TypeA', wrapper.callTypeInquiry);

        // Add more assertions for other scenarios and fields
    }
}



@isTest
public class CaseWrapperTest {

    @isTest
    static void testCaseWrapperInitialization() {
        // Create test data
        Case_Actions__c caseAction = new Case_Actions__c(
            // Populate required fields
        );
        insert caseAction;
        
        // Call the constructor of CaseWrapper class
        CaseWrapper wrapper = new CaseWrapper(caseAction);
        
        // Verify that the wrapper fields are correctly populated
        System.assertEquals(caseAction.Case__r.CaseNumber, wrapper.caseNumber);
        // Repeat for other fields as necessary
    }
}






public class CaseWrapper{
        @AuraEnabled public string caseNumber;
        @AuraEnabled  public date createdDate;
        @AuraEnabled public string planId;
        @AuraEnabled public string callTypeInquiry;
        @AuraEnabled public string callTypeTransaction;
        @AuraEnabled public string callTypeAccountMaintenance;
        @AuraEnabled public string callTypeForms;
        @AuraEnabled public string callTypeOthers;
        @AuraEnabled public string Id;
        @AuraEnabled public string comment;
        
        public CaseWrapper(Case_Actions__c ca){
            this.caseNumber=ca.Case__r.CaseNumber;
            Date dt= ca.Date_Time_c__c.date();
            this.createdDate=dt;
            this.planId=ca.PlanID_Text__c;//ca.PlanID__r.Name;
            this.Id=ca.Case__r.Id;
            this.comment=ca.Case__r.Description;
            
            
            if(ca.Call_Activity__c=='Inquiry' && ca.Call_Type__c !=''){
                this.callTypeInquiry = ca.Call_Type__c ;
                
            } 
            if(ca.Call_Activity__c=='Transaction' && ca.Call_Type__c !=''){
                this.callTypeTransaction = ca.Call_Type__c ;
                
            } 
            if(ca.Call_Activity__c=='Account Maintenance' && ca.Call_Type__c !=''){
                this.callTypeAccountMaintenance = ca.Call_Type__c ;
                
            } 
            if(ca.Call_Activity__c=='Forms' && ca.Call_Type__c !=''){
                this.callTypeForms = ca.Call_Type__c ;
                
            } 
            if((ca.Call_Activity__c=='Hand-Off Case' || ca.Call_Activity__c=='NIGO Callback') && ca.Call_Type__c !=''){
                this.callTypeOthers = ca.Call_Type__c ;
                
            } 
       }
   }




import static org.mockito.Mockito.*;

import java.util.Calendar;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class BalanceHistoryCacheTest {

    @Test
    public void testAccountAge24Months() {
        ParticipantKey key = mock(ParticipantKey.class);
        ParticipantInfo participantInfo = mock(ParticipantInfo.class);
        when(participantInfo.getParticipantAccountAgeInMonths()).thenReturn(24);
        when(ParticipantInfo.getInstance(any(), any(), any())).thenReturn(participantInfo);
        
        BalanceHistoryCache balanceHistoryCache = new BalanceHistoryCache(key);
        Calendar startDate = balanceHistoryCache.getStartDate();
        Assert.assertNotNull(startDate);
        startDate.add(Calendar.YEAR, 2);
        Assert.assertEquals(Calendar.getInstance().get(Calendar.YEAR), startDate.get(Calendar.YEAR));
    }

    @Test
    public void testAccountAge12Months() {
        // Similar test case as above but with account age 12 months
    }

    @Test
    public void testAccountAge6Months() {
        // Similar test case as above but with account age 6 months
    }

    @Test
    public void testAccountAge3Months() {
        // Similar test case as above but with account age 3 months
    }

    @Test
    public void testAccountAgeLessThan3Months() {
        // Similar test case as above but with account age less than 3 months
    }

    @Test(expected = BusinessException.class)
    public void testExceptionDuringServiceInvocation() {
        // Mocking the service to throw BusinessException
    }
}










package com.ing.rs.i401k.business.util;

import java.util.Calendar;
import java.util.Collections;
import java.util.Map;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;
import citistreet.id.services.cache.AbstractObjectCacheElementFactory;
import citistreet.id.services.cache.CacheConfigUtils;
import citistreet.logging.Log;
import citistreet.logging.LogFactory;

import com.citistreet.id.i401k.business.util.BusinessException;
import com.citistreet.id.i401k.business.util.ParticipantInfo;
import com.citistreet.id.i401k.business.util.ParticipantKey;
import com.citistreet.sol.util.SolServices;
import com.ing.cbc.services.CbcServices;

import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import edu.umd.cs.findbugs.annotations.NonNull;
import voya.util.cache.annotation.CacheConfiguration;
import com.voya.bridge.dao.ParticipantHistoryBridgeDAO;
import com.citistreet.id.i401k.business.util.*;
import org.apache.commons.lang.StringUtils;
import citistreet.id.services.*;
/**
 * Cache class that calls the SolService with a duration and caches the response.
 * The duration would be 3m,6m,1yr,2yr. 
 */
@Immutable
@DefaultAnnotation(NonNull.class)
public class BalanceHistoryCache implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private static final Log LOGGER = LogFactory.getLog(BalanceHistoryCache.class);
    private static final Factory FACTORY = new Factory();
	private Calendar startDate;
	private Calendar endDate;
    private Map<Integer, BalanceHistoryData> balanceHistory;
	
	@ThreadSafe
	@DefaultAnnotation(NonNull.class)
    @CacheConfiguration("pcf.cache.transition.participant.config")
	private static final class Factory extends AbstractObjectCacheElementFactory<ParticipantKey, BalanceHistoryCache> {
		private Factory() {
			init(CacheConfigUtils.getCacheConfig(CbcServices.CB_PARTICIPANT_CACHE_CONFIG));
		}

		@Override
		public BalanceHistoryCache createInstance(final ParticipantKey key) {
			return new BalanceHistoryCache(key);
		}
	}

    public static BalanceHistoryCache getInstance(final ParticipantKey key){
        if (key == null) {
            throw new IllegalArgumentException("Invalid Participant Key");
        }

        return FACTORY.getInstance(key);
    }
	
	/**
	 * Private constructor
	 * @param key
	 */
	private BalanceHistoryCache(final ParticipantKey key) {
		ParticipantInfo partInfo = ParticipantInfo.getInstance(key.getClient(),key.getPlan(),key.getParticipant());
		int pptAcctAge = partInfo.getParticipantAccountAgeInMonths();
		startDate = Calendar.getInstance();
		endDate = Calendar.getInstance(); 
		if(pptAcctAge >= 24) {
			if(LOGGER.isDebugEnabled()) {
				LOGGER.debug("Participant's {0} account age is >= 24 months, making a call to SOL service with 2yr duration...",key.getParticipant());
			}
			startDate.add(Calendar.YEAR, -2);
		} else if(pptAcctAge >= 12  ) {
			if(LOGGER.isDebugEnabled()) {
				LOGGER.debug("Participant's {0} account age is >=12 months, making a call to SOL service with 1yr duration...",key.getParticipant());
			}
			startDate.add(Calendar.YEAR, -1);
		} else if(pptAcctAge >= 6 ) {
			if(LOGGER.isDebugEnabled()){
				LOGGER.debug("Participant's {0} account age is >=6 months,  making a call to SOL service with 6 months duration...",key.getParticipant());
			}
			startDate.add(Calendar.MONTH, -(6));
		} else if (pptAcctAge >= 3) {
			if(LOGGER.isDebugEnabled()) {
				LOGGER.debug("Participant's {0} account age is >=3 months,  making a call to SOL service with 6 months duration...",key.getParticipant());
			}
			startDate.add(Calendar.MONTH, -(3));
		} else {
			startDate = null;
			endDate = null;
		}

        if (startDate == null && endDate == null) {
            //Participant's account is lessThan three months old.. Return empty Map.
            balanceHistory = Collections.emptyMap();
        } else {
            try {
				PlanInfo pi = PlanInfo.getInstance(key.getClient(), key.getPlan());
				PlanControl pc = pi.getPlanControl(ControlType.WEB);
				boolean isPlatformUpgradeEnabled = StringUtils.equalsIgnoreCase("B", pc.getPlatformUpgradeFlag());
				boolean bridgeappEnabledKey = StringUtils.equals("true",System.getProperty("ppt.bridgeapp.enabled"));

				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("isPlatformUpgradeEnabled"+isPlatformUpgradeEnabled);
					LOGGER.debug("bridgeappEnabledKey"+bridgeappEnabledKey);
				}
				if (bridgeappEnabledKey && isPlatformUpgradeEnabled) {
					ServiceManager serviceManager = ServiceManagerFactory.getInstance();
					ParticipantHistoryBridgeDAO participantHistBridgeClient = serviceManager.getService(ServiceID.valueOf("participant.history.bridge.dao"));
					balanceHistory = participantHistBridgeClient.getBalanceHistoryData(key.getClient(), key.getPlan(), key.getParticipant(),startDate,endDate);
				}else {

					SolServices solService = SolServices.getInstance(key.getClient(), key.getPlan(), key.getParticipant());
					balanceHistory = solService.getBalanceHistoryData(startDate, endDate);
				}
            } catch (BusinessException solException) {
                if (LOGGER.isErrorEnabled()) {
                    LOGGER.error("Exception occured for Balance History Sol Service....", solException);
                }
                throw solException;
            }
        }
	}

	/**
	 * Returns the BalanceHistoryData sorted Map
	 * @return Map<Integer,BalanceHistoryData>
	 */
	public Map<Integer, BalanceHistoryData> getBalanceHistoryData() {
		return balanceHistory;
	}
	
	public Calendar getStartDate() {
		return this.startDate;
	}
	
	public Calendar getEndDate() {
		return this.endDate;
	}
}
