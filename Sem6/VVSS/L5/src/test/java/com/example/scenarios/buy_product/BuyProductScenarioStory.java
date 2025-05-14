package com.example.scenarios.buy_product;

import com.example.steps.serenity.DemoBlazeUserSteps;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Steps;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom("src/test/resources/scenarios/buy_product/data.csv")
public class BuyProductScenarioStory {
    @Managed(uniqueSession = true)
    public WebDriver webdriver;

    @Steps
    public DemoBlazeUserSteps userSteps;

    public String productName;
    public String clientName;
    public String clientCard;

    @Test
    public void buy_product() {
        open_product();
        add_to_cart();
        go_to_cart();
        place_order();
//        check_order();
    }

    public void open_product() {
        userSteps.opens_the_home_page();
        userSteps.opens_card(productName);
    }

    public void add_to_cart() {
        userSteps.add_to_cart();
    }

    public void go_to_cart() {
        userSteps.go_to_cart();
    }

    public void place_order() {
        userSteps.place_order(clientName, clientCard);
    }

    public void check_order() {
        userSteps.check_order();
    }
}
