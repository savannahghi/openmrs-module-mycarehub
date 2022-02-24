package org.openmrs.module.mycarehub.api.service;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.mycarehub.model.MyCareHubSetting;

public interface MyCareHubSettingsService extends OpenmrsService {
	
	MyCareHubSetting getMyCareHubSettingByType(String settingType);
	
	MyCareHubSetting saveMyCareHubSettings(MyCareHubSetting myCareHubSetting);
}
