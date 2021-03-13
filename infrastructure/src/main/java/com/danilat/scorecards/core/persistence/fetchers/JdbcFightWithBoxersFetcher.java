package com.danilat.scorecards.core.persistence.fetchers;

import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.fight.Event;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxers;
import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxersFetcher;
import com.danilat.scorecards.shared.domain.Sort;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcFightWithBoxersFetcher implements
    FightWithBoxersFetcher {

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  public JdbcFightWithBoxersFetcher(DataSource dataSource) {
    namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
  }

  @Override
  public Optional<FightWithBoxers> get(FightId id) {
    SqlParameterSource params = new MapSqlParameterSource().addValue("id", id.value());
    List<FightWithBoxers> fightsWithBoxers = namedParameterJdbcTemplate.query(
        "SELECT f.*, fb.name as first_boxer_name, sb.name as second_boxer_name FROM fights f LEFT JOIN boxers fb ON f.first_boxer_id = fb.id LEFT JOIN boxers sb ON f.second_boxer_id = sb.id WHERE f.id = :id",
        params, (resultSet, rowNum) -> mapRow(resultSet));
    if (fightsWithBoxers.isEmpty()) {
      return Optional.empty();
    }
    return Optional.ofNullable(fightsWithBoxers.get(0));
  }

  protected String toSnakeCase(String field) {
    return field.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
  }

  @Override
  public Collection<FightWithBoxers> findAllBefore(LocalDate today, Sort sort,
      int limit) {
    SqlParameterSource params = new MapSqlParameterSource().addValue("happenAt", today);
    return namedParameterJdbcTemplate.query(
        "SELECT f.*, fb.name as first_boxer_name, sb.name as second_boxer_name FROM fights  f LEFT JOIN boxers fb ON f.first_boxer_id = fb.id LEFT JOIN boxers sb ON f.second_boxer_id = sb.id WHERE happen_at <= :happenAt ORDER BY "
            + toSnakeCase(sort.field()) + " " + sort
            .direction().value(), params, (resultSet, rowNum) -> mapRow(resultSet));
  }

  @Override
  public Collection<FightWithBoxers> all() {
    return namedParameterJdbcTemplate.query(
        "SELECT f.*, fb.name as first_boxer_name, sb.name as second_boxer_name FROM fights  f LEFT JOIN boxers fb ON f.first_boxer_id = fb.id LEFT JOIN boxers sb ON f.second_boxer_id = sb.id ORDER BY happen_at DESC",
            (resultSet, rowNum) -> mapRow(resultSet));
  }

  private FightWithBoxers mapRow(ResultSet resultSet) throws SQLException {
    Event event = new Event(resultSet.getDate("happen_at").toLocalDate(), resultSet.getString("place"));
    Fight fight = new Fight(new FightId(resultSet.getString("id")), new BoxerId(resultSet.getString("first_boxer_id")),
        new BoxerId(resultSet.getString("second_boxer_id")), event, resultSet.getInt("number_of_rounds"));
    return new FightWithBoxers(fight.id(), fight.firstBoxer(), resultSet.getString("first_boxer_name"),
        fight.secondBoxer(), resultSet.getString("second_boxer_name"), fight.event().place(), fight.event().happenAt(),
        fight.numberOfRounds());
  }
}
