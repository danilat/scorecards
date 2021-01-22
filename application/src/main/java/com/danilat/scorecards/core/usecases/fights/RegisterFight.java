package com.danilat.scorecards.core.usecases.fights;

import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.boxer.BoxerNotFoundError;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.domain.fight.Event;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightId;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.usecases.ConstraintValidatorToErrorMapper;
import com.danilat.scorecards.core.usecases.fights.RegisterFight.RegisterFightParameters;
import com.danilat.scorecards.shared.Clock;
import com.danilat.scorecards.shared.PrimaryPort;
import com.danilat.scorecards.shared.UniqueIdGenerator;
import com.danilat.scorecards.shared.domain.FieldError;
import com.danilat.scorecards.shared.domain.FieldErrors;
import com.danilat.scorecards.shared.events.EventBus;
import com.danilat.scorecards.shared.usecases.UseCase;
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

public class RegisterFight implements UseCase<Fight, RegisterFightParameters> {

  private final FightRepository fightRepository;
  private final BoxerRepository boxerRepository;
  private final EventBus eventBus;
  private final Clock clock;
  private final UniqueIdGenerator uniqueIdGenerator;
  private FieldErrors errors;

  public RegisterFight(FightRepository fightRepository, BoxerRepository boxerRepository,
      EventBus eventBus, Clock clock,
      UniqueIdGenerator uniqueIdGenerator) {
    this.fightRepository = fightRepository;
    this.boxerRepository = boxerRepository;
    this.eventBus = eventBus;
    this.clock = clock;
    this.uniqueIdGenerator = uniqueIdGenerator;
  }

  public void execute(PrimaryPort<Fight> primaryPort, RegisterFightParameters parameters) {
    if (!validate(parameters)) {
      primaryPort.error(errors);
      return;
    }

    Event event = new Event(parameters.getHappenAt(), parameters.getPlace());
    Fight fight = Fight.create(new FightId(uniqueIdGenerator.next()), parameters.getFirstBoxer(),
        parameters.getSecondBoxer(),
        event, parameters.getNumberOfRounds(), clock.now());
    fightRepository.save(fight);
    eventBus.publish(fight.domainEvents());
    primaryPort.success(fight);
  }

  private boolean validate(RegisterFightParameters parameters) {
    errors = new FieldErrors();
    errors.addAll(parameters.validate());
    errors.add(validateBoxer("firstBoxer", parameters.getFirstBoxer()));
    errors.add(validateBoxer("secondBoxer", parameters.getSecondBoxer()));
    return errors.isEmpty();
  }

  private FieldError validateBoxer(String fieldName, BoxerId boxerId) {
    FieldError error = null;
    if (!this.boxerRepository.get(boxerId).isPresent()) {
      error = new FieldError(fieldName, new BoxerNotFoundError(boxerId));
    }
    return error;
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

    public FieldErrors validate() {
      ConstraintValidatorToErrorMapper constraintValidatorToErrorMapper = new ConstraintValidatorToErrorMapper<RegisterFightParameters>();
      ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
      Validator validator = factory.getValidator();
      Set<ConstraintViolation<RegisterFightParameters>> violations = validator.validate(this);
      return constraintValidatorToErrorMapper.mapConstraintViolationsToErrors(violations);
    }
  }
}
