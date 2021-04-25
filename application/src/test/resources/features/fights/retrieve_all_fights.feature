Feature: Retrieve all fights

  Scenario: has some fights
    Given an existing fight between "Ali" and "Liston"
    When I retrieve all the fights
    Then there is the fight between "Ali" and "Liston" present