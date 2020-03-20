package com.mapbox.android.core.location;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LocationEngineResultTest {

  private static final double TEST_LAT_LNG = 1.0;

  @Test
  public void checkNullIntent() {
    LocationEngineResult result = LocationEngineResult.extractResult(null);
    assertThat(result).isNull();
  }

  @Test
  public void passInvalidIntent() {
    Intent intent = mock(Intent.class);
    LocationEngineResult result = LocationEngineResult.extractResult(intent);
    assertThat(result).isNull();
  }

  @Test
  public void passValidIntent() {
    Location location = mock(Location.class);
    LocationEngineResult result = LocationEngineResult.extractResult(getValidIntent(location));
    assertThat(result).isNotNull();
    assertThat(result.getLastLocation()).isSameAs(location);
  }

  @Test
  public void passNullLocation() {
    LocationEngineResult result = LocationEngineResult.create((Location) null);
    assertForNullInput(result);
  }

  @Test
  public void passNullLocationList() {
    LocationEngineResult result = LocationEngineResult.create((List<Location>) null);
    assertForNullInput(result);
  }

  @Test
  public void passValidLocation() {
    LocationEngineResult result = LocationEngineResult.create(getValidLocation());
    assertForValidInput(result);
  }

  @Test
  public void passValidLocationList() {
    LocationEngineResult result = LocationEngineResult.create(Collections.singletonList(getValidLocation()));
    assertForValidInput(result);
  }

  private static Location getValidLocation() {
    Location location = mock(Location.class);
    when(location.getLatitude()).thenReturn(TEST_LAT_LNG);
    when(location.getLongitude()).thenReturn(TEST_LAT_LNG);
    return location;
  }

  private static void assertForNullInput(LocationEngineResult result) {
    assertThat(result).isNotNull();
    assertThat(result.getLocations()).isEmpty();
  }

  private static void assertForValidInput(LocationEngineResult result) {
    assertThat(result.getLocations()).isNotNull();
    assertThat(result.getLocations().size()).isEqualTo(1);
    assertThat(result.getLocations().get(0).getLatitude()).isEqualTo(TEST_LAT_LNG);
    assertThat(result.getLocations().get(0).getLongitude()).isEqualTo(TEST_LAT_LNG);
  }

  private static Intent getValidIntent(Location location) {
    Intent intent = mock(Intent.class);
    when(intent.hasExtra(LocationManager.KEY_LOCATION_CHANGED)).thenReturn(true);
    Bundle bundle = mock(Bundle.class);
    when(bundle.getParcelable(LocationManager.KEY_LOCATION_CHANGED)).thenReturn(location);
    when(intent.getExtras()).thenReturn(bundle);
    return intent;
  }
}
