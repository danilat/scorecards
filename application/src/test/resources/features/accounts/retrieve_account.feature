Feature: Retrieve an account by username

  Scenario: the account exists
    Given an existing account "danilat"
    When I retrieve the account "danilat"
    Then the account "danilat" is present

  Scenario: the account not exists
    When I retrieve the account "joanmiro"
    Then the account "joanmiro" is not present