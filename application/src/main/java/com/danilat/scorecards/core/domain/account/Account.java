package com.danilat.scorecards.core.domain.account;

import com.danilat.scorecards.shared.domain.Entity;
import com.danilat.scorecards.shared.events.DomainEventId;
import java.time.Instant;

public class Account extends Entity<AccountId> {

  private String username;
  private String name;
  private String picture;
  private String email;

  public Account(AccountId id, String username, String name, String email, String picture) {
    super(id);
    this.username = username;
    this.name = name;
    this.email = email;
    this.picture = picture;
  }

  public String username() {
    return username;
  }


  public String name() {
    return name;
  }

  public String picture() {
    return picture;
  }

  public String email() {
    return email;
  }

  public static Account create(AccountId id, String username, String name, String email, String picture,
      Instant happenedAt) {
    Account account = new Account(id, username, name, email, picture);
    account
        .addDomainEvent(new AccountCreated(happenedAt, new DomainEventId(account.id().value() + happenedAt), account));
    return account;
  }
}
