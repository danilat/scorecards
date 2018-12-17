package com.danilat.scorecards.acceptation;

import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:features", snippets = SnippetType.CAMELCASE)
public class CucumberRunnerTest {

}
