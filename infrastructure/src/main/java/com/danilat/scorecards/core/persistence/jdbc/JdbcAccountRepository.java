package com.danilat.scorecards.core.persistence.jdbc;

import com.danilat.scorecards.core.domain.account.Account;
import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.account.AccountRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Primary
@Repository
public class JdbcAccountRepository extends JdbcBaseRepository<Account, AccountId> implements AccountRepository {

  @Autowired
  public JdbcAccountRepository(DataSource dataSource) {
    super(dataSource);
  }

  @Override
  public Optional<Account> findByUsername(String username) {
    SqlParameterSource params = new MapSqlParameterSource().addValue("username", username);
    return queryOne("SELECT * FROM accounts WHERE username = :username", params);
  }

  @Override
  public Optional<Account> findByEmail(String email) {
    SqlParameterSource params = new MapSqlParameterSource().addValue("email", email);
    return queryOne("SELECT * FROM accounts WHERE email = :email", params);
  }

  @Override
  public void save(Account account) {
    SqlParameterSource params = new MapSqlParameterSource().addValue("id", account.id().value())
        .addValue("name", account.name()).addValue("username", account.username()).addValue("email", account.email())
        .addValue("picture", account.picture());
    if (get(account.id()).isPresent()) {
      namedParameterJdbcTemplate
          .update(
              "UPDATE accounts SET name = :name, username = :username, email = :email, picture = :picture WHERE id = :id",
              params);
    } else {
      namedParameterJdbcTemplate
          .update(
              "INSERT INTO accounts (id, name, username, email, picture) VALUES (:id, :name, :username, :email, :picture)",
              params);
    }
  }

  @Override
  protected Account mapRow(ResultSet resultSet) throws SQLException {
    return new Account(new AccountId(resultSet.getString("id")), resultSet.getString("username"),
        resultSet.getString("name"), resultSet.getString("email"), resultSet.getString("picture"));
  }

  @Override
  protected String tableName() {
    return "accounts";
  }
}
