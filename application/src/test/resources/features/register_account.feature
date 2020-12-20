Feature: Register an account

  Scenario: successfully if all is ok
    Given a name "Dani Latorre"
    And a username "danilat"
    And an email "danilat@danilat.com"
    When I register the account
    Then the account is successfully registered

  Scenario: fails if the name is not present
    Given a name ""
    And a username "danilat"
    And an email "danilat@danilat.com"
    When I register the account
    Then the account is not registered

  Scenario: fails if the username is not present
    Given a name "Dani Latorre"
    And a username ""
    And an email "danilat@danilat.com"
    When I register the account
    Then the account is not registered

  Scenario: fails if the email is not present
    Given a name "Dani Latorre"
    And a username "danilat"
    And an email ""
    When I register the account
    Then the account is not registered

  Scenario: fails if the username is already used
    Given another account with username "danilat" is present
    And a name "Dani Latorre"
    And a username "danilat"
    And an email "danilat@danilat.com"
    When I register the account
    Then the account is not registered

  Scenario: fails if the email is already used
    Given an account with email "danilat@danilat.com" is present
    And a name "Dani Latorre"
    And a username "danilat"
    And an email "danilat@danilat.com"
    When I register the account
    Then the account is not registered
