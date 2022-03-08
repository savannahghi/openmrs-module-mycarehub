package org.openmrs.module.mycarehub.api.service;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.mycarehub.model.RedFlags;

import java.util.Date;
import java.util.List;

public interface RedFlagService extends OpenmrsService {
	
	List<RedFlags> getAllRedFlagRequests();
	
	List<RedFlags> getAllRedFlagRequestsByLastSyncDate(Date lastSyncDate);
	
	List<RedFlags> saveRedFlagRequests(List<RedFlags> redFlags);
	
	RedFlags getRedFlagRequestByMycarehubId(String mycarehubId);
	
	void syncPatientRedFlagRequests();
	
	void fetchPatientRedFlagRequests();
}
