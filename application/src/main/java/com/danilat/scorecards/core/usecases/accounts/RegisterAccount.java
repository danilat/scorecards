package com.danilat.scorecards.core.usecases.accounts;

import com.danilat.scorecards.core.domain.account.Account;
import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.account.AccountRepository;
import com.danilat.scorecards.core.usecases.accounts.RegisterAccount.RegisterAccountParameters;
import com.danilat.scorecards.shared.Clock;
import com.danilat.scorecards.shared.domain.errors.SimpleError;
import com.danilat.scorecards.shared.domain.errors.FieldErrors;
import com.danilat.scorecards.shared.events.EventBus;
import com.danilat.scorecards.shared.usecases.ValidatableParameters;
import com.danilat.scorecards.shared.usecases.WriteUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotEmpty;

public class RegisterAccount extends WriteUseCase<Account, RegisterAccountParameters> {

  private final AccountRepository accountRepository;
  private final Clock clock;
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  public RegisterAccount(AccountRepository accountRepository, EventBus eventBus,
      Clock clock) {
    super(eventBus);
    this.accountRepository = accountRepository;
    this.clock = clock;
  }

  @Override
  public Account executeWhenValid(RegisterAccountParameters params) {
    String username = params.getUsername().toLowerCase();
    Account account = Account
        .create(new AccountId(username), username, params.getName(), params.getEmail(),
            params.getPictureUrl(), clock.now());
    accountRepository.save(account);
    logger.debug("Account saved {}", account);
    return account;
  }

  protected FieldErrors validate(RegisterAccountParameters params) {
    FieldErrors errors = new FieldErrors();
    errors.addAll(params.validate());
    if (accountRepository.findByUsername(params.getUsername()).isPresent()) {
      errors.add("username", new SimpleError("The username is already used"));
    }
    if (accountRepository.findByEmail(params.getEmail()).isPresent()) {
      errors.add("email", new SimpleError("The email is already used"));
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
