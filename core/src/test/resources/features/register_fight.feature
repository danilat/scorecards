Feature: Register a fight

  Scenario: successfully if all is ok
    Given an existing boxer is "Kerman Lejarraga"
    And an existing boxer is "Bradley Skeete"
    And an event in "Bilbao Arena" in "28/04/2018"
    When I register the fight
    Then the fight is registered

  Scenario: fails if the first boxer is not present
  Scenario: fails if the second boxer is not present
  Scenario: fails if the date is not present