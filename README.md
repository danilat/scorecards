# Scorecards

This is a little side-project to support scoring in boxing fights.

As boxing aficionado I would like to scoreCard fights and share it with my friends an other boxing fitghts. But at this moment is more an experiment than a real application.

## Principal features (WIP):
- Register fights
- See all the available fights
- Score the fights round by round
- See other fans scoring
- Possibility to access to fighters statistics (via boxrec)

You can see the source code [on github](https://github.com/danilat/scorecards)

## Dependencies

Now we are starting to work with real persistence using PostgreSQL, you can use the configured in the docker-compose.yml running `docker-compose start`

### Run the tests

`mvn clean test`

### Run the app

`mvn install`

`mvn flyway:migrate`

`cb boot/`

`GOOGLE_APPLICATION_CREDENTIALS=pat-to-crendetials.json mvn spring-boot:run -Dspring-boot.run.profiles=dev`

You can also run the app with docker-compose creating a google-credentials.json file with:

`docker-compose up`