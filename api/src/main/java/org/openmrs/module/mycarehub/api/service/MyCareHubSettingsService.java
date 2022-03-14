package org.openmrs.module.mycarehub.api.service;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.mycarehub.model.MyCareHubSetting;

import java.util.Date;

public interface MyCareHubSettingsService extends OpenmrsService {
	
	MyCareHubSetting getMyCareHubSettingByType(String settingType);
	
	MyCareHubSetting createMyCareHubSetting(String settingType, Date syncDate);
}
