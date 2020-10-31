Feature: Score all rounds
  Background:
    Given an existing boxer called "Mohamed Ali"
    And an existing boxer called "Joe Frazier"
    And an existing fight between "Mohamed Ali" and "Joe Frazier" with 12 rounds

  Scenario: the first round in a fight
    When an aficionado scores the round 1 for the existing fight with 10 and 9
    Then the round 1 is scored with with 10 and 9
    And the fight scoreCard is 10 to 9

  Scenario: the next rounds in a fight, the fight scoreCard sums
    Given the existing fight has been scored by the aficionado in the round 1 with 10 and 9
    When an aficionado scores the round 2 for the existing fight with 10 and 10
    Then the round 2 is scored with with 10 and 10
    And the fight scoreCard is 20 to 19

  Scenario: a round is re-scored changes the scoreCard for the round
    Given the existing fight has been scored by the aficionado in the round 1 with 10 and 10
    When an aficionado scores the round 1 for the existing fight with 10 and 9
    Then the round 1 is scored with with 10 and 9
    And the fight scoreCard is 10 to 9

  Scenario: out of range of the interval of rounds in a fight
    When an aficionado scores the round 13 for the existing fight with 10 and 9
    Then the round is not scored

  Scenario: less than the first round
    When an aficionado scores the round 0 for the existing fight with 10 and 9
    Then the round is not scored

  Scenario: the fight not exist
    When an aficionado scores the round 1 for the non-existing fight with 10 and 9
    Then the round is not scored

  Scenario: there are scores in rounds less than 1
    When an aficionado scores the round 1 for the existing fight with 0 and 9
    Then the round is not scored

  Scenario: there are scores a round more than 10
    When an aficionado scores the round 1 for the existing fight with 11 and 9
    Then the round is not scored

  Scenario: there are scores in a rounds only for a boxer
    When an aficionado scores the round 1 for the existing fight with 10 for the first boxer
    Then the round is not scored