package org.openmrs.module.mycarehub.api.db.hibernate;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.mycarehub.api.db.MyCareHubPatientDao;
import org.openmrs.module.mycarehub.api.rest.mapper.MyCareHubAllergy;
import org.openmrs.module.mycarehub.model.ConsentedPatient;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

public class MyCareHubPatientDaoTest extends BaseModuleContextSensitiveTest {
  private MyCareHubPatientDao myCareHubPatientDao;

  @Autowired DbSessionFactory sessionFactory;

  public static final String MCH_CONSENTED_PATIENT_XML_FILENAME =
      "org.openmrs.module.mycarehub/api/include/MyCareHubConsentedPatient.xml";

  @Override
  public Boolean useInMemoryDatabase() {
    // Defaults to true. If you want to use your own database specified in your runtime properties
    // return false instead, otherwise return super.useInMemoryDatabase();.
    return super.useInMemoryDatabase();
  }

  @Before
  public void setUp() throws Exception {
    executeDataSet(MCH_CONSENTED_PATIENT_XML_FILENAME);

    myCareHubPatientDao = new HibernateMyCareHubPatientDao(sessionFactory);
  }

  @Test
  public void getConsentedPatientsInList_shouldReturnZeroAsNoSuchPatientExist() {
    List<ConsentedPatient> consentedPatients =
        myCareHubPatientDao.getConsentedPatientsInList(Collections.singletonList(56));
    assertEquals(0, consentedPatients.size());
  }

  @Test
  public void
      getConsentedPatientsInList_shouldReturnOneAsOnlyOnePatientExistsWithTheProvidedPatientID() {
    List<ConsentedPatient> consentedPatients =
        myCareHubPatientDao.getConsentedPatientsInList(Collections.singletonList(1));
    assertEquals(1, consentedPatients.size());
  }

  @Test
  public void saveNewConsentedPatients() {
    ConsentedPatient consentedPatient = new ConsentedPatient();
    consentedPatient.setUuid(String.valueOf(UUID.randomUUID()));
    consentedPatient.setId(1);
    consentedPatient.setPatientId(1);
    consentedPatient.setVoided(false);

    myCareHubPatientDao.saveNewConsentedPatients(consentedPatient);
  }

  @Test
  public void getConsentedPatientIdsUpdatedSinceDate() throws ParseException {
    String dateTimeStr = "2023-09-22 10:38:09";
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = formatter.parse(dateTimeStr);

    List<Integer> consentedPatientIds =
        myCareHubPatientDao.getConsentedPatientIdsUpdatedSinceDate(date);
    assertEquals(0, consentedPatientIds.size());
  }

  @Test
  public void getCccPatientIdsByIdentifier() {
    List<Integer> consentedPatientIds = myCareHubPatientDao.getCccPatientIdsByIdentifier("101");
    assertEquals(0, consentedPatientIds.size());
  }

  @Test
  public void getConsentedPatientsWithUpdatedMedicalRecordsSinceDate() throws ParseException {
    String dateTimeStr = "2023-09-22 10:38:09";
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = formatter.parse(dateTimeStr);

    List<Integer> consentedPatientIds =
        myCareHubPatientDao.getConsentedPatientsWithUpdatedMedicalRecordsSinceDate(date);
    assertEquals(0, consentedPatientIds.size());
  }

  @Test
  public void getUpdatedVitalSignsSinceDate_shouldReturnZero() {
    Patient patient = new Patient();
    patient.setPatientId(7);
    patient.setDateCreated(new Date());

    List<Obs> obs =
        myCareHubPatientDao.getUpdatedVitalSignsSinceDate(patient, patient.getDateCreated());
    assertEquals(0, obs.size());
  }

  @Test
  public void getUpdatedAllergiesSinceDate_shouldReturnZeroAllergies() throws ParseException {
    Date fomattedDate = getFormattedDate();

    Patient patient = new Patient();
    patient.setPatientId(7);

    List<MyCareHubAllergy> obs =
        myCareHubPatientDao.getUpdatedAllergiesSinceDate(patient, fomattedDate);
    assertEquals(0, obs.size());
  }

  @Test
  public void getUpdatedTestsSinceDate_shouldReturnZeroObs() throws ParseException {
    Date fomattedDate = getFormattedDate();

    Patient patient = new Patient();
    patient.setPatientId(7);

    List<Obs> obs = myCareHubPatientDao.getUpdatedTestsSinceDate(patient, fomattedDate);
    assertEquals(0, obs.size());
  }

  @Test
  public void getUpdatedTestOrdersSinceDate_shouldReturnZeroObs() throws ParseException {
    Date fomattedDate = getFormattedDate();

    Patient patient = new Patient();
    patient.setPatientId(7);

    List<Obs> obs = myCareHubPatientDao.getUpdatedTestOrdersSinceDate(patient, fomattedDate);
    assertEquals(0, obs.size());
  }

  @Test
  public void getUpdatedMedicationsSinceDate_shouldReturnZeroObs() throws ParseException {
    Date fomattedDate = getFormattedDate();

    Patient patient = new Patient();
    patient.setPatientId(7);

    List<Obs> obs = myCareHubPatientDao.getUpdatedMedicationsSinceDate(patient, fomattedDate);
    assertEquals(0, obs.size());
  }

  private static Date getFormattedDate() throws ParseException {
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date currentDate = new Date();
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(currentDate);
    calendar.add(Calendar.MONTH, -1);
    Date oneMonthAgo = calendar.getTime();

    String date = formatter.format(oneMonthAgo);
    return formatter.parse(date);
  }
}
