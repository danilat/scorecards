Feature: Update a boxer
  The only mandatory field is the name

  Scenario: successfully if all is ok
    Given an existing boxer "pacquiao"
    And a boxer name "Manny Pacquiao"
    And a boxer alias "pac-man"
    And a boxrec url "https://boxrec.com/en/proboxer/6129"
    When I update the boxer
    Then the boxer is successfully updated

  Scenario: fails if the name is not present
    Given an existing boxer "pacquiao"
    And a boxer alias "pac-man"
    And a boxrec url "https://boxrec.com/en/proboxer/6129"
    When I update the boxer
    Then the boxer is not updated

  Scenario: fails if the boxer doesn't exists
    Given an non-existing boxer
    And a boxer alias "pac-man"
    And a boxrec url "https://boxrec.com/en/proboxer/6129"
    When I update the boxer
    Then the boxer is not updated