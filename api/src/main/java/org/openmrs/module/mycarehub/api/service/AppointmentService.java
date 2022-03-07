package org.openmrs.module.mycarehub.api.service;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.appointmentscheduling.Appointment;
import org.openmrs.module.mycarehub.model.AppointmentRequests;

import java.util.Date;
import java.util.List;

public interface AppointmentService extends OpenmrsService {
	
	List<Appointment> getAppointmentsByLastSyncDate(Date lastSyncDate);
	
	List<AppointmentRequests> getAllAppointmentRequests();
	
	List<AppointmentRequests> getAllAppointmentRequestsByLastSyncDate(Date lastSyncDate);
	
	List<AppointmentRequests> saveAppointmentRequests(List<AppointmentRequests> appointmentRequests);
	
	AppointmentRequests getAppointmentRequestByMycarehubId(String mycarehubId);
	
	void syncPatientAppointments();
	
	void syncPatientAppointmentRequests();
	
	void fetchPatientAppointmentRequests();
}
