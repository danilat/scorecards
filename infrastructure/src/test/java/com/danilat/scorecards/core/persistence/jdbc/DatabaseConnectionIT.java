package com.danilat.scorecards.core.persistence.jdbc;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import javax.sql.DataSource;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(
    classes = {JdbcConfig.class, SimpleMeterRegistry.class})
@RunWith(SpringRunner.class)
public class DatabaseConnectionIT {

  @Autowired
  private DataSource dataSource;

  @Test
  public void shouldConnectToDatabase() {
    try {
      assertNotNull(dataSource.getConnection());
    } catch (SQLException exception) {
      fail("Can't connect to the database:" + exception.getMessage());
    }
  }
}
