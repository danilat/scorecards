package com.danilat.scorecards.acceptation;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.CucumberOptions.SnippetType;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:features", plugin = {
    "html:target/features",
    "message:target/cucumber_messages"}, snippets = SnippetType.CAMELCASE, tags = "not @pending")
public class CucumberRunnerTest {

}
