package com.example.pages;

import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.DefaultUrl;
import net.thucydides.core.pages.PageObject;
import org.openqa.selenium.By;


@DefaultUrl("https://www.demoblaze.com/")
public class DemoBlazePage extends PageObject {
    public void open_card(String title) {
        WebElementFacade webElementFacade = find(By.xpath("//a[text()='" + title + "']"));

        if (webElementFacade.isPresent()) {
            webElementFacade.click();
        } else {
            throw new Error("Card with title '" + title + "' not found");
        }
    }

    public String getCardDescription() {
        try {
            return find(By.id("more-information")).getText();
        } catch (Exception e) {
            return null; // Handle the case where the element is not found
        }
    }

    public void add_to_cart() {
        WebElementFacade addToCartButton = find(By.xpath("//a[@onclick='addToCart(1)']"));
        if (addToCartButton.isPresent()) {
            addToCartButton.click();
        } else {
            throw new Error("Add to cart button not found");
        }
    }

    public void go_to_cart() {
        WebElementFacade cartButton = find(By.xpath("//a[@id='cartur']"));
        if (cartButton.isPresent()) {
            cartButton.click();
        } else {
            cartButton = find(By.xpath("//a[@onclick='showcart()']"));

            if (cartButton.isPresent()) {
                cartButton.click();
            } else {
                throw new Error("Cart button not found");
            }
        }
    }

    public void place_order(String clientName, String clientCard) {
        WebElementFacade placeOrderButton = find(By.xpath("//button[@class='btn btn-success']"));

        if (placeOrderButton.isPresent()) {
            placeOrderButton.click();
        } else {
            throw new Error("Place order button not found");
        }

        WebElementFacade nameInput = find(By.id("name"));
        WebElementFacade cardInput = find(By.id("card"));

        nameInput.type(clientName);
        cardInput.type(clientCard);

        WebElementFacade purchaseButton = find(By.xpath("//button[@onclick='purchaseOrder()']"));

        if (purchaseButton.isPresent()) {
            purchaseButton.click();
        } else {
            throw new Error("Purchase button not found");
        }

        WebElementFacade okButton = find(By.xpath("//button[@class='confirm btn btn-lg btn-primary']"));

        if (okButton.isPresent()) {
            okButton.click();
        } else {
            throw new Error("OK button not found");
        }

        // Close order
        WebElementFacade closeButton = find(By.xpath("/html/body/div[3]/div/div/div[3]/button[1]"));
        if (closeButton.isPresent()) {
            closeButton.click();
        } else {
            throw new Error("Close button not found");
        }
    }

    public void check_cart_empty() {
        WebElementFacade tableBody = find(By.xpath("//*[@id='tbodyid']"));

        if (tableBody.isPresent()) {
            String cartContent = tableBody.getText();

            if (cartContent.isEmpty()) {
                System.out.println("Cart is empty");
            } else {
                throw new Error("Cart is not empty");
            }
        } else {
            throw new Error("Table body not found");
        }
    }
}
