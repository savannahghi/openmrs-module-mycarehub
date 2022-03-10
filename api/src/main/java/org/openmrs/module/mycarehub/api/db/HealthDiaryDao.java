package org.openmrs.module.mycarehub.api.db;

import org.openmrs.module.mycarehub.model.HealthDiary;

import java.util.List;

public interface HealthDiaryDao {
	
	List<HealthDiary> saveHealthDiaries(List<HealthDiary> healthDiary);
	
	Number countHealthDiaries();
	
	List<HealthDiary> getPagedHealthDiaries(Integer pageNumber, Integer pageSize);
	
}
