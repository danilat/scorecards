package com.danilat.scorecards.core.db;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@ComponentScan
public class JdbcConfig {

  @Value("${DB_NAME}")
  private String databaseName;
  @Value("${DB_HOST}")
  private String databaseHost;
  @Value("${DB_PORT}")
  private String databasePort;
  @Value("${DB_USER}")
  private String databaseUser;
  @Value("${DB_PASSWORD}")
  private String databasePassword;

  @Bean
  public DataSource postgresDataSource() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName("org.postgresql.Driver");
    dataSource.setUrl("jdbc:postgresql://" + databaseHost + ":" + databasePort + "/" + databaseName);
    dataSource.setUsername(databaseUser);
    dataSource.setPassword(databasePassword);
    return dataSource;
  }
}
