package org.openmrs.module.mycarehub.utils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.mycarehub.api.rest.RestApiService;
import org.openmrs.module.mycarehub.api.service.MyCareHubSettingsService;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.openmrs.module.mycarehub.utils.Constants.EMPTY;
import static org.openmrs.module.mycarehub.utils.Constants.GP_MYCAREHUB_API_DEFAULT_PASSWORD;
import static org.openmrs.module.mycarehub.utils.Constants.GP_MYCAREHUB_API_DEFAULT_USERNAME;
import static org.openmrs.module.mycarehub.utils.Constants.GP_MYCAREHUB_API_PASSWORD;
import static org.openmrs.module.mycarehub.utils.Constants.GP_MYCAREHUB_API_URL;
import static org.openmrs.module.mycarehub.utils.Constants.GP_MYCAREHUB_API_USERNAME;
import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.MYCAREHUB_CLIENT_REGISTRATIONS;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Context.class)
@PowerMockIgnore("javax.net.ssl.*")
public class MyCareHubUtilTest {
	
	RestApiService restApiService;
	
	AdministrationService administrationService;
	
	MyCareHubSettingsService myCareHubSettingsService;
	
	String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	
	SimpleDateFormat sf = new SimpleDateFormat(pattern);
	
	private final static String testServerUrl = "https://mycarehub-testing.savannahghi.org/kenya-emr/";
	
	private final static String username = "kenya-emr@savannahinformatics.com";
	
	private final static String password = "#kenya-EMR#";
	
	@Before
	public void setup() {
		restApiService = mock(RestApiService.class);
		administrationService = mock(AdministrationService.class);
		myCareHubSettingsService = mock(MyCareHubSettingsService.class);
		
		mockStatic(Context.class);
		when(Context.getService(MyCareHubSettingsService.class)).thenReturn(myCareHubSettingsService);
		
		when(Context.getAdministrationService()).thenReturn(administrationService);
		when(administrationService.getGlobalProperty(GP_MYCAREHUB_API_URL, EMPTY)).thenReturn(testServerUrl);
		when(administrationService.getGlobalProperty(GP_MYCAREHUB_API_USERNAME, GP_MYCAREHUB_API_DEFAULT_USERNAME))
		        .thenReturn(username);
		when(administrationService.getGlobalProperty(GP_MYCAREHUB_API_PASSWORD, GP_MYCAREHUB_API_DEFAULT_PASSWORD))
		        .thenReturn(password);
	}
	
	@Test
	public void getNewMyCareHubClientCccIdentifiers_shouldCreateCorrectSyncTimeSetting() {
		Date lastSyncTime = new Date(10000);
		Date newSyncTime = new Date();
		
		MyCareHubUtil.getNewMyCareHubClientCccIdentifiers(lastSyncTime, newSyncTime);
		verify(myCareHubSettingsService, times(1)).createMyCareHubSetting(MYCAREHUB_CLIENT_REGISTRATIONS, newSyncTime);
	}
}
