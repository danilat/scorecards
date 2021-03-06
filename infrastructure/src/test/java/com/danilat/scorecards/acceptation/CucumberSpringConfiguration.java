package com.danilat.scorecards.acceptation;

import com.danilat.scorecards.acceptation.steps.World;
import com.danilat.scorecards.core.CoreInitializer;
import com.danilat.scorecards.shared.auth.firebase.TokenValidator;
import io.cucumber.spring.CucumberContextConfiguration;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest(
    classes = {CoreInitializer.class, World.class, SimpleMeterRegistry.class})
@ContextConfiguration
@ImportAutoConfiguration
public class CucumberSpringConfiguration {

  @MockBean
  private TokenValidator tokenValidator;
}
