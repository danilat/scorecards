package com.danilat.scorecards.core.persistence.jdbc;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Primary
@Repository
public class JdbcBoxerRepository extends JdbcBaseRepository<Boxer, BoxerId> implements BoxerRepository {

  @Autowired
  public JdbcBoxerRepository(DataSource dataSource) {
    super(dataSource);
  }

  @Override
  public void save(Boxer boxer) {
    SqlParameterSource params = new MapSqlParameterSource().addValue("id", boxer.id().value())
        .addValue("name", boxer.name());
    if (get(boxer.id()).isPresent()) {
      namedParameterJdbcTemplate.update("UPDATE boxers SET name = :name WHERE id = :id", params);
    } else {
      namedParameterJdbcTemplate.update("INSERT INTO boxers (id, name) VALUES (:id, :name)", params);
    }
  }

  @Override
  protected Boxer mapRow(ResultSet resultSet) throws SQLException {
    return new Boxer(new BoxerId(resultSet.getString("id")), resultSet.getString("name"));
  }

  @Override
  protected String tableName() {
    return "boxers";
  }
}
