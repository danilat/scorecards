Feature: Retrieve a scorecard

  Scenario: the scorecard exists
    Given an existing scorecard
    When I retrieve the existing scorecard
    Then the scorecard is present

  Scenario: the scorecard not exists
    When I retrieve a non-existing scorecard
    Then the scorecard is not present