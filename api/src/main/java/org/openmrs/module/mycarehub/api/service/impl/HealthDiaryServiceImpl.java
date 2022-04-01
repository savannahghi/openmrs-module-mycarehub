package org.openmrs.module.mycarehub.api.service.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.mycarehub.api.db.HealthDiaryDao;
import org.openmrs.module.mycarehub.api.service.HealthDiaryService;
import org.openmrs.module.mycarehub.api.service.MyCareHubSettingsService;
import org.openmrs.module.mycarehub.model.HealthDiary;
import org.openmrs.module.mycarehub.model.MyCareHubSetting;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.PATIENT_HEALTH_DIARY_GET;
import static org.openmrs.module.mycarehub.utils.MyCareHubUtil.getPatientHealthDiaries;

public class HealthDiaryServiceImpl extends BaseOpenmrsService implements HealthDiaryService {
	
	private static final Log log = LogFactory.getLog(HealthDiaryServiceImpl.class);
	
	private HealthDiaryDao dao;
	
	//ToDo: Move date formats to constants
	private final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	public HealthDiaryServiceImpl(HealthDiaryDao dao) {
		this.dao = dao;
	}
	
	@Override
	public List<HealthDiary> saveHealthDiaries(List<HealthDiary> healthDiary) {
		return dao.saveHealthDiaries(healthDiary);
	}
	
	@Override
	public Number countHealthDiaries() {
		return dao.countHealthDiaries();
	}
	
	@Override
	public List<HealthDiary> getPagedHealthDiaries(Integer pageNumber, Integer pageSize) {
		return dao.getPagedHealthDiaries(pageNumber, pageSize);
	}
	
	@Override
	public void fetchPatientHealthDiaries() {
		MyCareHubSettingsService settingsService = Context.getService(MyCareHubSettingsService.class);
		MyCareHubSetting setting = settingsService.getLatestMyCareHubSettingByType(PATIENT_HEALTH_DIARY_GET);
		if (setting != null) {
			Date newSyncDate = new Date();
			
			JsonArray jsonArray = getPatientHealthDiaries(setting.getLastSyncTime(), newSyncDate);
			List<HealthDiary> healthDiaries = new ArrayList<HealthDiary>();
			if (jsonArray != null) {
				for (int i = 0; i < jsonArray.size(); i++) {
					JsonObject jsonObject1 = jsonArray.get(i).getAsJsonObject();
					HealthDiary healthDiary = new HealthDiary();
					
					healthDiary.setCccNumber(jsonObject1.get("CCCNumber").toString());
					healthDiary.setMood(jsonObject1.get("mood").toString());
					healthDiary.setNote(jsonObject1.get("note").toString());
					healthDiary.setEntryType(jsonObject1.get("entryType").toString());
					if (jsonObject1.get("createdAt").toString() != null) {
						try {
							healthDiary.setDateRecorded(dateFormat.parse(jsonObject1.get("createdAt").toString()));
						}
						catch (ParseException e) {
							log.error("Cannot parse createdAt date", e);
						}
					}
					
					if (jsonObject1.get("sharedAt").toString() != null) {
						try {
							healthDiary.setSharedOn(dateFormat.parse(jsonObject1.get("sharedAt").toString()));
						}
						catch (ParseException e) {
							log.error("Cannot parse sharedAt date", e);
						}
					}
					
					healthDiary.setDateCreated(new Date());
					healthDiary.setCreator(new User(1));
					healthDiary.setVoided(false);
					healthDiary.setUuid(UUID.randomUUID().toString());
					
					healthDiaries.add(healthDiary);
				}
			}
			if (healthDiaries.size() > 0) {
				saveHealthDiaries(healthDiaries);
			}
		} else {
			settingsService.createMyCareHubSetting(PATIENT_HEALTH_DIARY_GET, new Date());
		}
	}
}
