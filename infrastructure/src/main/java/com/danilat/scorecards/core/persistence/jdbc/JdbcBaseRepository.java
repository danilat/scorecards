package com.danilat.scorecards.core.persistence.jdbc;

import com.danilat.scorecards.shared.domain.Entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public abstract class JdbcBaseRepository<E extends Entity> {

  protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public JdbcBaseRepository(DataSource dataSource) {
    namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
  }

  public String toSnakeCase(String field){
    return field.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
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
