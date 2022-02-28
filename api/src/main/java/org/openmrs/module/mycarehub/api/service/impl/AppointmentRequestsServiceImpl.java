package org.openmrs.module.mycarehub.api.service.impl;

import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.appointmentscheduling.Appointment;
import org.openmrs.module.mycarehub.api.db.AppointmentRequestsDao;
import org.openmrs.module.mycarehub.api.service.AppointmentRequestsService;
import org.openmrs.module.mycarehub.model.AppointmentRequests;

import java.util.Date;
import java.util.List;

public class AppointmentRequestsServiceImpl extends BaseOpenmrsService implements AppointmentRequestsService {
	
	AppointmentRequestsDao dao;
	
	public AppointmentRequestsServiceImpl(AppointmentRequestsDao dao) {
		this.dao = dao;
	}
	
	@Override
	public List<Appointment> getAppointmentsByLastSyncDate(Date lastSyncDate) {
		return dao.getAppointmentsByLastSyncDate(lastSyncDate);
	}
	
	@Override
	public List<AppointmentRequests> getAllAppointmentRequests() {
		return dao.getAllAppointmentRequests();
	}
	
	@Override
	public List<AppointmentRequests> saveAppointmentRequests(List<AppointmentRequests> appointmentRequests) {
		return dao.saveAppointmentRequests(appointmentRequests);
	}
	
	public AppointmentRequestsDao getDao() {
		return dao;
	}
	
	public void setDao(AppointmentRequestsDao dao) {
		this.dao = dao;
	}
}
