package org.openmrs.module.mycarehub.api.db;

import org.openmrs.module.mycarehub.model.MyCareHubSetting;

public interface MyCareHubSettingsDao {
	
	MyCareHubSetting getMyCareHubSettingByType(String settingType);
	
	MyCareHubSetting saveOrUpdateMyCareHubSetting(MyCareHubSetting myCareHubSetting);
}
