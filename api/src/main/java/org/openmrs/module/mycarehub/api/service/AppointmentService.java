package org.openmrs.module.mycarehub.api.service;

import java.util.Date;
import java.util.List;
import org.openmrs.Obs;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.mycarehub.model.AppointmentRequests;

public interface AppointmentService extends OpenmrsService {
	
	List<Obs> getAppointmentsByLastSyncDate(Date lastSyncDate);
	
	List<AppointmentRequests> getAllAppointmentRequests();
	
	AppointmentRequests getAppointmentRequestByUuid(String uuid);
	
	List<AppointmentRequests> getAllAppointmentRequestsByLastSyncDate(Date lastSyncDate);
	
	List<AppointmentRequests> saveAppointmentRequests(List<AppointmentRequests> appointmentRequests);
	
	AppointmentRequests saveAppointmentRequests(AppointmentRequests appointmentRequest);
	
	AppointmentRequests getAppointmentRequestByMycarehubId(String mycarehubId);
	
	Number countAppointments(String searchString);
	
	List<AppointmentRequests> getPagedAppointments(String searchString, Integer pageNumber, Integer pageSize);
	
	void syncPatientAppointments();
	
	void syncPatientAppointmentRequests();
	
	void fetchPatientAppointmentRequests();
}
