package com.danilat.scorecards.acceptation.steps;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.danilat.scorecards.core.domain.account.Account;
import com.danilat.scorecards.core.domain.account.AccountRepository;
import com.danilat.scorecards.core.mothers.AccountMother;
import com.danilat.scorecards.core.usecases.accounts.RegisterAccount;
import com.danilat.scorecards.core.usecases.accounts.RegisterAccount.RegisterAccountParameters;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.domain.errors.Error;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;

public class AccountSteps {

  private String name;
  private String username;
  private String email;
  private String picture;

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private RegisterAccount registerAccount;
  private Account account;
  private Error errors;
  private PrimaryPort<Account> primaryPort = new PrimaryPort<Account>() {
    @Override
    public void success(Account response) {
      account = response;
      errors = null;
    }

    @Override
    public void error(Error errors) {
      AccountSteps.this.errors = errors;
      account = null;
    }
  };
  private RegisterAccountParameters params;

  @Given("a name {string}")
  public void aName(String name) {
    this.name = name;
  }

  @Given("a username {string}")
  public void aUsername(String username) {
    this.username = username;
  }

  @Given("an email {string}")
  public void anEmail(String email) {
    this.email = email;
  }

  @When("I register the account")
  public void iRegisterTheAccount() {
    params = new RegisterAccountParameters(username, name, email, picture);

    registerAccount.execute(primaryPort, params);
  }

  @Then("the account is successfully registered")
  public void theAccountIsSuccessfullyRegistered() {
    assertNotNull(account);
    assertNull(errors);
  }

  @Then("the account is not registered")
  public void theAccountIsNotRegistered() {
    assertNull(account);
    assertNotNull(errors);
  }

  @Given("an account with email {string} is present")
  public void anAccountWithEmailIsPresent(String email) {
    Account exitingAccount = AccountMother.anAccountWithEmail(email);
    accountRepository.save(exitingAccount);
  }

  @Given("another account with username {string} is present")
  public void anotherAccountWithUsernameIsPresent(String username) {
    Account exitingAccount = AccountMother.anAccountWithUsername(username);
    accountRepository.save(exitingAccount);
  }

}
