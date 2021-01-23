package com.danilat.scorecards.core.persistence.sql;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import com.danilat.scorecards.acceptation.steps.World;
import com.danilat.scorecards.core.CoreInitializer;
import com.danilat.scorecards.core.db.JdbcConfig;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(
    classes = {JdbcConfig.class})
@RunWith(SpringRunner.class)
public class DatabaseConnectionTest {
  @Autowired
  private DataSource dataSource;

  @Test
  public void shouldConnectToDatabase(){
    try {
      assertNotNull(dataSource.getConnection());
    } catch (SQLException exception) {
      fail("Can't connect to the database:" + exception.getMessage());
    }
  }
}
