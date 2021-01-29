package com.danilat.scorecards.core.persistence.jdbc;

import com.danilat.scorecards.core.domain.account.Account;
import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.account.AccountRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Primary
@Repository
public class JdbcAccountRepository extends JdbcBaseRepository implements AccountRepository {

  @Autowired
  public JdbcAccountRepository(DataSource dataSource) {
    super(dataSource);
  }

  @Override
  public Optional<Account> findByUsername(String username) {
    return queryOne("SELECT * FROM accounts WHERE username = ?", new Object[]{username});
  }

  @Override
  public Optional<Account> findByEmail(String email) {
    return queryOne("SELECT * FROM accounts WHERE email = ?", new Object[]{email});
  }

  @Override
  public void save(Account account) {
    if (get(account.id()).isPresent()) {
      jdbcTemplate
          .update("UPDATE accounts SET name = ?, username = ?, email = ?, picture = ? WHERE id = ?", account.name(),
              account.username(), account.email(), account.picture(), account.id().value());
    } else {
      jdbcTemplate
          .update("INSERT INTO accounts (id, name, username, email, picture) VALUES (?, ?, ?, ?, ?)",
              account.id().value(),
              account.name(), account.username(), account.email(), account.picture());
    }
  }

  @Override
  public Optional<Account> get(AccountId id) {
    return queryOne("SELECT * FROM accounts WHERE id = ?", new Object[]{id.value()});
  }

  protected Account mapRow(ResultSet resultSet) throws SQLException {
    return new Account(new AccountId(resultSet.getString("id")), resultSet.getString("username"),
        resultSet.getString("name"), resultSet.getString("email"), resultSet.getString("picture"));
  }

  @Override
  public Collection<Account> all() {
    List<Account> accounts = jdbcTemplate.query("SELECT * FROM accounts", (resultSet, rowNum) -> mapRow(resultSet));
    return accounts;
  }

  @Override
  public void clear() {
    jdbcTemplate.update("DELETE FROM accounts");
  }
}
