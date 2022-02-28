package org.openmrs.module.mycarehub.api.service;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.appointmentscheduling.Appointment;
import org.openmrs.module.mycarehub.model.AppointmentRequests;

import java.util.Date;
import java.util.List;

public interface AppointmentRequestsService extends OpenmrsService {
	
	List<Appointment> getAppointmentsByLastSyncDate(Date lastSyncDate);
	
	List<AppointmentRequests> getAllAppointmentRequests();
	
	List<AppointmentRequests> saveAppointmentRequests(List<AppointmentRequests> appointmentRequests);
	
	void syncPatientAppointments();
}
