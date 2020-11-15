Feature: Retrieve scorecards that an account has made

Scenario: when has scored some fights
  Given "Dani" account has scored 2 fights
  And "Vanessa" account has scored 3 fights
  When I retrieve the scorecards for "Dani"
  Then scorecards that "Dani" did are present
  Then scorecards that "Vanessa" did are not present