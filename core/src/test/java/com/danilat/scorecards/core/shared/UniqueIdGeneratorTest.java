package com.danilat.scorecards.core.shared;

import static org.junit.Assert.*;

import org.junit.Test;

public class UUIDGeneratorTest {
  @Test
  public void theNextUuidIsUnique(){
    UUIDGenerator uuidGenerator = new UUIDGenerator();

    String firstValue = uuidGenerator.next();
    String secondValue = uuidGenerator.next();

    assertNotEquals(firstValue, secondValue);
  }
}