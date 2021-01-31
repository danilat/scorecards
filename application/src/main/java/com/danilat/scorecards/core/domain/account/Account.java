package com.danilat.scorecards.core.domain.account;

import com.danilat.scorecards.shared.domain.Entity;
import com.danilat.scorecards.shared.events.DomainEventId;
import java.time.Instant;
import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    Account account = (Account) o;
    return Objects.equals(username, account.username) &&
        Objects.equals(name, account.name) &&
        Objects.equals(picture, account.picture) &&
        Objects.equals(email, account.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), username, name, picture, email);
  }

  @Override
  public String toString() {
    return "Account{" +
        "username='" + username + '\'' +
        ", name='" + name + '\'' +
        ", picture='" + picture + '\'' +
        ", email='" + email + '\'' +
        ", id=" + id +
        '}';
  }
}
