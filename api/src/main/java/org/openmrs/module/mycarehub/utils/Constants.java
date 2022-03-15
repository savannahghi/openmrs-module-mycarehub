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
}
