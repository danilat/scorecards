Feature: Register a fight

  Scenario: successfully if all is ok
    Given an existing boxer called "Kerman Lejarraga"
    And an existing boxer called "Bradley Skeete"
    And an event in "Bilbao Arena" at "28/04/2018"
    When I register the fight in the event for "Kerman Lejarraga" and "Bradley Skeete"
    Then the fight is registered

  Scenario: fails if the first boxer is not present
  Scenario: fails if the second boxer is not present
  Scenario: fails if the date is not present