package org.openmrs.module.mycarehub.utils;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Constants {

  public static final String EMPTY = "";

  public static final String mycarehubDateTimePattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

  /** A URL to reach myCareHub API. * */
  public static final String GP_MYCAREHUB_API_URL = "mycarehub.api.url";

  /** Username for myCareHub api user. * */
  public static final String GP_MYCAREHUB_API_USERNAME = "mycarehub.api.username";

  public static final String GP_MYCAREHUB_API_DEFAULT_USERNAME = "admin";

  /** Password to authenticate myCareHub api user. * */
  public static final String GP_MYCAREHUB_API_PASSWORD = "mycarehub.api.password";

  public static final String GP_MYCAREHUB_API_DEFAULT_PASSWORD = "test";

  /** Time based oauth2.0 token for myCareHub backend server. * */
  public static final String GP_MYCAREHUB_API_TOKEN = "mycarehub.api.token";

  /** Date-time when the current token expires. * */
  public static final String GP_MYCAREHUB_API_TOKEN_EXPIRY_TIME = "mycarehub.api.token.expiry";

  /** MFL code for the default location on the KenyaEmr instance. * */
  public static final String GP_DEFAULT_LOCATION_MFL_CODE = "facility.mflcode";

  public static final String CCC_NUMBER_IDENTIFIER_TYPE_UUID =
      "05ee9cf4-7242-4a17-b4d4-00f707265c8a";

  public static final int APPOINTMENT_DATE_CONCEPT_ID = 5096;

  public static final int APPOINTMENT_REASON_CONCEPT_ID = 160288;

  public static final String DATE_TIME_FORMAT = "dd-MM-yyyy HH:mm:ss";

  public static final String DATE_FORMAT = "dd-MM-yyyy";

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

    public static final String PATIENT_APPOINTMENTS_REQUESTS_POST =
        "PATIENT_APPOINTMENTS_REQUESTS_POST";

    public static final String PATIENT_RED_FLAGS_REQUESTS_POST = "PATIENT_RED_FLAGS_REQUESTS_POST";

    public static final String PATIENT_APPOINTMENTS_REQUESTS_GET =
        "PATIENT_APPOINTMENTS_REQUESTS_GET";

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

      public static final List<Integer> ALLERGY_GROUP_CONCEPTS =
          new ArrayList<Integer>() {

            {
              add(121760);
              add(121689);
            }
          };

      public static final List<Integer> ALLERGEN_CONCEPTS =
          new ArrayList<Integer>() {

            {
              add(160643);
              add(1193);
            }
          };

      public static final List<Integer> ALLERGY_REACTION_CONCEPTS =
          new ArrayList<Integer>() {

            {
              add(160646);
              add(159935);
            }
          };

      public static final int ALLERGY_OTHER_REACTION = 160644;

      public static final List<Integer> ALLERGY_SEVERITY_CONCEPTS =
          new ArrayList<Integer>() {

            {
              add(160759);
              add(162760);
            }
          };

      public static final int ALLERGY_DATE = 160753;

      public static final List<Integer> OTHER_DRUG_CONCEPTS =
          Arrays.asList(
              164505, 1652, 160124, 162565, 162563, 162199, 792, 160104, 1652, 160124, 162561,
              162200, 164505, 162559, 164508, 164509, 164510, 162200, 162561, 164505, 162563,
              162201, 164508, 164509, 164510, 162561, 164511, 162201, 164512, 162200);
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

      public static final String APPOINTMENT_ID_KEY = "appointment_id";

      public static final String APPOINTMENT_DATE_KEY = "appointment_date";

      public static final String APPOINTMENT_REASON_KEY = "appointment_reason";

      public static final String APPOINTMENTS_CONTAINER_KEY = "appointments";
    }

    public static final class AppointmentRequestObjectKeys {

      public static final String APPOINTMENT_REQUEST_STATUS_KEY = "status";

      public static final String APPOINTMENT_PROGRESS_DATE_KEY = "InProgressAt";

      public static final String APPOINTMENT_PROGRESS_BY_KEY = "InProgressBy";

      public static final String APPOINTMENT_RESOLVED_DATE_KEY = "ResolvedAt";

      public static final String APPOINTMENT_RESOLVED_BY_KEY = "ResolvedBy";

      public static final String APPOINTMENT_REQUEST_CONTAINER = "serviceRequests";
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

      public static final String APPOINTMENT_DATE_KEY = "appointment_date";
    }
  }
}
