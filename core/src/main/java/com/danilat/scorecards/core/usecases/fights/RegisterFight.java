package com.danilat.scorecards.core.usecases.fights;

import com.danilat.scorecards.core.domain.boxer.Boxer;
import com.danilat.scorecards.core.domain.boxer.BoxerId;
import com.danilat.scorecards.core.domain.boxer.BoxerNotFoundException;
import com.danilat.scorecards.core.domain.boxer.BoxerRepository;
import com.danilat.scorecards.core.domain.fight.Event;
import com.danilat.scorecards.core.domain.fight.Fight;
import com.danilat.scorecards.core.domain.fight.FightRepository;
import com.danilat.scorecards.core.usecases.UseCase;
import com.danilat.scorecards.core.usecases.fights.RegisterFight.RegisterFightParameters;
import java.time.LocalDate;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;

public class RegisterFight implements UseCase<RegisterFightParameters> {

  private final FightRepository fightRepository;
  private final BoxerRepository boxerRepository;

  public RegisterFight(FightRepository fightRepository, BoxerRepository boxerRepository) {
    this.fightRepository = fightRepository;
    this.boxerRepository = boxerRepository;
  }

  @Override
  public Fight execute(RegisterFightParameters parameters) {
    validate(parameters);
    Boxer firstBoxer = this.boxerRepository.get(parameters.getFirstBoxer())
        .orElseThrow(() -> new BoxerNotFoundException(parameters.getFirstBoxer()));
    Boxer secondBoxer = this.boxerRepository.get(parameters.getSecondBoxer())
        .orElseThrow(() -> new BoxerNotFoundException(parameters.getSecondBoxer()));
    Event event = new Event(parameters.getHappenAt(), parameters.getPlace());

    Fight fight = new Fight(fightRepository.nextId(), firstBoxer.id(),
        secondBoxer.id(),
        event);
    fightRepository.save(fight);
    return fight;
  }

  private void validate(RegisterFightParameters parameters) {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    Set<ConstraintViolation<RegisterFightParameters>> violations = validator.validate(parameters);
    if (!violations.isEmpty()) {
      throw new InvalidFightException(violations);
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

    public RegisterFightParameters(BoxerId firstBoxer, BoxerId secondBoxer, LocalDate happenAt, String place) {
      this.firstBoxer = firstBoxer;
      this.secondBoxer = secondBoxer;
      this.happenAt = happenAt;
      this.place = place;
    }
  }
}
