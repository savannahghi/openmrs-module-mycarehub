package org.openmrs.module.mycarehub.api.service.impl;

import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.mycarehub.api.db.MyCareHubSettingsDao;
import org.openmrs.module.mycarehub.api.service.MyCareHubSettingsService;
import org.openmrs.module.mycarehub.model.MyCareHubSetting;

public class MyCareHubSettingsServiceImpl extends BaseOpenmrsService implements MyCareHubSettingsService {
	
	MyCareHubSettingsDao dao;
	
	public MyCareHubSettingsServiceImpl(MyCareHubSettingsDao dao) {
		this.dao = dao;
	}
	
	public MyCareHubSettingsDao getDao() {
		return dao;
	}
	
	public void setDao(MyCareHubSettingsDao dao) {
		this.dao = dao;
	}
	
	@Override
	public MyCareHubSetting getMyCareHubSettingByType(String settingType) {
		return dao.getMyCareHubSettingByType(settingType);
	}
	
	@Override
	public MyCareHubSetting saveMyCareHubSettings(MyCareHubSetting myCareHubSetting) {
		return dao.saveOrUpdateMyCareHubSetting(myCareHubSetting);
	}
}
