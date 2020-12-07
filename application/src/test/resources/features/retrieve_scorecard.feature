Feature: Retrieve a scorecard by a fight and account

  Scenario: the scorecard exists
    Given an existing scorecard
    When I retrieve the existing scorecard by it's fight and account
    Then the scorecard is present

  Scenario: the scorecard not exists
    When I retrieve a scorecard by a fight and account that not exist
    Then the scorecard is not present