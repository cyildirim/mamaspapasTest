package com.mamaspapas.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

/**
 * Created by can on 17/08/16.
 */
public class SearchPage extends PageObject
{
    public SearchPage(WebDriver driver)
    {
        super(driver);
    }

    @FindBy(css = ".product-collection div.col-md-4")
    public List<WebElement> searchResultItems;

    @FindBy(css = ".load-more")
    public WebElement loadMore;

    @FindBy(css = ".blank-search-message")
    public WebElement blankSearchMessage;

    @FindBy(linkText = "Newborn")
    public WebElement newBornFilter;

    @FindBy(css = ".sorting-field options")
    public List<WebElement> sortingOptionsList;

    @FindBy(css = ".sorting-field")
    public Select sortingOptionsSelect;


}
