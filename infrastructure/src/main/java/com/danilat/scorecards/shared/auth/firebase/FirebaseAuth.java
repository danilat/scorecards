package com.danilat.scorecards.shared.auth.firebase;

import com.danilat.scorecards.core.domain.account.Account;
import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.account.AccountRepository;
import com.danilat.scorecards.shared.Auth;

import java.util.Optional;

import com.danilat.scorecards.shared.domain.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FirebaseAuth implements Auth {

  @Autowired
  private AccountRepository accountRepository;
  @Autowired
  private TokenValidator tokenValidator;
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  @Override
  public AccountId currentAccountId(String accessToken) {
    Optional<Account> optionalAccount = currentOptionalAccount(accessToken);
    return optionalAccount.map(Entity::id).orElse(null);
  }

  @Override
  public Optional<Account> currentOptionalAccount(String accessToken) {
    if (accessToken == null || accessToken.equals("")) {
      return Optional.empty();
    }
    TokenValidator.UserFromToken user = tokenValidator.validateToken(accessToken);
    if (user == null) {
      logger.debug("The access token {} is not valid", accessToken);
      return Optional.empty();
    }
    logger.debug("User with email {} found", user.getEmail());
    return accountRepository.findByEmail(user.getEmail());
  }
}
