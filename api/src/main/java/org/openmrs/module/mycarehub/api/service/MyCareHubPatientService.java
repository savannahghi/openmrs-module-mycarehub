package org.openmrs.module.mycarehub.api.service;

import org.openmrs.api.OpenmrsService;

public interface MyCareHubPatientService extends OpenmrsService {
	
	void syncPatientData();
}
