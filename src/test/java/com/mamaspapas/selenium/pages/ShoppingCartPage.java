package com.mamaspapas.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Created by can on 19/08/16.
 */
public class ShoppingCartPage extends PageObject

{
    public ShoppingCartPage(WebDriver driver)
    {
        super(driver);
    }

    @FindBy(css = ".update-quantity")
    public WebElement updateQuantityInput;

    @FindBy(css = ".info>div>strong>div")
    public WebElement productTitle;
}
