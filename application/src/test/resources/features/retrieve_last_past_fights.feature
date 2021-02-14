Feature: Retrieve last past fights

  Scenario: there is some fights are ordered chronologically descending
    Given exists a fight that happened "01/01/2021"
    And exists a fight that happened "02/01/2021"
    When I retrieve the last fights
    Then the fights are present
    And the fight that happened "02/01/2021" is first than the fight that happened "01/01/2021"

  Scenario: there is no fights
    Given there is no fights
    When I retrieve the last fights
    Then the fights are not present
