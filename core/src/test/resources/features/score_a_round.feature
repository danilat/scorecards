Feature: Score a round
  Background:
    Given an existing boxer called "Kerman Lejarraga"
    And an existing boxer called "Bradley Skeete"
    And an existing fight between "Kerman Lejarraga" and "Bradley Skeete" with 12 rounds

  Scenario: the first round in a fight
    When an aficionado scores the round "1" for the existing fight with "10" and "9"
    Then the round is scored with with "10" and "9"
    And the fight score is "10" to "9"

  Scenario: the next rounds in a fight, the fight score sums
    Given the existing fight has been scored by the aficionado in the round "1" with "10" and "9"
    When an aficionado scores the round "2" for the existing fight with "10" and "10"
    Then the round is scored with with "10" and "10"
    And the fight score is "20" to "19"

  Scenario: a round is reescored changes the score for the round
    Given the existing fight has been scored by the aficionado in the round "1" with "10" and "10"
    When an aficionado scores the round "1" for the existing fight with "10" and "9"
    Then the round is scored with with "10" and "9"
    And the fight score is "10" to "9"

  Scenario: out of range of the inverval of rounds in a fight
    When an aficionado scores the round "13" for the existing fight with "10" and "9"
    Then the round is not scored

  Scenario: less than the first round
    When an aficionado scores the round "0" for the existing fight with "10" and "9"
    Then the round is not scored
    
  Scenario: the fight not exist
    When an aficionado scores the round "1" for the non-existing fight with "10" and "9"
    Then the round is not scored