Feature: Create a boxer
  The only mandatory field is the name

  Scenario: successfully if all is ok
    Given a boxer name "Manny Pacquiao"
    And a boxer alias "pac-man"
    And a boxrec url "https://boxrec.com/en/proboxer/6129"
    When I create the boxer
    Then the boxer is successfully created

  Scenario: fails if the name is not present
    Given a boxer alias "pac-man"
    And a boxrec url "https://boxrec.com/en/proboxer/6129"
    When I create the boxer
    Then the boxer is not created