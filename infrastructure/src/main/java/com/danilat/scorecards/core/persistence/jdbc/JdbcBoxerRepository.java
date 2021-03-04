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
        .addValue("name", boxer.name()).addValue("alias", boxer.alias()).addValue("boxrec_url", boxer.boxrecUrl());
    if (get(boxer.id()).isPresent()) {
      namedParameterJdbcTemplate
          .update("UPDATE boxers SET name = :name, alias = :alias, boxrec_url = :boxrec_url WHERE id = :id", params);
    } else {
      namedParameterJdbcTemplate
          .update("INSERT INTO boxers (id, name, alias, boxrec_url) VALUES (:id, :name, :alias, :boxrec_url)", params);
    }
  }

  @Override
  protected Boxer mapRow(ResultSet resultSet) throws SQLException {
    return new Boxer(new BoxerId(resultSet.getString("id")), resultSet.getString("name"), resultSet.getString("alias"),
        resultSet.getString("boxrec_url"));
  }

  @Override
  protected String tableName() {
    return "boxers";
  }
}
