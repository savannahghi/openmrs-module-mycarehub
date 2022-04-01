package org.openmrs.module.mycarehub.api.db;

import org.openmrs.Obs;
import org.openmrs.module.mycarehub.model.AppointmentRequests;

import java.util.Date;
import java.util.List;

public interface AppointmentDao {
	
	List<Obs> getAppointmentsByLastSyncDate(Date lastSyncDate);
	
	List<AppointmentRequests> getAllAppointmentRequests();
	
	List<AppointmentRequests> getAllAppointmentRequestsByLastSyncDate(Date lastSyncDate);
	
	List<AppointmentRequests> saveAppointmentRequests(List<AppointmentRequests> appointmentRequests);
	
	AppointmentRequests saveAppointmentRequests(AppointmentRequests appointmentRequest);
	
	AppointmentRequests getAppointmentRequestByMycarehubId(String mycarehubId);
	
	AppointmentRequests getAppointmentRequestByUuid(String uuid);
	
	Number countAppointments();
	
	List<AppointmentRequests> getPagedAppointments(Integer pageNumber, Integer pageSize);
	
	Obs getObsByEncounterAndConcept(Integer encounterId, Integer conceptId);
}
