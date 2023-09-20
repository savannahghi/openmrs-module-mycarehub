package org.openmrs.module.mycarehub.api.service.impl;

import static org.junit.Assert.assertEquals;
import static org.openmrs.module.mycarehub.utils.Constants.MyCareHubSettingType.PATIENT_HEALTH_DIARY_GET;
import static org.powermock.api.mockito.PowerMockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.mycarehub.api.db.HealthDiaryDao;
import org.openmrs.module.mycarehub.api.rest.RestApiService;
import org.openmrs.module.mycarehub.api.service.MyCareHubSettingsService;
import org.openmrs.module.mycarehub.model.HealthDiary;
import org.openmrs.module.mycarehub.model.MyCareHubSetting;
import org.openmrs.module.mycarehub.utils.MyCareHubUtil;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class, MyCareHubUtil.class, RestApiService.class})
public class HealthDiaryServiceImplTest {

  @Mock private HealthDiaryDao fakeHealthDiaryDao;

  @InjectMocks private HealthDiaryServiceImpl healthDiaryImpl;

  private static Date currentDate;

  private static final String searchString = "RED_FLAG";

  @Mock public static MyCareHubSettingsService myCareHubSettingsService;

  @Before
  public void setUp() {
    myCareHubSettingsService = mock(MyCareHubSettingsService.class);
    MyCareHubSetting setting = mock(MyCareHubSetting.class);
    currentDate = new Date();

    mockStatic(Context.class);
    mockStatic(MyCareHubUtil.class);
    when(Context.getService(MyCareHubSettingsService.class)).thenReturn(myCareHubSettingsService);

    AdministrationService administrationService = mock(AdministrationService.class);
  }

  @Test
  public void testSaveHealthDiaries() {
    HealthDiary entryOne = createHealthDiary(currentDate, 1);
    HealthDiary entryTwo = createHealthDiary(currentDate, 2);

    List<HealthDiary> healthDiaries = Arrays.asList(entryOne, entryTwo);
    when(fakeHealthDiaryDao.saveHealthDiaries(healthDiaries)).thenReturn(healthDiaries);

    List<HealthDiary> savedHealthDiaryEntries = healthDiaryImpl.saveHealthDiaries(healthDiaries);
    assertEquals(2, savedHealthDiaryEntries.size());
  }

  @Test
  public void testSaveHealthDiaries_EmptyList() {
    List<HealthDiary> healthDiaries = Collections.emptyList();
    when(fakeHealthDiaryDao.saveHealthDiaries(healthDiaries)).thenReturn(healthDiaries);

    List<HealthDiary> savedHealthDiaryEntries = healthDiaryImpl.saveHealthDiaries(healthDiaries);
    assertEquals(0, savedHealthDiaryEntries.size());
  }

  @Test
  public void testSaveHealthDiaries_SingleEntry() {
    HealthDiary entryOne = createHealthDiary(currentDate, 1);
    List<HealthDiary> healthDiaries = Collections.singletonList(entryOne);

    when(fakeHealthDiaryDao.saveHealthDiaries(healthDiaries)).thenReturn(healthDiaries);

    List<HealthDiary> savedHealthDiaryEntries = healthDiaryImpl.saveHealthDiaries(healthDiaries);
    assertEquals(1, savedHealthDiaryEntries.size());
  }

  @Test
  public void testCountHealthDiaries() {
    Number healthDiariesCount = 5;

    when(fakeHealthDiaryDao.countHealthDiaries(searchString)).thenReturn(healthDiariesCount);

    Number healthDiaries = healthDiaryImpl.countHealthDiaries(searchString);
    assertEquals(5, healthDiaries);
  }

  @Test
  public void testGetPagedHealthDiaries() {
    Integer pageNumber = 1;
    Integer pageSize = 5;

    HealthDiary entryOne = createHealthDiary(currentDate, 1);
    HealthDiary entryTwo = createHealthDiary(currentDate, 2);

    List<HealthDiary> pagedHealthDiaries = Arrays.asList(entryOne, entryTwo);
    when(fakeHealthDiaryDao.getPagedHealthDiaries(searchString, pageNumber, pageSize))
        .thenReturn(pagedHealthDiaries);

    List<HealthDiary> fetchedHealthDiaries =
        healthDiaryImpl.getPagedHealthDiaries(searchString, pageNumber, pageSize);
    assertEquals(2, fetchedHealthDiaries.size());
  }

  @Test
  public void fetchPatientHealthDiariesNullSetting() {
    when(myCareHubSettingsService.getLatestMyCareHubSettingByType(PATIENT_HEALTH_DIARY_GET))
        .thenReturn(null);

    healthDiaryImpl.fetchPatientHealthDiaries();
  }

  @Test
  public void fetchPatientHealthDiaries() {
    MyCareHubSetting myCareHubSetting = new MyCareHubSetting();
    myCareHubSetting.setSettingType(PATIENT_HEALTH_DIARY_GET);
    myCareHubSetting.setLastSyncTime(currentDate);

    when(myCareHubSettingsService.getLatestMyCareHubSettingByType(PATIENT_HEALTH_DIARY_GET))
        .thenReturn(myCareHubSetting);

    JsonArray jsonArray = getJsonElements();

    when(MyCareHubUtil.getPatientHealthDiaries(Mockito.any(Date.class), Mockito.any(Date.class)))
        .thenReturn(jsonArray);

    healthDiaryImpl.fetchPatientHealthDiaries();
  }

  private static JsonArray getJsonElements() {
    JsonArray jsonArray = new JsonArray();

    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("cccNumber", "1234567890");
    jsonObject.addProperty("clientName", "testClient");
    jsonObject.addProperty("phoneNumber", "0711223344");
    jsonObject.addProperty("mood", "test mood");
    jsonObject.addProperty("note", "test ");
    jsonObject.addProperty("entryType", "HEALTH_DIARY");
    jsonObject.addProperty("createdAt", String.valueOf(currentDate));
    jsonObject.addProperty("sharedAt", String.valueOf(currentDate));

    jsonArray.add(jsonObject);
    return jsonArray;
  }

  // Helper method to create a HealthDiary object
  private static HealthDiary createHealthDiary(Date date, int id) {
    HealthDiary healthDiary = new HealthDiary();
    healthDiary.setId(id);
    healthDiary.setCccNumber("123456");
    healthDiary.setMood("VERY_SAD");
    healthDiary.setNote("I am very sad");
    healthDiary.setDateRecorded(date);
    healthDiary.setEntryType("health_diary");
    healthDiary.setSharedOn(date);
    healthDiary.setClientContact("+254711223344");
    healthDiary.setClientName("test");

    return healthDiary;
  }
}
