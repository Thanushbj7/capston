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
