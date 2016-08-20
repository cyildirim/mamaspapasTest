package com.mamaspapas.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Created by can on 17/08/16.
 */
public class ProductDetailPage extends PageObject
{
    public ProductDetailPage(WebDriver driver)
    {
        super(driver);
    }

    @FindBy(css = ".slider-nav .slick-track figure")
    public List<WebElement> thumbnailList;

    @FindBy(css = ".product-images-wrapper .slider-for .slick-current img")
    public WebElement currentActiveSliderImage;

    @FindBy(css = ".toggle-favorites")
    public WebElement toggleFavoritesButton;

    @FindBy(css = ".tab-headers a")
    public List<WebElement> tabs;

    @FindBy(css = "h1")
    public WebElement productTitle;

    @FindBy(css = ".increase")
    public WebElement increaseQuantityButton;

    @FindBy(css = ".decrease")
    public WebElement decreaseQuantityButton;

    @FindBy(css = ".quantity-input")
    public WebElement quantityInput;

    @FindBy(css = ".add-to-cart-button")
    public WebElement addTocardButton;

    @FindBy(css = ".mini-cart a.box-header")
    public WebElement shoppingCart;

}
