package com.danilat.scorecards.core.usecases.accounts;

import com.danilat.scorecards.core.domain.account.Account;
import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.account.AccountRepository;
import com.danilat.scorecards.core.usecases.accounts.RegisterAccount.RegisterAccountParameters;
import com.danilat.scorecards.shared.Clock;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.domain.Error;
import com.danilat.scorecards.shared.domain.FieldError;
import com.danilat.scorecards.shared.domain.FieldErrors;
import com.danilat.scorecards.shared.events.EventBus;
import com.danilat.scorecards.shared.usecases.ValidatableParameters;
import com.danilat.scorecards.shared.usecases.UseCase;
import javax.validation.constraints.NotEmpty;

public class RegisterAccount implements UseCase<Account, RegisterAccountParameters> {

  private final AccountRepository accountRepository;
  private final EventBus eventBus;
  private final Clock clock;
  private FieldErrors errors;

  public RegisterAccount(AccountRepository accountRepository, EventBus eventBus,
      Clock clock) {

    this.accountRepository = accountRepository;
    this.eventBus = eventBus;
    this.clock = clock;
  }

  @Override
  public void execute(PrimaryPort<Account> primaryPort, RegisterAccountParameters params) {
    if (!validate(params)) {
      primaryPort.error(errors);
      return;
    }

    Account account = Account
        .create(new AccountId(params.getUsername()), params.getUsername(), params.getName(), params.getEmail(),
            params.getPictureUrl(), clock.now());
    accountRepository.save(account);
    eventBus.publish(account.domainEvents());
    primaryPort.success(account);
  }

  private boolean validate(RegisterAccountParameters params) {
    errors = new FieldErrors();
    errors.addAll(params.validate());
    if (accountRepository.findByUsername(params.getUsername()).isPresent()) {
      errors.add(new FieldError("username", new Error("The username is already used")));
    }
    if (accountRepository.findByEmail(params.getEmail()).isPresent()) {
      errors.add(new FieldError("email", new Error("The email is already used")));
    }
    return errors.isEmpty();
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
