package org.openmrs.module.mycarehub.api.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.mycarehub.model.MyCareHubSetting;
import org.openmrs.test.BaseModuleContextSensitiveTest;

import java.util.Date;

import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.KENYAEMR_PATIENT_REGISTRATIONS;

public class MyCareHubSettingServiceTest extends BaseModuleContextSensitiveTest {
	
	MyCareHubSettingsService settingsService = null;
	
	@Before
	public void setup() throws Exception {
		settingsService = Context.getService(MyCareHubSettingsService.class);
		initializeInMemoryDatabase();
		authenticate();
	}
	
	@Test
	public void createMyCareHubSetting_shouldCreateSettingOfMatchingTypeAndDate() {
		Date syncDate = new Date();
		MyCareHubSetting myCareHubSetting = settingsService.createMyCareHubSetting(KENYAEMR_PATIENT_REGISTRATIONS, syncDate);
		Assert.assertEquals(syncDate, myCareHubSetting.getLastSyncTime());
		Assert.assertEquals(KENYAEMR_PATIENT_REGISTRATIONS, myCareHubSetting.getSettingType());
	}
	
	//	@Test
	//	public void getLatestSettingForLocalPatientRegistrations_shouldGetLatestSettingOfMatchingType() {
	//
	//	}
}
