package com.mamaspapas.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Created by can on 15/08/16.
 */
public class FacebookPage extends PageObject
{
    public FacebookPage(WebDriver driver)
    {
        super(driver);
    }

    // login Page

    @FindBy(id = "email")
    public WebElement emailInput;

    @FindBy(id = "pass")
    public WebElement passInput;

    @FindBy(id = "loginbutton")
    public WebElement loginButton;


    // App Page

    @FindBy(xpath = "//div[contains(text(),\"You haven\")]")
    public WebElement noSuchApp;

    @FindBy(xpath = "//div[text()='Mamas & Papas ME']")
    public WebElement mpFbApp;

    @FindBy(xpath = "//a[text()='Remove App']")
    public WebElement removeAppLinkOnAppLB;

    @FindBy(css = ".uiButtonConfirm")
    public WebElement confirmRemoveApp;
}
