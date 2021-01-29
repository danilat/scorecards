package com.danilat.scorecards.core.persistence.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class JdbcBaseRepository<E> {

  protected JdbcTemplate jdbcTemplate;

  public JdbcBaseRepository(DataSource dataSource) {
    jdbcTemplate = new JdbcTemplate(dataSource);
  }

  protected Optional<E> queryOne(String sql, Object[] params) {
    List<E> entities = jdbcTemplate
        .query(sql, params, (resultSet, rowNum) -> mapRow(resultSet));
    if (entities.isEmpty()) {
      return Optional.empty();
    }

    return Optional.of(entities.get(0));
  }

  protected abstract E mapRow(ResultSet resultSet) throws SQLException;
}
