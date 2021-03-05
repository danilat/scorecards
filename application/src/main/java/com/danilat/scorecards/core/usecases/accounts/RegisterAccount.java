package com.danilat.scorecards.core.usecases.accounts;

import com.danilat.scorecards.core.domain.account.Account;
import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.account.AccountRepository;
import com.danilat.scorecards.core.usecases.accounts.RegisterAccount.RegisterAccountParameters;
import com.danilat.scorecards.shared.Clock;
import com.danilat.scorecards.shared.domain.Error;
import com.danilat.scorecards.shared.domain.FieldErrors;
import com.danilat.scorecards.shared.events.EventBus;
import com.danilat.scorecards.shared.usecases.ValidatableParameters;
import com.danilat.scorecards.shared.usecases.WriteUseCase;
import javax.validation.constraints.NotEmpty;

public class RegisterAccount extends WriteUseCase<Account, RegisterAccountParameters> {

  private final AccountRepository accountRepository;
  private final Clock clock;

  public RegisterAccount(AccountRepository accountRepository, EventBus eventBus,
      Clock clock) {
    super(eventBus);
    this.accountRepository = accountRepository;
    this.clock = clock;
  }

  @Override
  public Account executeWhenValid(RegisterAccountParameters params) {
    Account account = Account
        .create(new AccountId(params.getUsername()), params.getUsername(), params.getName(), params.getEmail(),
            params.getPictureUrl(), clock.now());
    accountRepository.save(account);
    return account;
  }

  protected FieldErrors validate(RegisterAccountParameters params) {
    FieldErrors errors = new FieldErrors();
    errors.addAll(params.validate());
    if (accountRepository.findByUsername(params.getUsername()).isPresent()) {
      errors.add("username", new Error("The username is already used"));
    }
    if (accountRepository.findByEmail(params.getEmail()).isPresent()) {
      errors.add("email", new Error("The email is already used"));
    }
    return errors;
  }

  public static class RegisterAccountParameters extends ValidatableParameters {

    @NotEmpty(message = "username is mandatory")
    private final String username;
    @NotEmpty(message = "name is mandatory")
    private final String name;
    @NotEmpty(message = "email is mandatory")
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
