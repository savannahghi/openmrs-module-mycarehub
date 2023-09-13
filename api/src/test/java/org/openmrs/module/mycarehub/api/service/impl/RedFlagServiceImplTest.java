package org.openmrs.module.mycarehub.api.service.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.openmrs.module.mycarehub.utils.Constants.*;
import static org.openmrs.module.mycarehub.utils.Constants.EMPTY;
import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.PATIENT_RED_FLAGS_REQUESTS_GET;
import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.PATIENT_RED_FLAGS_REQUESTS_POST;
import static org.powermock.api.mockito.PowerMockito.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.mycarehub.api.db.MyCareHubRedFlagDao;
import org.openmrs.module.mycarehub.api.rest.ApiClient;
import org.openmrs.module.mycarehub.api.rest.RestApiService;
import org.openmrs.module.mycarehub.api.service.MyCareHubSettingsService;
import org.openmrs.module.mycarehub.model.MyCareHubSetting;
import org.openmrs.module.mycarehub.model.RedFlags;
import org.openmrs.module.mycarehub.utils.MyCareHubUtil;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class, ApiClient.class, MyCareHubUtil.class, RestApiService.class})
public class RedFlagServiceImplTest {

  @Mock MyCareHubRedFlagDao myCareHubRedFlagDao;

  @InjectMocks RedFlagServiceImpl fakeRedFlagServiceImpl;

  private static final List<RedFlags> RED_FLAGS = testRedFlagsFactory();

  private static Date date = null;

  private MyCareHubSettingsService myCareHubSettingsService;
  private static MyCareHubSetting setting = null;

  private RestApiService restApiService;

  private AdministrationService administrationService;

  private static final String testServerUrl = "https://mycarehub-testing.savannahghi.org/";

  private static final String username = "kenya-emr@savannahinformatics.com";

  private static final String password = "#kenya-EMR#";

  private static final String mflCode = "232343434";

  @Before
  public void setUp() {
    date = new Date();
    myCareHubSettingsService = mock(MyCareHubSettingsService.class);
    setting = mock(MyCareHubSetting.class);
    administrationService = mock(AdministrationService.class);

    mockStatic(Context.class);
    when(Context.getService(MyCareHubSettingsService.class)).thenReturn(myCareHubSettingsService);

    mockStatic(ApiClient.class);
    mockStatic(RestApiService.class);
    mockStatic(MyCareHubUtil.class);

    PowerMockito.when(Context.getAdministrationService()).thenReturn(administrationService);
    PowerMockito.when(administrationService.getGlobalProperty(GP_MYCAREHUB_API_URL, EMPTY))
        .thenReturn(testServerUrl);
    PowerMockito.when(
            administrationService.getGlobalProperty(
                GP_MYCAREHUB_API_USERNAME, GP_MYCAREHUB_API_DEFAULT_USERNAME))
        .thenReturn(username);
    PowerMockito.when(
            administrationService.getGlobalProperty(
                GP_MYCAREHUB_API_PASSWORD, GP_MYCAREHUB_API_DEFAULT_PASSWORD))
        .thenReturn(password);
    PowerMockito.when(administrationService.getGlobalProperty(GP_DEFAULT_LOCATION_MFL_CODE, EMPTY))
        .thenReturn(mflCode);
  }

  @Test
  public void getAllRedFlagRequests() {
    List<RedFlags> redFlagsList = RED_FLAGS;
    assertNotNull(redFlagsList);

    when(myCareHubRedFlagDao.getAllRedFlagRequests()).thenReturn(redFlagsList);

    List<RedFlags> allRedFlags = fakeRedFlagServiceImpl.getAllRedFlagRequests();
    assertEquals(1, allRedFlags.size());
    assertNotEquals(2, allRedFlags.size());
    assertEquals(redFlagsList, allRedFlags);
  }

  @Test
  public void getRedFlagByUuid() {
    RedFlags redFlag = RED_FLAGS.get(0);
    assertNotNull(redFlag);

    when(myCareHubRedFlagDao.getRedFlagByUuid(redFlag.getUuid())).thenReturn(redFlag);

    RedFlags singleRedFlag = fakeRedFlagServiceImpl.getRedFlagByUuid(redFlag.getUuid());
    assertEquals(redFlag, singleRedFlag);
  }

  @Test
  public void getAllRedFlagRequestsByLastSyncDate() {
    List<RedFlags> redFlagsList = RED_FLAGS;
    assertNotNull(redFlagsList);

    when(myCareHubRedFlagDao.getAllRedFlagRequestsByLastSyncDate(date)).thenReturn(redFlagsList);

    List<RedFlags> redFlags = fakeRedFlagServiceImpl.getAllRedFlagRequestsByLastSyncDate(date);
    assertEquals(1, redFlags.size());
    assertNotNull(redFlags);
  }

  @Test
  public void getPagedRedFlagsByRequestType() {
    List<RedFlags> redFlagsList = RED_FLAGS;
    assertNotNull(redFlagsList);
    assertEquals(1, redFlagsList.size());

    when(myCareHubRedFlagDao.getPagedRedFlagsByRequestType(
            "TEST", redFlagsList.get(0).getRequestType(), 1, 3))
        .thenReturn(redFlagsList);

    List<RedFlags> redFlags =
        fakeRedFlagServiceImpl.getPagedRedFlagsByRequestType(
            "TEST", redFlagsList.get(0).getRequestType(), 1, 3);
    assertEquals(1, redFlags.size());
    assertNotNull(redFlags);
  }

  @Test
  public void saveRedFlagRequests() {
    List<RedFlags> redFlagsList = RED_FLAGS;
    assertNotNull(redFlagsList);
    assertEquals(1, redFlagsList.size());

    when(myCareHubRedFlagDao.saveRedFlagRequests(redFlagsList)).thenReturn(redFlagsList);

    List<RedFlags> redFlags = fakeRedFlagServiceImpl.saveRedFlagRequests(redFlagsList);
    assertEquals(1, redFlags.size());
    assertEquals(redFlags, redFlagsList);
    assertNotNull(redFlags);
  }

  @Test
  public void saveRedFlagRequests_saveOne() {
    RedFlags redFlag = RED_FLAGS.get(0);
    assertNotNull(redFlag);

    when(myCareHubRedFlagDao.saveRedFlagRequests(redFlag)).thenReturn(redFlag);

    RedFlags singleRedFlags = fakeRedFlagServiceImpl.saveRedFlagRequests(redFlag);
    assertEquals(singleRedFlags, redFlag);
    assertNotNull(singleRedFlags);
  }

  @Test
  public void getRedFlagRequestByMycarehubId() {
    RedFlags redFlag = RED_FLAGS.get(0);
    assertNotNull(redFlag);

    when(myCareHubRedFlagDao.getRedFlagRequestByMycarehubId(redFlag.getMycarehubId()))
        .thenReturn(redFlag);

    RedFlags singleRedFlags =
        fakeRedFlagServiceImpl.getRedFlagRequestByMycarehubId(redFlag.getMycarehubId());
    assertEquals(singleRedFlags, redFlag);
    assertNotNull(singleRedFlags);
  }

  @Test
  public void countRedFlagsByType() {
    RedFlags redFlag = RED_FLAGS.get(0);
    assertNotNull(redFlag);

    Number number = RED_FLAGS.size();

    when(myCareHubRedFlagDao.countRedFlagsByType("TEST", redFlag.getRequestType()))
        .thenReturn(number);

    Number redFlagsCount =
        fakeRedFlagServiceImpl.countRedFlagsByType("TEST", redFlag.getRequestType());
    assertEquals(1, redFlagsCount);
    assertNotNull(redFlagsCount);
  }

  @Test
  public void syncPatientRedFlagRequests_Null_Setting() {
    when(myCareHubSettingsService.getLatestMyCareHubSettingByType(PATIENT_RED_FLAGS_REQUESTS_POST))
        .thenReturn(null);

    fakeRedFlagServiceImpl.syncPatientRedFlagRequests();
  }

  @Test
  public void syncPatientRedFlagRequest() {
    when(myCareHubSettingsService.getLatestMyCareHubSettingByType(PATIENT_RED_FLAGS_REQUESTS_POST))
        .thenReturn(setting);

    List<RedFlags> redFlagsList = RED_FLAGS;
    assertEquals(1, redFlagsList.size());
    when(myCareHubRedFlagDao.getAllRedFlagRequestsByLastSyncDate(setting.getLastSyncTime()))
        .thenReturn(redFlagsList);

    fakeRedFlagServiceImpl.syncPatientRedFlagRequests();
  }

  @Test
  public void fetchPatientRedFlagRequests_null() {
    setting.setSettingType(PATIENT_RED_FLAGS_REQUESTS_GET);
    when(myCareHubSettingsService.getLatestMyCareHubSettingByType(PATIENT_RED_FLAGS_REQUESTS_GET))
        .thenReturn(null);

    fakeRedFlagServiceImpl.fetchPatientRedFlagRequests();
  }

  private static List<RedFlags> testRedFlagsFactory() {
    RedFlags redFlags = new RedFlags();
    redFlags.setId(1);
    redFlags.setCccNumber("1234");
    redFlags.setClientContact("+254711223344");
    redFlags.setClientName("test");
    redFlags.setMflCode("1234");
    redFlags.setRequestType("RED_FLAG");
    redFlags.setMycarehubId(String.valueOf(UUID.randomUUID()));
    redFlags.setUuid("bcbdaf68-3d36-4365-b575-4182d6749af0");

    return Collections.singletonList(redFlags);
  }
}
