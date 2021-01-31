package com.danilat.scorecards.core.persistence.jdbc;

import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.fight.Event;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Primary
@Repository
public class JdbcFightRepository extends JdbcBaseRepository<Fight, FightId> implements FightRepository {

  public JdbcFightRepository(DataSource dataSource) {
    super(dataSource);
  }

  @Override
  protected Fight mapRow(ResultSet resultSet) throws SQLException {
    Event event = new Event(resultSet.getDate("happen_at").toLocalDate(), resultSet.getString("place"));
    return new Fight(new FightId(resultSet.getString("id")), new BoxerId(resultSet.getString("first_boxer_id")),
        new BoxerId(resultSet.getString("second_boxer_id")), event, resultSet.getInt("number_of_rounds"));
  }

  @Override
  protected String tableName() {
    return "fights";
  }

  @Override
  public void save(Fight fight) {
    SqlParameterSource params = new MapSqlParameterSource().addValue("id", fight.id().value())
        .addValue("first_boxer_id", fight.firstBoxer().value())
        .addValue("second_boxer_id", fight.secondBoxer().value())
        .addValue("number_of_rounds", fight.numberOfRounds())
        .addValue("place", fight.event().place())
        .addValue("happen_at", fight.event().happenAt());
    if (get(fight.id()).isPresent()) {
      namedParameterJdbcTemplate.update(
          "UPDATE fights SET first_boxer_id = :first_boxer_id, second_boxer_id = :second_boxer_id, number_of_rounds = :number_of_rounds, place = :place, happen_at = :happen_at WHERE id = :id",
          params);
    } else {
      namedParameterJdbcTemplate.update(
          "INSERT INTO fights (id, first_boxer_id, second_boxer_id, number_of_rounds, place, happen_at) VALUES (:id, :first_boxer_id, :second_boxer_id, :number_of_rounds, :place, :happen_at)",
          params);
    }
  }
}
