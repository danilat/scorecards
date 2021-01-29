package com.danilat.scorecards.core.persistence.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public abstract class JdbcBaseRepository<E> {

  protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public JdbcBaseRepository(DataSource dataSource) {
    namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
  }

  protected Optional<E> queryOne(String sql, SqlParameterSource params) {
    List<E> entities = namedParameterJdbcTemplate
        .query(sql, params, (resultSet, rowNum) -> mapRow(resultSet));
    if (entities.isEmpty()) {
      return Optional.empty();
    }

    return Optional.of(entities.get(0));
  }

  protected abstract E mapRow(ResultSet resultSet) throws SQLException;
}
