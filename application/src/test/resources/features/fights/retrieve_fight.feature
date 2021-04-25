Feature: Retrieve a fight

  Scenario: the fight exists
    Given an existing fight
    When I retrieve the existing fight
    Then the fight is present

  Scenario: the fight not exists
    When I retrieve a non-existing fight
    Then the fight is not present