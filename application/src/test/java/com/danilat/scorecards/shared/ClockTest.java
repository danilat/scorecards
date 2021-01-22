package com.danilat.scorecards.shared;

import static org.junit.Assert.assertNotNull;

import java.time.Instant;
import org.junit.Test;

public class ClockTest {

  @Test
  public void nowGetsADate() {
    Clock clock = new Clock();

    Instant now = clock.now();

    assertNotNull(now);
  }
}