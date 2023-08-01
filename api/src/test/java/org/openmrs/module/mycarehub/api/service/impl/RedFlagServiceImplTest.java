package org.openmrs.module.mycarehub.api.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openmrs.module.mycarehub.api.db.MyCareHubRedFlagDao;
import org.openmrs.module.mycarehub.model.RedFlags;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RedFlagServiceImplTest {
    @Mock
    MyCareHubRedFlagDao myCareHubRedFlagDao;

    @InjectMocks
    RedFlagServiceImpl fakeRedFlagServiceImpl;

    private static final List<RedFlags> RED_FLAGS = testRedFlagsFactory();
    private static Date date = null;

    @Before
    public void setUp() {
        date = new Date();
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
    public void getRedFlagByUuid(){
        RedFlags redFlag = RED_FLAGS.get(0);
        assertNotNull(redFlag);

        when(myCareHubRedFlagDao.getRedFlagByUuid(redFlag.getUuid())).thenReturn(redFlag);

        RedFlags singleRedFlag = fakeRedFlagServiceImpl.getRedFlagByUuid(redFlag.getUuid());
        assertEquals(redFlag, singleRedFlag);
    }

    @Test
    public void getAllRedFlagRequestsByLastSyncDate(){
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

        when(myCareHubRedFlagDao.getPagedRedFlagsByRequestType("TEST",redFlagsList.get(0).getRequestType(), 1, 3)).thenReturn(redFlagsList);

        List<RedFlags> redFlags = fakeRedFlagServiceImpl.getPagedRedFlagsByRequestType("TEST", redFlagsList.get(0).getRequestType(), 1, 3);
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

        when(myCareHubRedFlagDao.getRedFlagRequestByMycarehubId(redFlag.getMycarehubId())).thenReturn(redFlag);

        RedFlags singleRedFlags = fakeRedFlagServiceImpl.getRedFlagRequestByMycarehubId(redFlag.getMycarehubId());
        assertEquals(singleRedFlags, redFlag);
        assertNotNull(singleRedFlags);
    }

    @Test
    public void countRedFlagsByType() {
        RedFlags redFlag = RED_FLAGS.get(0);
        assertNotNull(redFlag);

        Number number = RED_FLAGS.size();

        when(myCareHubRedFlagDao.countRedFlagsByType("TEST", redFlag.getRequestType())).thenReturn(number);

        Number redFlagsCount = fakeRedFlagServiceImpl.countRedFlagsByType("TEST", redFlag.getRequestType());
        assertEquals(1, redFlagsCount);
        assertNotNull(redFlagsCount);
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

        return Arrays.asList(redFlags);
    }
}