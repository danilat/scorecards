package com.danilat.scorecards.core.persistence.fetchers;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.domain.fight.Event;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxers;
import com.danilat.scorecards.core.domain.fight.projections.FightWithBoxersFetcher;
import com.danilat.scorecards.shared.domain.Sort;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
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
  private FightRepository fightRepository;
  private BoxerRepository boxerRepository;

  @Autowired
  public JdbcFightWithBoxersFetcher(FightRepository fightRepository,
      BoxerRepository boxerRepository, DataSource dataSource) {
    this.fightRepository = fightRepository;
    this.boxerRepository = boxerRepository;
    namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
  }

  @Override
  public Optional<FightWithBoxers> get(FightId id) {
    Optional<Fight> optionalFight = fightRepository.get(id);
    if (!optionalFight.isPresent()) {
      return Optional.empty();
    }
    Fight fight = optionalFight.get();
    Boxer firstBoxer = boxerRepository.get(fight.firstBoxer()).get();
    Boxer secondBoxer = boxerRepository.get(fight.secondBoxer()).get();

    FightWithBoxers fightWithBoxers = new FightWithBoxers(fight.id(), fight.firstBoxer(), firstBoxer.name(),
        fight.secondBoxer(), secondBoxer.name(), fight.event().place(), fight.event().happenAt(),
        fight.numberOfRounds());
    return Optional.ofNullable(fightWithBoxers);
  }

  protected String toSnakeCase(String field) {
    return field.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
  }

  @Override
  public Collection<FightWithBoxers> findAllBefore(LocalDate today, Sort sort,
      int limit) {
    SqlParameterSource params = new MapSqlParameterSource().addValue("happenAt", today);
    return namedParameterJdbcTemplate.query(
        "SELECT * FROM fights WHERE happen_at <= :happenAt ORDER BY " + toSnakeCase(sort.field()) + " " + sort
            .direction().value(), params, (resultSet, rowNum) -> mapRow(resultSet));
  }

  private FightWithBoxers mapRow(ResultSet resultSet) throws SQLException {
    Event event = new Event(resultSet.getDate("happen_at").toLocalDate(), resultSet.getString("place"));
    Fight fight = new Fight(new FightId(resultSet.getString("id")), new BoxerId(resultSet.getString("first_boxer_id")),
        new BoxerId(resultSet.getString("second_boxer_id")), event, resultSet.getInt("number_of_rounds"));
    Boxer firstBoxer = boxerRepository.get(fight.firstBoxer()).get();
    Boxer secondBoxer = boxerRepository.get(fight.secondBoxer()).get();
    return new FightWithBoxers(fight.id(), fight.firstBoxer(), firstBoxer.name(),
        fight.secondBoxer(), secondBoxer.name(), fight.event().place(), fight.event().happenAt(),
        fight.numberOfRounds());
  }
}
