package com.example.steps.serenity;

import com.example.pages.DemoBlazePage;
import net.thucydides.core.annotations.Step;
import org.openqa.selenium.Alert;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class DemoBlazeUserSteps {
    DemoBlazePage demoBlazePage;

    @Step
    public void opens_the_home_page() {
        demoBlazePage.open();
    }

    @Step
    public void opens_card(String title) {
        try {
            demoBlazePage.open_card(title);
            System.out.println("Card with title '" + title + "' opened successfully");
        } catch (Error e) {
            System.out.println("Card with title '" + title + "' not found");
        }
    }

    @Step
    public void should_see_card_description(String description) {
        String actualDescription = demoBlazePage.getCardDescription();

        if (actualDescription == null) {
            throw new AssertionError("Card description is not available");
        }
        if (!actualDescription.contains(description)) {
            throw new AssertionError("Expected card description to contain: " + description + ", but got: " + actualDescription);
        }
    }

    public void should_still_be_on_homepage() {
        String currentUrl = demoBlazePage.getDriver().getCurrentUrl();
        String expectedUrl = "https://www.demoblaze.com/";

        if (!currentUrl.equals(expectedUrl)) {
            throw new AssertionError("Expected to be on homepage: " + expectedUrl + ", but was on: " + currentUrl);
        }
    }

    public void add_to_cart() {
        demoBlazePage.add_to_cart();

        try {
            WebDriver driver = demoBlazePage.getDriver();

            WebDriverWait wait = new WebDriverWait(driver, 5);

            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            String alertText = alert.getText();

            if (alertText.contains("Product added")) {
                alert.accept();
            } else {
                throw new AssertionError("Expected alert text to contain 'Product added', but got: " + alertText);
            }
        } catch (TimeoutException e) {
            throw new AssertionError("Alert not found or not as expected");
        }
    }

    public void go_to_cart() {
        demoBlazePage.go_to_cart();

        // Check we are on the cart page
        String currentUrl = demoBlazePage.getDriver().getCurrentUrl();
        String expectedUrl = "https://www.demoblaze.com/cart.html";

        if (!currentUrl.equals(expectedUrl)) {
            throw new AssertionError("Expected to be on cart page: " + expectedUrl + ", but was on: " + currentUrl);
        }
    }

    public void place_order(String clientName, String clientCard) {
        demoBlazePage.place_order(clientName, clientCard);
    }

    public void check_order() {
        demoBlazePage.go_to_cart();
        demoBlazePage.check_cart_empty();
    }
}
