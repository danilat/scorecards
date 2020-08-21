package com.danilat.scorecards.shared;

import static org.junit.Assert.*;

import java.time.LocalDate;
import org.junit.Test;

public class ClockTest {

  @Test
  public void nowGetsADate() {
    Clock clock = new Clock();

    LocalDate now = clock.now();

    assertNotNull(now);
  }
}