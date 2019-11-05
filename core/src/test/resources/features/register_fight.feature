Feature: Register a fight
  Background:
    Given an existing boxer called "Kerman Lejarraga"
    And an existing boxer called "Bradley Skeete"

  Scenario: successfully if all is ok
    Given an event in "Bilbao Arena" at "28/04/2018"
    When I register the fight in the event for "Kerman Lejarraga" and "Bradley Skeete" for 12 rounds
    Then the fight is successfully registered

  Scenario: successfully if all is ok but place is not present
    Given an event at "28/04/2018"
    When I register the fight in the event for "Kerman Lejarraga" and "Bradley Skeete" for 12 rounds
    Then the fight is successfully registered

 Scenario: fails without event date
     Given an event in "Bilbao Arena"
     When I register the fight in the event for "Kerman Lejarraga" and "Bradley Skeete" for 12 rounds
     Then the fight is not registered

Scenario: fails without number of rounds
    Given an event in "Bilbao Arena" at "28/04/2018"
    When I register the fight in the event for "Kerman Lejarraga" and "Bradley Skeete"
    Then the fight is not registered