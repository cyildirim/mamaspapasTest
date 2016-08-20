package com.mamaspapas.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Created by can on 15/08/16.
 */
public class HomePage extends PageObject
{
    public HomePage(WebDriver driver)
    {
        super(driver);
    }

    @FindBy(css = ".login-button")
    public WebElement headerLoginLink;

    @FindBy(css = ".login-form")
    public WebElement loginForm;

    @FindBy(css = ".loginWithFacebook")
    public WebElement loginWithFacebook;

    @FindBy(css = "#user-info-box span strong")
    public WebElement userInfoBoxUserName;

    @FindBy(css = ".quick-search input[type=text]")
    public WebElement searchInput;

    @FindBy(css = ".quick-search input[type=submit]")
    public WebElement searchSubmitButton;

    @FindBy(css = ".quick-search .inner a")
    public WebElement searchSuggestionFirstLink;

    //login form

    @FindBy(css = "#login-modal input[name=email]")
    public WebElement inputEmail;

    @FindBy(css = "#login-modal input[name=password]")
    public WebElement inputPassword;

    @FindBy(css = "#login-modal button[type=submit]")
    public WebElement loginSubmitButton;

    @FindBy(css = "#user-info-box")
    public WebElement userInfoBox;
}
