package com.example.features.open_card;

import com.example.steps.serenity.DemoBlazeUserSteps;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Steps;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom("src/test/resources/features/open_card/data.csv")
public class OpenCardTest {
    @Managed(uniqueSession = true)
    public WebDriver webdriver;

    @Steps
    public DemoBlazeUserSteps userSteps;

    public String title;
    public String description;
    public String fail;

    @Test
    public void openCard() {
        userSteps.opens_the_home_page();

        userSteps.opens_card(title);

        if (fail.equals("false")) {
            userSteps.should_see_card_description(description);
        } else {
            userSteps.should_still_be_on_homepage();
        }
    }
}
