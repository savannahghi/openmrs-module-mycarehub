package org.openmrs.module.mycarehub.api.service;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.mycarehub.model.HealthDiary;

import java.util.List;

public interface HealthDiaryService extends OpenmrsService {
	
	List<HealthDiary> saveHealthDiaries(List<HealthDiary> healthDiary);
	
	Number countHealthDiaries();
	
	List<HealthDiary> getPagedHealthDiaries(Integer pageNumber, Integer pageSize);
	
	void fetchPatientHealthDiaries();
}
