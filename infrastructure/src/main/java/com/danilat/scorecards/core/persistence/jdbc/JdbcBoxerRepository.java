package com.danilat.scorecards.core.persistence.jdbc;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Primary
@Repository
public class JdbcBoxerRepository implements BoxerRepository {

  private JdbcTemplate jdbcTemplate;

  @Autowired
  public JdbcBoxerRepository(DataSource dataSource) {
    jdbcTemplate = new JdbcTemplate(dataSource);
  }

  @Override
  public void save(Boxer boxer) {
    if (get(boxer.id()).isPresent()) {
      jdbcTemplate.update("UPDATE boxers SET name = ? WHERE id = ?", boxer.name(), boxer.id().value());
    } else {
      jdbcTemplate.update("INSERT INTO boxers (id, name) VALUES (?, ?)", boxer.id().value(), boxer.name());
    }
  }

  @Override
  public Optional<Boxer> get(BoxerId id) {
    List<Boxer> boxers = jdbcTemplate
        .query("SELECT * FROM boxers WHERE id = ?", new Object[]{id.value()},
            (resultSet, rowNum) -> mapRow(resultSet));
    if(boxers.isEmpty())
      return Optional.empty();

    return Optional.of(boxers.get(0));
  }

  @Override
  public Collection<Boxer> all() {
    List<Boxer> boxers = jdbcTemplate.query("SELECT * FROM boxers", (resultSet, rowNum) -> mapRow(resultSet));
    return boxers;
  }

  private Boxer mapRow(ResultSet resultSet) throws SQLException {
    return new Boxer(new BoxerId(resultSet.getString("id")), resultSet.getString("name"));
  }

  @Override
  public void clear() {
    jdbcTemplate.update("DELETE FROM boxers");
  }
}
