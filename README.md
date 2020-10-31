# Scorecards

This is a little side-project to support scoring in boxing fights.

As boxing aficionado I would like to scoreCard fights and share it with my friends an other boxing fitghts. But at this moment is more an experiment than a real application.

## Principal features (WIP):
- Register fights
- See all the available fights
- Score the fights round by round
- See other fans scoring
- Watch streaming fights when is possible
- Watch past video fights when is possible
- Possibility to access to fighters statistics (via boxrec)

This is a backend service to support this features, uses Hexagonal Architecture and DDD tactical artifacts with Spring Boot as infrastructure framework.


You can see the source code [on github](https://github.com/danilat/scorecards)

### Run the tests

`mvn clean test`

### Run the app

`mvn install`

`cb boot/`

`mvn spring-boot:run -Dspring-boot.run.profiles=dev`