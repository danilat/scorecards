package com.danilat.scorecards.shared;

import static org.junit.Assert.*;

import org.junit.Test;

public class UniqueIdGeneratorTest {
  @Test
  public void theNextIdIsUnique(){
    UniqueIdGenerator uniqueIdGenerator = new UniqueIdGenerator();

    String firstValue = uniqueIdGenerator.next();
    String secondValue = uniqueIdGenerator.next();

    assertNotEquals(firstValue, secondValue);
  }
}