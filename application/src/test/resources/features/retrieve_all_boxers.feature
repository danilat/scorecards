Feature: Retrieve all boxers

  Scenario: has some boxers if all is ok
    Given an existing boxer called "Manny Pacquiao"
    And an existing boxer called "Tyson Fury"
    When I retrieve all the boxers
    Then the boxer called "Manny Pacquiao" is present
    And the boxer called "Tyson Fury" is present