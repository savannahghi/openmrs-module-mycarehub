package org.openmrs.module.mycarehub.api.service.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Obs;
import org.openmrs.PatientIdentifierType;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.appointmentscheduling.Appointment;
import org.openmrs.module.mycarehub.api.db.AppointmentDao;
import org.openmrs.module.mycarehub.api.service.AppointmentService;
import org.openmrs.module.mycarehub.api.service.MyCareHubSettingsService;
import org.openmrs.module.mycarehub.model.AppointmentRequests;
import org.openmrs.module.mycarehub.model.MyCareHubSetting;
import org.openmrs.module.mycarehub.utils.MyCareHubUtil;

import static org.openmrs.module.mycarehub.utils.Constants.APPOINTMENT_DATE_CONCEPT_ID;
import static org.openmrs.module.mycarehub.utils.Constants.APPOINTMENT_REASON_CONCEPT_ID;
import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.PATIENT_APPOINTMENTS_REQUESTS_GET;
import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.PATIENT_APPOINTMENTS_REQUESTS_POST;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.PATIENT_APPOINTMENTS;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.AppointmentObjectKeys.APPOINTMENTS_CONTAINER_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.AppointmentObjectKeys.APPOINTMENT_DATE_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.AppointmentObjectKeys.APPOINTMENT_ID_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.AppointmentObjectKeys.APPOINTMENT_REASON_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.AppointmentRequestObjectKeys.APPOINTMENT_PROGRESS_BY_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.AppointmentRequestObjectKeys.APPOINTMENT_PROGRESS_DATE_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.AppointmentRequestObjectKeys.APPOINTMENT_REQUEST_CONTAINER;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.AppointmentRequestObjectKeys.APPOINTMENT_REQUEST_STATUS_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.AppointmentRequestObjectKeys.APPOINTMENT_RESOLVED_BY_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.AppointmentRequestObjectKeys.APPOINTMENT_RESOLVED_DATE_KEY;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.GeneralKeys.CCC_NUMBER;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.GeneralKeys.FACILITY_MFL_CODE;
import static org.openmrs.module.mycarehub.utils.Constants.RestKeys.GeneralKeys.MYCAREHUB_ID_KEY;

public class AppointmentServiceImpl extends BaseOpenmrsService implements AppointmentService {
	
	private static final Log log = LogFactory.getLog(AppointmentServiceImpl.class);
	
	private AppointmentDao dao;
	
	private final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	public AppointmentServiceImpl(AppointmentDao dao) {
		this.dao = dao;
	}
	
	@Override
	public List<Obs> getAppointmentsByLastSyncDate(Date lastSyncDate) {
		return dao.getAppointmentsByLastSyncDate(lastSyncDate);
	}
	
	@Override
	public List<AppointmentRequests> getAllAppointmentRequests() {
		return dao.getAllAppointmentRequests();
	}
	
	@Override
	public AppointmentRequests getAppointmentRequestByUuid(String uuid) {
		return dao.getAppointmentRequestByUuid(uuid);
	}
	
	@Override
	public List<AppointmentRequests> getAllAppointmentRequestsByLastSyncDate(Date lastSyncDate) {
		return dao.getAllAppointmentRequestsByLastSyncDate(lastSyncDate);
	}
	
	@Override
	public List<AppointmentRequests> saveAppointmentRequests(List<AppointmentRequests> appointmentRequests) {
		return dao.saveAppointmentRequests(appointmentRequests);
	}
	
	@Override
	public AppointmentRequests saveAppointmentRequests(AppointmentRequests appointmentRequest) {
		return dao.saveAppointmentRequests(appointmentRequest);
	}
	
	@Override
	public AppointmentRequests getAppointmentRequestByMycarehubId(String mycarehubId) {
		return dao.getAppointmentRequestByMycarehubId(mycarehubId);
	}
	
	@Override
	public Number countAppointments() {
		return dao.countAppointments();
	}
	
	@Override
	public List<AppointmentRequests> getPagedAppointments(Integer pageNumber, Integer pageSize) {
		//		return dao.getPagedAppointments(pageNumber, pageSize);
		List<AppointmentRequests> appointments = dao.getPagedAppointments(pageNumber, pageSize);
		System.out.println("PPPPPPPPPPPPPPPPPPPpp " + appointments.size());
		return appointments;
	}
	
	@Override
	public void syncPatientAppointments() {
		MyCareHubSettingsService settingsService = Context.getService(MyCareHubSettingsService.class);
		MyCareHubSetting setting = settingsService.getLatestMyCareHubSettingByType(PATIENT_APPOINTMENTS);
		if (setting != null) {
			List<Obs> appointments = dao.getAppointmentsByLastSyncDate(setting.getLastSyncTime());
			Date newSyncDate = new Date();
			
			JsonObject containerObject = new JsonObject();
			JsonArray appointmentsArray = new JsonArray();
			PatientIdentifierType cccPatientIdentifierType = MyCareHubUtil.getcccPatientIdentifierType();
			List<Integer> encounterIds = new ArrayList<Integer>();
			if (appointments.size() > 0) {
				for (Obs appointment : appointments) {
					Integer encounterId = appointment.getEncounter().getEncounterId();
					if (!encounterIds.contains(encounterId)) {
						JsonObject appointmentObject = new JsonObject();
						SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
						if (appointment.getConcept().getId() == APPOINTMENT_DATE_CONCEPT_ID) {
							Obs obs = dao.getObsByEncounterAndConcept(encounterId, APPOINTMENT_REASON_CONCEPT_ID);
							appointmentObject.addProperty(APPOINTMENT_DATE_KEY,
							    dateFormat.format(appointment.getValueDate()));
							if (obs != null) {
								appointmentObject.addProperty(APPOINTMENT_REASON_KEY, obs.getValueCodedName().getName());
							} else {
								appointmentObject.addProperty(APPOINTMENT_REASON_KEY, "");
							}
						} else {
							Obs obs = dao.getObsByEncounterAndConcept(encounterId, APPOINTMENT_DATE_CONCEPT_ID);
							if (obs != null && obs.getValueDatetime() != null) {
								appointmentObject.addProperty(APPOINTMENT_DATE_KEY,
								    dateFormat.format(obs.getValueDatetime()));
							} else {
								appointmentObject.addProperty(APPOINTMENT_DATE_KEY, "");
							}
							appointmentObject.addProperty(APPOINTMENT_REASON_KEY, obs.getValueCodedName().getName());
						}
						appointmentObject.addProperty(APPOINTMENT_ID_KEY, appointment.getEncounter().getEncounterId());
						appointmentObject.addProperty(CCC_NUMBER,
						    appointment.getPatient().getPatientIdentifier(cccPatientIdentifierType).getIdentifier());
						appointmentsArray.add(appointmentObject);
					}
				}
				
				containerObject.addProperty(FACILITY_MFL_CODE, MyCareHubUtil.getDefaultLocationMflCode());
				containerObject.add(APPOINTMENTS_CONTAINER_KEY, appointmentsArray);
				
				MyCareHubUtil.uploadPatientAppointments(containerObject, newSyncDate);
				
			} else {
				settingsService.createMyCareHubSetting(PATIENT_APPOINTMENTS, newSyncDate);
			}
		} else {
			settingsService.createMyCareHubSetting(PATIENT_APPOINTMENTS, new Date());
		}
	}
	
	@Override
	public void syncPatientAppointmentRequests() {
		MyCareHubSettingsService settingsService = Context.getService(MyCareHubSettingsService.class);
		MyCareHubSetting setting = settingsService.getLatestMyCareHubSettingByType(PATIENT_APPOINTMENTS_REQUESTS_POST);
		if (setting != null) {
			List<AppointmentRequests> appointmentRequests = dao.getAllAppointmentRequestsByLastSyncDate(setting
			        .getLastSyncTime());
			Date newSyncDate = new Date();
			
			JsonObject containerObject = new JsonObject();
			JsonArray appointmentsObject = new JsonArray();
			if (appointmentRequests.size() > 0) {
				for (AppointmentRequests appointmentRequest : appointmentRequests) {
					JsonObject appointmentObject = new JsonObject();
					appointmentObject.addProperty(MYCAREHUB_ID_KEY, appointmentRequest.getMycarehubId());
					appointmentObject.addProperty(APPOINTMENT_REQUEST_STATUS_KEY, appointmentRequest.getStatus());
					if (appointmentRequest.getProgressDate() != null) {
						appointmentObject.addProperty(APPOINTMENT_PROGRESS_DATE_KEY,
						    dateTimeFormat.format(appointmentRequest.getProgressDate()));
					} else {
						appointmentObject.addProperty(APPOINTMENT_PROGRESS_DATE_KEY, "null");
					}
					appointmentObject.addProperty(APPOINTMENT_PROGRESS_BY_KEY, appointmentRequest.getProgressBy());
					if (appointmentRequest.getDateResolved() != null) {
						appointmentObject.addProperty(APPOINTMENT_RESOLVED_DATE_KEY,
						    dateTimeFormat.format(appointmentRequest.getDateResolved()));
					} else {
						appointmentObject.addProperty(APPOINTMENT_RESOLVED_DATE_KEY, "null");
					}
					appointmentObject.addProperty(APPOINTMENT_RESOLVED_BY_KEY, appointmentRequest.getResolvedBy());
					
					appointmentsObject.add(appointmentObject);
				}
				
				containerObject.add(APPOINTMENT_REQUEST_CONTAINER, appointmentsObject);
				
				MyCareHubUtil.uploadPatientAppointmentRequests(containerObject, newSyncDate);
				
			} else {
				settingsService.createMyCareHubSetting(PATIENT_APPOINTMENTS_REQUESTS_POST, newSyncDate);
			}
		} else {
			settingsService.createMyCareHubSetting(PATIENT_APPOINTMENTS_REQUESTS_POST, new Date());
		}
	}
	
	@Override
	public void fetchPatientAppointmentRequests() {
		MyCareHubSettingsService settingsService = Context.getService(MyCareHubSettingsService.class);
		MyCareHubSetting setting = settingsService.getLatestMyCareHubSettingByType(PATIENT_APPOINTMENTS_REQUESTS_GET);
		if (setting != null) {
			Date newSyncDate = new Date();
			
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("MFLCODE", MyCareHubUtil.getDefaultLocationMflCode());
			jsonObject.addProperty("lastSyncTime", dateTimeFormat.format(setting.getLastSyncTime()));
			
			JsonArray jsonArray = MyCareHubUtil.fetchPatientAppointmentRequests(setting.getLastSyncTime(), newSyncDate);
			
			List<AppointmentRequests> appointmentRequests = new ArrayList<AppointmentRequests>();
			if (jsonArray != null) {
				for (int i = 0; i < jsonArray.size(); i++) {
					JsonObject jsonObject1 = jsonArray.get(i).getAsJsonObject();
					AppointmentRequests appointmentRequest = new AppointmentRequests();
					String mycarehubId = jsonObject1.get("ID").toString();
					AppointmentRequests existingRequests = getAppointmentRequestByMycarehubId(mycarehubId);
					if (existingRequests != null && existingRequests.getMycarehubId() != null) {
						appointmentRequest = existingRequests;
					} else {
						appointmentRequest.setCreator(new User(1));
						appointmentRequest.setDateCreated(new Date());
						appointmentRequest.setUuid(UUID.randomUUID().toString());
						appointmentRequest.setVoided(false);
					}
					
					appointmentRequest.setAppointmentUUID(jsonObject1.get("AppointmentID").toString());
					appointmentRequest.setMycarehubId(jsonObject1.get("ID").toString());
					appointmentRequest.setAppointmentReason(jsonObject1.get("AppointmentReason").toString());
					try {
						appointmentRequest
						        .setRequestedDate(dateFormat.parse(jsonObject1.get("SuggestedDate").getAsString()));
					}
					catch (ParseException e) {
						log.error("Cannot parse SuggestedDate date", e);
					}
					appointmentRequest.setStatus(jsonObject1.get("Status").toString());
					if (jsonObject1.get("InProgressAt").toString() != null) {
						try {
							appointmentRequest.setProgressDate(dateFormat.parse(jsonObject1.get("InProgressAt").toString()));
						}
						catch (ParseException e) {
							log.error("Cannot parse InProgressAt date", e);
						}
					}
					appointmentRequest.setProgressBy(jsonObject1.get("InProgressBy").toString());
					if (jsonObject1.get("ResolvedAt").toString() != null) {
						try {
							appointmentRequest.setProgressDate(dateFormat.parse(jsonObject1.get("ResolvedAt").toString()));
						}
						catch (ParseException e) {
							log.error("Cannot parse ResolvedAt date", e);
						}
					}
					
					appointmentRequest.setResolvedBy(jsonObject1.get("ResolvedBy").toString());
					appointmentRequest.setClientName(jsonObject1.get("ClientName").toString());
					appointmentRequest.setClientContact(jsonObject1.get("ClientContact").toString());
					appointmentRequest.setCccNumber(jsonObject1.get("CCCNumber").toString());
					appointmentRequest.setMflCode(jsonObject1.get("MFLCODE").toString());
					
					appointmentRequests.add(appointmentRequest);
					
				}
			}
			if (appointmentRequests.size() > 0) {
				saveAppointmentRequests(appointmentRequests);
			}
		} else {
			settingsService.createMyCareHubSetting(PATIENT_APPOINTMENTS_REQUESTS_GET, new Date());
		}
	}
	
	public AppointmentDao getDao() {
		return dao;
	}
	
	public void setDao(AppointmentDao dao) {
		this.dao = dao;
	}
}
