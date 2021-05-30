package com.danilat.scorecards.shared.auth.firebase;

import com.danilat.scorecards.core.domain.account.Account;
import com.danilat.scorecards.core.domain.account.AccountId;
import com.danilat.scorecards.core.domain.account.AccountRepository;
import com.danilat.scorecards.shared.Auth;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FirebaseAuth implements Auth {

  @Autowired
  private AccountRepository accountRepository;
  @Autowired
  private TokenValidator tokenValidator;

  @Override
  public AccountId currentAccountId(String accessToken) {
    Optional<Account> optionalAccount = currentAccount(accessToken);
    if (!optionalAccount.isPresent()) {
      return null;
    }
    return optionalAccount.get().id();
  }

  @Override
  public Optional<Account> currentAccount(String accessToken) {
    if (accessToken == null || accessToken.equals("")) {
      return Optional.empty();
    }
    TokenValidator.UserFromToken user = tokenValidator.validateToken(accessToken);
    if (user == null) {
      return Optional.empty();
    }
    return accountRepository.findByEmail(user.getEmail());
  }
}
