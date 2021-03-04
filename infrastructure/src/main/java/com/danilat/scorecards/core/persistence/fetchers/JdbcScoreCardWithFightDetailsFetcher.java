package com.danilat.scorecards.core.persistence.fetchers;

import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.score.BoxerScores;
import com.danilat.scorecards.core.domain.score.ScoreCardId;
import com.danilat.scorecards.core.domain.score.projections.ScoreCardWithFightDetails;
import com.danilat.scorecards.core.domain.score.projections.ScoreCardWithFightDetailsFetcher;
import com.danilat.scorecards.core.persistence.mappers.RawToScores;
import com.danilat.scorecards.shared.domain.Sort;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Collection;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcScoreCardWithFightDetailsFetcher implements ScoreCardWithFightDetailsFetcher {

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  public JdbcScoreCardWithFightDetailsFetcher(DataSource dataSource) {
    namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
  }

  @Override
  public Collection<ScoreCardWithFightDetails> findAllByAccountId(AccountId accountId, Sort sort) {
    SqlParameterSource params = new MapSqlParameterSource().addValue("accountId", accountId.value());
    return namedParameterJdbcTemplate.query(
        "SELECT f.*, s.id as scorecard_id, s.account_id as account_id, s.first_boxer_scores as first_boxer_scores,"
            + " s.second_boxer_scores as second_boxer_scores, s.scored_at as scored_at, fb.name as first_boxer_name, sb.name as second_boxer_name FROM fights f INNER JOIN boxers fb ON f.first_boxer_id = fb.id INNER JOIN boxers sb ON f.second_boxer_id = sb.id INNER JOIN scorecards s ON f.id = s.fight_id WHERE s.account_id = :accountId",
        params, (resultSet, rowNum) -> mapRow(resultSet));
  }

  private ScoreCardWithFightDetails mapRow(ResultSet resultSet) throws SQLException {
    FightId fightId = new FightId(resultSet.getString("id"));
    BoxerId firstBoxerId = new BoxerId(resultSet.getString("first_boxer_id"));
    BoxerId secondBoxerId = new BoxerId(resultSet.getString("second_boxer_id"));
    ScoreCardId scoreCardId = new ScoreCardId(resultSet.getString("scorecard_id"));
    AccountId accountId = new AccountId(resultSet.getString("account_id"));

    BoxerScores firstBoxerScores = RawToScores.map(resultSet.getString("first_boxer_scores"), firstBoxerId);
    BoxerScores secondBoxerScores = RawToScores.map(resultSet.getString("second_boxer_scores"), secondBoxerId);
    Instant scoredAt = resultSet.getTimestamp("scored_at").toInstant();

    return new ScoreCardWithFightDetails(scoreCardId, fightId, resultSet.getString("place"),
        resultSet.getDate("happen_at").toLocalDate(), resultSet.getInt("number_of_rounds"),
        resultSet.getString("first_boxer_name"), resultSet.getString("second_boxer_name"),
        firstBoxerScores, secondBoxerScores, accountId, scoredAt);
  }
}
