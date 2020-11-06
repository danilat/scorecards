package com.danilat.scorecards.core.usecases.fights;

import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.domain.fight.Event;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.domain.fight.events.FightCreated;
import com.danilat.scorecards.core.usecases.ConstraintValidatorToErrorMapper;
import com.danilat.scorecards.core.usecases.fights.RegisterFight.RegisterFightParameters;
import com.danilat.scorecards.shared.Clock;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.UniqueIdGenerator;
import com.danilat.scorecards.shared.UseCase;
import com.danilat.scorecards.shared.domain.Error;
import com.danilat.scorecards.shared.domain.Errors;
import com.danilat.scorecards.shared.events.DomainEventId;
import com.danilat.scorecards.shared.events.EventBus;
import java.time.LocalDate;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class RegisterFight implements UseCase<RegisterFightParameters, Fight> {

  private final FightRepository fightRepository;
  private final BoxerRepository boxerRepository;
  private final EventBus eventBus;
  private final Clock clock;
  private final UniqueIdGenerator uniqueIdGenerator;
  private Errors errors;

  private ConstraintValidatorToErrorMapper constraintValidatorToErrorMapper;

  public RegisterFight(FightRepository fightRepository, BoxerRepository boxerRepository,
      EventBus eventBus, Clock clock,
      UniqueIdGenerator uniqueIdGenerator) {
    this.fightRepository = fightRepository;
    this.boxerRepository = boxerRepository;
    this.eventBus = eventBus;
    this.clock = clock;
    this.uniqueIdGenerator = uniqueIdGenerator;
    constraintValidatorToErrorMapper = new ConstraintValidatorToErrorMapper<RegisterFightParameters>();
  }

  public void execute(RegisterFightParameters parameters, PrimaryPort<Fight> primaryPort) {
    if (!validate(parameters)) {
      primaryPort.error(errors);
      return;
    }

    Event event = new Event(parameters.getHappenAt(), parameters.getPlace());
    Fight fight = new Fight(new FightId(uniqueIdGenerator.next()), parameters.getFirstBoxer(),
        parameters.getSecondBoxer(),
        event, parameters.getNumberOfRounds());
    fightRepository.save(fight);
    eventBus.publish(new FightCreated(fight, clock.now(), new DomainEventId(uniqueIdGenerator.next())));
    primaryPort.success(fight);
  }

  private boolean validate(RegisterFightParameters parameters) {
    errors = new Errors();
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    Set<ConstraintViolation<RegisterFightParameters>> violations = validator.validate(parameters);
    errors.addAll(constraintValidatorToErrorMapper.mapConstraintViolationsToErrors(violations));
    boxerExists(parameters.getFirstBoxer(), "firstBoxer");
    boxerExists(parameters.getSecondBoxer(), "secondBoxer");
    return errors.size() == 0;
  }

  private void boxerExists(BoxerId boxerId, String fieldName) {
    if (!this.boxerRepository.get(boxerId).isPresent()) {
      Error error = new Error(fieldName, boxerId + " not found");
      errors.add(error);
    }
  }

  public static class RegisterFightParameters {

    @NotNull(message = "firstBoxer is mandatory")
    private final BoxerId firstBoxer;
    @NotNull(message = "secondBoxer is mandatory")
    private final BoxerId secondBoxer;
    @NotNull(message = "happenAt is mandatory")
    private final LocalDate happenAt;
    private final String place;
    @NotNull(message = "numberOfRounds is mandatory")
    @Min(value = 3, message = "numberOfRounds is less than three")
    @Max(value = 12, message = "numberOfRounds is more than twelve")
    private final Integer numberOfRounds;

    @AssertFalse(message = "firstBoxer and secondBoxer should be different")
    public boolean isFirstBoxersEqualThanSecondBoxer() {
      if (getFirstBoxer() == null) {
        return true;
      }
      return getFirstBoxer().equals(getSecondBoxer());
    }


    public BoxerId getFirstBoxer() {
      return firstBoxer;
    }

    public BoxerId getSecondBoxer() {
      return secondBoxer;
    }

    public LocalDate getHappenAt() {
      return happenAt;
    }

    public String getPlace() {
      return place;
    }

    public Integer getNumberOfRounds() {
      return numberOfRounds;
    }


    public RegisterFightParameters(BoxerId firstBoxer, BoxerId secondBoxer, LocalDate happenAt,
        String place, Integer numberOfRounds) {
      this.firstBoxer = firstBoxer;
      this.secondBoxer = secondBoxer;
      this.happenAt = happenAt;
      this.place = place;
      this.numberOfRounds = numberOfRounds;
    }
  }
}
