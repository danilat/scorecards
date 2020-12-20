package com.danilat.scorecards.core.usecases.accounts;

import com.danilat.scorecards.core.domain.account.Account;
import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.account.AccountRepository;
import com.danilat.scorecards.core.usecases.accounts.RegisterAccount.RegisterAccountParameters;
import com.danilat.scorecards.shared.Clock;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.events.EventBus;
import com.danilat.scorecards.shared.usecases.UseCase;

public class RegisterAccount implements UseCase<Account, RegisterAccountParameters> {

  private final AccountRepository accountRepository;
  private final EventBus eventBus;
  private final Clock clock;

  public RegisterAccount(AccountRepository accountRepository, EventBus eventBus,
      Clock clock) {

    this.accountRepository = accountRepository;
    this.eventBus = eventBus;
    this.clock = clock;
  }

  @Override
  public void execute(PrimaryPort<Account> primaryPort, RegisterAccountParameters params) {
    Account account = Account
        .create(new AccountId(params.getUsername()), params.getUsername(), params.getName(), params.getEmail(),
            params.getPictureUrl(), clock.now());
    accountRepository.save(account);
    eventBus.publish(account.domainEvents());
    primaryPort.success(account);
  }

  public static class RegisterAccountParameters {

    private final String username;
    private final String name;
    private final String email;
    private final String pictureUrl;

    public RegisterAccountParameters(String username, String name, String email, String pictureUrl) {
      this.username = username;
      this.name = name;
      this.email = email;
      this.pictureUrl = pictureUrl;
    }

    public String getUsername() {
      return username;
    }

    public String getName() {
      return name;
    }

    public String getPictureUrl() {
      return pictureUrl;
    }

    public String getEmail() {
      return email;
    }
  }
}
