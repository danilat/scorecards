Feature: Update a fight

  Background:
    Given an existing boxer called "Mohamed Ali"
    And an existing boxer called "Joe Frazier"
    And an existing boxer called "George Foreman"
    And an existing fight between "Mohamed Ali" and "Joe Frazier" with 12 rounds

  Scenario: successfully if all is ok
    Given an event in "Zaire" at "30/10/1974"
    When I update the fight in the event for "Mohamed Ali" and "George Foreman" for 12 rounds
    Then the fight is successfully updated

  Scenario: successfully if all is ok but place is not present
    Given an event at "30/10/1974"
    When I update the fight in the event for "Mohamed Ali" and "George Foreman" for 12 rounds
    Then the fight is successfully updated

  Scenario: fails if the first boxer is not present
    When I update the fight in the event for "" and "George Foreman" for 12 rounds
    Then the fight is not updated

  Scenario: fails if the second boxer is not present
    When I update the fight in the event for "Mohamed Ali" and "" for 12 rounds
    Then the fight is not updated

  Scenario: fails without event date
    Given an event in "Zaire"
    When I update the fight in the event for "Mohamed Ali" and "George Foreman" for 12 rounds
    Then the fight is not updated

  Scenario: fails without number of rounds
    When I update the fight in the event for "Mohamed Ali" and "George Foreman"
    Then the fight is not updated

  Scenario: fails with less than 3 rounds
    When I update the fight in the event for "Mohamed Ali" and "George Foreman" for 2 rounds
    Then the fight is not updated

  Scenario: fails with more than 12 rounds
    When I update the fight in the event for "Mohamed Ali" and "George Foreman" for 13 rounds
    Then the fight is not updated