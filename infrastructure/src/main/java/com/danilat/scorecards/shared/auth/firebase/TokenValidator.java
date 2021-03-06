package com.danilat.scorecards.shared.auth.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component()
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class TokenValidator {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  public TokenValidator() {
    try {
      FirebaseOptions options = FirebaseOptions.builder()
          .setCredentials(GoogleCredentials.getApplicationDefault())
          .build();
      if (FirebaseApp.getApps().isEmpty()) {
        FirebaseApp.initializeApp(options);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public UserFromToken validateToken(String idToken) {
    try {
      FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
      logger.debug("The token {} is valid", decodedToken);
      return new UserFromToken(decodedToken.getName(), decodedToken.getEmail(), decodedToken.getPicture());
    } catch (FirebaseAuthException e) {
      logger.error("Failing validating the token", e);
      return null;
    }
  }

  public static class UserFromToken {

    private String name;
    private String email;
    private String picture;

    public UserFromToken(String name, String email, String picture) {
      this.name = name;
      this.email = email;
      this.picture = picture;
    }

    public String getName() {
      return name;
    }

    public String getEmail() {
      return email;
    }

    public String getPicture() {
      return picture;
    }
  }
}
