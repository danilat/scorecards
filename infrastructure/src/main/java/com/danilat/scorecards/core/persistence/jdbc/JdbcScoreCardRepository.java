package com.danilat.scorecards.core.persistence.jdbc;

import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.score.ScoreCard;
import com.danilat.scorecards.core.domain.score.ScoreCardId;
import com.danilat.scorecards.core.domain.score.ScoreCardRepository;
import com.danilat.scorecards.shared.domain.Sort;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Primary
@Repository
public class JdbcScoreCardRepository extends JdbcBaseRepository<ScoreCard, ScoreCardId> implements ScoreCardRepository {

  public JdbcScoreCardRepository(DataSource dataSource) {
    super(dataSource);
  }

  @Override
  protected ScoreCard mapRow(ResultSet resultSet) throws SQLException {
    ObjectMapper objectMapper = new ObjectMapper();
    Map<Integer, Integer> firstBoxerScores;
    Map<Integer, Integer> secondBoxerScores;
    try {
      firstBoxerScores = objectMapper
          .readValue(resultSet.getString("first_boxer_scores"), new TypeReference<Map<Integer, Integer>>() {
          });
      secondBoxerScores = objectMapper
          .readValue(resultSet.getString("second_boxer_scores"), new TypeReference<Map<Integer, Integer>>() {
          });
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
    Instant scoredAt = resultSet.getTimestamp("scored_at").toInstant();

    return new ScoreCard(new ScoreCardId(resultSet.getString("id")), new AccountId(resultSet.getString("account_id")),
        new FightId(resultSet.getString("fight_id")), new BoxerId(resultSet.getString("first_boxer_id")),
        new BoxerId(resultSet.getString("second_boxer_id")), firstBoxerScores, secondBoxerScores, scoredAt);
  }

  @Override
  protected String tableName() {
    return "scorecards";
  }

  @Override
  public Optional<ScoreCard> findByFightIdAndAccountId(FightId fightId, AccountId accountId) {
    SqlParameterSource params = new MapSqlParameterSource().addValue("fight_id", fightId.value())
        .addValue("account_id", accountId.value());
    return queryOne("SELECT * FROM scorecards WHERE fight_id = :fight_id AND account_id = :account_id", params);
  }

  @Override
  public Collection<ScoreCard> findAllByAccountId(AccountId accountId, Sort sort) {
    SqlParameterSource params = new MapSqlParameterSource().addValue("account_id", accountId.value());
    List<ScoreCard> scorecards = namedParameterJdbcTemplate
        .query(
            "SELECT * FROM scorecards WHERE account_id = :account_id ORDER BY " + toSnakeCase(sort.field()) + " " + sort
                .direction().value(),
            params,
            (resultSet, rowNum) -> mapRow(resultSet));
    return scorecards;
  }

  @Override
  public void save(ScoreCard scoreCard) {
    ObjectMapper objectMapper = new ObjectMapper();
    SqlParameterSource params;
    try {
      params = new MapSqlParameterSource().addValue("id", scoreCard.id().value())
          .addValue("fight_id", scoreCard.fightId().value())
          .addValue("account_id", scoreCard.accountId().value())
          .addValue("first_boxer_id", scoreCard.firstBoxerId().value())
          .addValue("second_boxer_id", scoreCard.secondBoxerId().value())
          .addValue("first_boxer_scores", objectMapper.writeValueAsString(scoreCard.firstBoxerScores()))
          .addValue("second_boxer_scores", objectMapper.writeValueAsString(scoreCard.secondBoxerScores()))
          .addValue("scored_at", Timestamp.from(scoreCard.scoredAt()));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    if (get(scoreCard.id()).isPresent()) {
      namedParameterJdbcTemplate.update(
          "UPDATE scorecards SET fight_id = :fight_id, account_id = :account_id, first_boxer_id = :first_boxer_id, second_boxer_id = :second_boxer_id, first_boxer_scores = cast(:first_boxer_scores AS JSON), second_boxer_scores = cast(:second_boxer_scores AS JSON), scored_at = :scored_at WHERE id = :id",
          params);
    } else {
      namedParameterJdbcTemplate.update(
          "INSERT INTO scorecards (id, fight_id, account_id, first_boxer_id, second_boxer_id, first_boxer_scores, second_boxer_scores, scored_at) VALUES (:id, :fight_id, :account_id, :first_boxer_id, :second_boxer_id, cast(:first_boxer_scores AS JSON), cast(:second_boxer_scores AS JSON), :scored_at)",
          params);
    }
  }
}
