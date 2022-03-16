package org.openmrs.module.mycarehub.api.service.impl;

import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.mycarehub.api.db.MyCareHubSettingsDao;
import org.openmrs.module.mycarehub.api.service.MyCareHubSettingsService;
import org.openmrs.module.mycarehub.model.MyCareHubSetting;

import java.util.Date;

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
	public MyCareHubSetting getLatestMyCareHubSettingByType(String settingType) {
		return dao.getMyCareHubSettingByType(settingType);
	}
	
	@Override
	public MyCareHubSetting createMyCareHubSetting(String settingType, Date syncTime) {
		MyCareHubSetting setting = new MyCareHubSetting(settingType, syncTime);
		return dao.saveOrUpdateMyCareHubSetting(setting);
	}
}
