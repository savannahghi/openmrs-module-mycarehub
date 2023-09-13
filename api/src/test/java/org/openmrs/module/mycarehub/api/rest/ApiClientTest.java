package org.openmrs.module.mycarehub.api.rest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.mycarehub.utils.MyCareHubUtil;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import retrofit2.Retrofit;

@RunWith(PowerMockRunner.class)
@PrepareForTest({
  RestApiService.class,
  Retrofit.class,
  Context.class,
  MyCareHubUtil.class,
  ApiClient.class
})
@PowerMockIgnore("javax.net.ssl.*")
public class ApiClientTest {

  @Mock private MyCareHubUtil myCareHubUtil;

  @Before
  public void setUp() {
    mockStatic(ApiClient.class);
    mockStatic(RestApiService.class);
    AdministrationService administrationService = mock(AdministrationService.class);

    mockStatic(Context.class);
    PowerMockito.when(Context.getAdministrationService()).thenReturn(administrationService);

    myCareHubUtil = mock(MyCareHubUtil.class);
  }

  @Test
  public void testGetRestServiceWithValidApiUrl() {
    when(MyCareHubUtil.getApiUrl()).thenReturn("https://example.com");

    RestApiService restApiService = ApiClient.getRestService();
  }

  @Test
  public void testGetRestServiceWithInvalidApiUrl() {
    when(MyCareHubUtil.getApiUrl()).thenReturn("invalid_url");

    RestApiService restApiService = ApiClient.getRestService();

    assertNull(restApiService);
  }
}
