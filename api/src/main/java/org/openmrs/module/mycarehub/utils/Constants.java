package org.openmrs.module.mycarehub.utils;

public class Constants {
	
	public final static String EMPTY = "";
	
	/** A URL to reach myCareHub API. **/
	public final static String GP_MYCAREHUB_API_URL = "mycarehub.api.url";
	
	/** Username for myCareHub api user. **/
	public final static String GP_MYCAREHUB_API_USERNAME = "mycarehub.api.username";
	
	public final static String GP_MYCAREHUB_API_DEFAULT_USERNAME = "admin";
	
	/** Password to authenticate myCareHub api user. **/
	public final static String GP_MYCAREHUB_API_PASSWORD = "mycarehub.api.password";
	
	public final static String GP_MYCAREHUB_API_DEFAULT_PASSWORD = "test";
	
	/** Time based oauth2.0 token for myCareHub backend server. **/
	public final static String GP_MYCAREHUB_API_TOKEN = "mycarehub.api.token";
	
	/** Date-time when the current token expires. **/
	public final static String GP_MYCAREHUB_API_TOKEN_EXPIRY_TIME = "mycarehub.api.token.expiry";
	
	/** MFL code for the default location on the KenyaEmr instance. **/
	public final static String GP_DEFAULT_LOCATION_MFL_CODE = "facility.mflcode";
	
	public final static String CCC_NUMBER_IDENTIFIER_TYPE_UUID = "05ee9cf4-7242-4a17-b4d4-00f707265c8a";
	
	public static final class _PersonAttributeType {
		
		public static final String NEXT_OF_KIN_CONTACT = "342a1d39-c541-4b29-8818-930916f4c2dc";
		
		public static final String NEXT_OF_KIN_NAME = "830bef6d-b01f-449d-9f8d-ac0fede8dbd3";
		
		public static final String NEXT_OF_KIN_RELATIONSHIP = "d0aa9fd1-2ac5-45d8-9c5e-4317c622c8f5";
		
		public static final String TELEPHONE_CONTACT = "b2c38640-2603-4629-aebd-3b54f33f1e3a";
	}
	
	public static final class MyCareHubSettingType {
		
		public static final String KENYAEMR_PATIENT_REGISTRATIONS = "KENYAEMR_PATIENT_REGISTRATIONS";
		
		public static final String KENYAEMR_MEDICAL_RECORDS = "KENYAEMR_MEDICAL_RECORDS";
		
		public static final String MYCAREHUB_CLIENT_REGISTRATIONS = "MYCAREHUB_CLIENT_REGISTRATIONS";
		
		public static final String PATIENT_APPOINTMENTS = "PATIENT_APPOINTMENTS";
		
		public static final String PATIENT_APPOINTMENTS_REQUESTS_POST = "PATIENT_APPOINTMENTS_REQUESTS_POST";
		
		public static final String PATIENT_RED_FLAGS_REQUESTS_POST = "PATIENT_RED_FLAGS_REQUESTS_POST";
		
		public static final String PATIENT_APPOINTMENTS_REQUESTS_GET = "PATIENT_APPOINTMENTS_REQUESTS_GET";
		
		public static final String PATIENT_RED_FLAGS_REQUESTS_GET = "PATIENT_RED_FLAGS_REQUESTS_GET";
		
		public static final String PATIENT_HEALTH_DIARY_GET = "PATIENT_HEALTH_DIARY_GET";
		
	}
	
	public static final class MedicalRecordConcepts {
		
		public static final class VitalSigns {
			
			public static final int PULSE = 5087;
			
			public static final int TEMPERATURE = 5088;
			
			public static final int WEIGHT = 5089;
			
			public static final int HEIGHT = 5090;
			
			public static final int BMI = 1342;
			
			public static final int SPO2 = 5092;
			
			public static final int CD4_COUNT = 5497;
			
			public static final int VIRAL_LOAD = 856;
			
			public static final int RESPIRATORY_RATE = 5242;
		}
		
		public static final class Tests {
			
			public static final int WIDAL = 306;
			
			public static final int HIV_POLYMERASE = 1030;
			
			public static final int TESTS_ORDERED = 1271;
		}
		
		public static final class Medications {
			
			public static final int REGIMEN = 164855;
		}
		
		public static final class Allergies {
			
			public static final int ALLERGEN = 160643;
			
			public static final int ALLERGY_REACTION = 160646;
			
			public static final int ALLERGY_OTHER_REACTION = 160644;
			
			public static final int ALLERGY_SEVERITY = 160759;
			
			public static final int ALLERGY_DATE = 160753;
		}
	}
	
	public static class RestKeys {
		
		public static class GeneralKeys {
			
			public static final String CCC_NUMBER = "ccc_number";
			
			public static final String FACILITY_MFL_CODE = "MFLCODE";
			
			public static final String MYCAREHUB_ID_KEY = "ID";
		}
		
		public static final class NextOfKinPatientRegistrationKeys {
			
			public static final String NEXT_OF_KIN_NAME_KEY = "next_of_kin_name";
			
			public static final String NEXT_OF_KIN_CONTACTS_KEY = "contacts_of_next_of_kin";
			
			public static final String NEXT_OF_KIN_RELATIONSHIP_KEY = "relationship_to_next_of_kin";
		}
		
		public static final class AppointmentObjectKeys {
			
			public static final String APPOINTMENT_UUID_KEY = "appointment_uuid";
			
			public static final String APPOINTMENT_DATE_KEY = "appointment_date";
			
			public static final String APPOINTMENT_TIME_SLOT_KEY = "time_slot";
			
			public static final String APPOINTMENT_TYPE_KEY = "appointment_type";
			
			public static final String APPOINTMENT_STATUS_KEY = "status";
			
			public static final String APPOINTMENTS_CONTAINER_KEY = "appointments";
		}
		
		public static final class AppointmentRequestObjectKeys {
			
			public static final String APPOINTMENT_REQUEST_STATUS_KEY = "status";
			
			public static final String APPOINTMENT_PROGRESS_DATE_KEY = "InProgressAt";
			
			public static final String APPOINTMENT_PROGRESS_BY_KEY = "InProgressBy";
			
			public static final String APPOINTMENT_RESOLVED_DATE_KEY = "ResolvedAt";
			
			public static final String APPOINTMENT_RESOLVED_BY_KEY = "ResolvedBy";
			
			public static final String APPOINTMENT_REQUEST_CONTAINER = "appointment-request";
		}
		
		public static final class REdFlagsObjectKeys {
			
			public static final String RED_FLAG_REQUEST_TYPE_KEY = "RequestType";
			
			public static final String RED_FLAG_STATUS_KEY = "status";
			
			public static final String RED_FLAG_PROGRESS_DATE_KEY = "InProgressAt";
			
			public static final String RED_FLAG_PROGRESS_BY_KEY = "InProgressBy";
			
			public static final String RED_FLAG_RESOLVED_DATE_KEY = "ResolvedAt";
			
			public static final String RED_FLAG_RESOLVED_BY_KEY = "ResolvedBy";
			
			public static final String RED_FLAG_CONTAINER = "serviceRequests";
		}
		
		public static final class MedicalRecordKeys {
			
			public static final String TEMPERATURE_CONCEPT_KEY = "temperature";
			
			public static final String WEIGHT_CONCEPT_KEY = "weight";
			
			public static final String HEIGHT_CONCEPT_KEY = "height";
			
			public static final String BMI_CONCEPT_KEY = "bmi";
			
			public static final String SPO2_CONCEPT_KEY = "spo2";
			
			public static final String PULSE_CONCEPT_KEY = "pulse";
			
			public static final String CD4_CONCEPT_KEY = "cd4";
			
			public static final String VIRAL_LOAD_CONCEPT_KEY = "viral_load";
			
			public static final String RESPIRATORY_RATE_CONCEPT_KEY = "respiratory_rate";
		}
	}
}
