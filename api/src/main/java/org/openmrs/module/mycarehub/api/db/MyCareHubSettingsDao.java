package org.openmrs.module.mycarehub.api.db;

import org.openmrs.module.mycarehub.model.MyCareHubSetting;

/**
 * Handles the storage and retrieval of the myCareHub setting.
 */
public interface MyCareHubSettingsDao {
	/**
	 * Gets the most recent myCareHub setting by type.
	 * @param settingType The type of setting to retrieve.
	 * @return
	 */
	MyCareHubSetting getMyCareHubSettingByType(String settingType);

	/**
	 * Updates the myCareHub setting.
	 * @param myCareHubSetting
	 * @return the saved setting
	 */
	MyCareHubSetting saveOrUpdateMyCareHubSetting(MyCareHubSetting myCareHubSetting);
}
