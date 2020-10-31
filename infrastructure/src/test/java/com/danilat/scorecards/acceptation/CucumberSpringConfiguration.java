package com.danilat.scorecards.acceptation;

import com.danilat.scorecards.acceptation.steps.World;
import com.danilat.scorecards.core.CoreInitializer;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest(
    classes = {CoreInitializer.class, World.class})
@ContextConfiguration
public class CucumberSpringConfiguration {

}
