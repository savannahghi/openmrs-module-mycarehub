package org.openmrs.module.mycarehub.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.mycarehub.api.service.AppointmentRequestsService;
import org.openmrs.module.mycarehub.api.service.MyCareHubPatientService;

public class DataSynchronizer {
	
	private final Log log = LogFactory.getLog(DataSynchronizer.class);
	
	private static Boolean isRunning = false;
	
	public void synchronizeData() {
		if (!isRunning) {
			synchronizeAllData();
		} else {
			log.error("Data synchronizer aborting (another synchronizer already running)!");
		}
	}
	
	private void synchronizeAllData() {
		try {
			isRunning = true;
			log.info("Firing up the REST Synchronizer ...");
			Context.getService(MyCareHubPatientService.class).syncPatientData();
			Context.getService(AppointmentRequestsService.class).syncPatientAppointments();
		}
		finally {
			isRunning = false;
			log.info("Shutting down the REST Synchronizer...");
		}
	}
}
