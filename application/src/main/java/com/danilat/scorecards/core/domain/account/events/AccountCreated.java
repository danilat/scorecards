package com.danilat.scorecards.core.domain.account.events;

import com.danilat.scorecards.core.domain.account.Account;
import com.danilat.scorecards.shared.events.DomainEvent;
import com.danilat.scorecards.shared.events.DomainEventId;
import java.time.Instant;

public class AccountCreated extends DomainEvent {

  private Account account;

  public AccountCreated(Instant happenedAt, DomainEventId eventId, Account account) {
    super(happenedAt, eventId);
    this.account = account;
  }

  public Account account() {
    return account;
  }
}
