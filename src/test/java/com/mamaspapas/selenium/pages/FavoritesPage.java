package com.mamaspapas.selenium.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

/**
 * Created by can on 18/08/16.
 */
public class FavoritesPage extends PageObject
{
    public FavoritesPage(WebDriver driver)
    {
        super(driver);
    }

    @FindBy(css = ".wishlist-product-item")
    public List<WebElement> listFavoritesProductList;

    @FindBy(css = ".remove-from-favorites")
    public WebElement removeFromFavorites;

    @FindBy(css = ".wishlist-product-item")
    public WebElement favoriteProductContainer;

    @FindBy(css = ".confirm-btn")
    public WebElement confirmRemoveFavorite;
}
