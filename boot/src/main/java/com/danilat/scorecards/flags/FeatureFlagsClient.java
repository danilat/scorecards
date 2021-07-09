package com.danilat.scorecards.flags;

import com.configcat.ConfigCatClient;
import com.configcat.User;
import com.danilat.scorecards.core.domain.account.Account;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ComponentScan
public class FeatureFlagsClient {
  private ConfigCatClient client;

  public FeatureFlagsClient(@Value("${CONFIG_CAT_KEY}") String configCatKey) {
    this.client = ConfigCatClient.newBuilder().maxWaitTimeForSyncCallsInSeconds(5).build(configCatKey);
  }

  public boolean showTotalScoresTopOfFightScoreSection(Account account) {
    Map<String, String> customAttributes = new HashMap<>();
    customAttributes.put("name", account.name());
    customAttributes.put("isEditor", String.valueOf(account.isEditor()));
    User configCatUser = User.newBuilder().email(account.email()).custom(customAttributes).build(account.username());
    return client.getValue(Boolean.class, "showTotalScoresOnTopOfFightScoreSection", configCatUser, false);
  }
}
