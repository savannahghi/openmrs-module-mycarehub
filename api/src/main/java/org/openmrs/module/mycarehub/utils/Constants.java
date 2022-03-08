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
	
	/** MFL code for the default location on the KenyaEmr instance. **/
	public final static String GP_DEFAULT_LOCATION_MFL_CODE = "kenyaemr.defaultLocation";
	
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
}
