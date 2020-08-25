package com.danilat.scorecards.acceptation;

import com.danilat.scorecards.core.CoreInitializer;
import com.danilat.scorecards.shared.SharedInitializer;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest(
    classes = {CoreInitializer.class, SharedInitializer.class})
@ContextConfiguration
public class CucumberSpringConfiguration {

}
