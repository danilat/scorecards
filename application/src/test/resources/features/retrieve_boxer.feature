Feature: Retrieve a boxer

  Scenario: the boxer exists
    Given an existing boxer "ali"
    When I retrieve the boxer "ali"
    Then the boxer "ali" is present

  Scenario: the boxer not exists
    When I retrieve the boxer "balboa"
    Then the boxer "balboa" is not present