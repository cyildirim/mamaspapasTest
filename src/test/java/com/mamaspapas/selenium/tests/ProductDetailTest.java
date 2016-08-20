package com.mamaspapas.selenium.tests;

import com.mamaspapas.selenium.helper.UrlFactory;
import com.mamaspapas.selenium.pages.FavoritesPage;
import com.mamaspapas.selenium.pages.ProductDetailPage;
import com.mamaspapas.selenium.pages.ShoppingCartPage;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Created by can on 15/08/16.
 */
@Category(SeleniumRegressionTestSuite.class)
public class ProductDetailTest extends AbstractSeleniumTest
{
    private final String PRODUCT_NAME = "Baby Snug - Red";
    private ProductDetailPage productDetailPage;
    private FavoritesPage favoritesPage;
    private ShoppingCartPage shoppingCartPage;
    private Logger logger = Logger.getLogger(ProductDetailTest.class);

    @Before
    public void setUp()
    {
        super.setUp();
        productDetailPage = new ProductDetailPage(driver);
        favoritesPage = new FavoritesPage(driver);
        shoppingCartPage = new ShoppingCartPage(driver);
    }

    @Test
    public void testProductThumbnail() throws InterruptedException
    {
        navigateProductDetail(PRODUCT_NAME);
        waitForAjax();
        String imageUrl = "";
        webDriverWait.until(ExpectedConditions.visibilityOf(productDetailPage.currentActiveSliderImage));

        for (int i = 0; i < productDetailPage.thumbnailList.size(); i++)
        {
            productDetailPage.thumbnailList.get(i).click();
            Thread.sleep(DEFAULT_SLEEP / 2);
            logger.info("Active Image = " + productDetailPage.currentActiveSliderImage.getAttribute("src"));
            Assert.assertFalse(productDetailPage.currentActiveSliderImage.getAttribute("src").equals(imageUrl));
            Assert.assertTrue(productDetailPage.currentActiveSliderImage.getAttribute("src").matches("https://prod.mnpcdn.ae/(.*).jpeg"));
            imageUrl = productDetailPage.currentActiveSliderImage.getAttribute("src");
        }
    }

    @Test
    public void testAddProductWishListForNonLoggedInUser() throws InterruptedException
    {
        navigateProductDetail(PRODUCT_NAME);
        waitVisibilityAndClick(productDetailPage.toggleFavoritesButton);
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.loginForm));
    }

    @Test
    public void testAddProductWishListForLoggedInUser() throws InterruptedException
    {
        login(STANDARD_USER_EMAIL, STANDARD_USER_PASS);
        removeProductFromFavorites(PRODUCT_NAME);
        navigateProductDetail(PRODUCT_NAME);
        waitVisibilityAndClick(productDetailPage.toggleFavoritesButton);
        waitForAjax();
        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='REMOVE FROM WISHLIST']")));
        removeProductFromFavorites(PRODUCT_NAME);
    }

    @Test
    public void testProductDetailTabs() throws InterruptedException
    {
        navigateProductDetail(PRODUCT_NAME);
        for (WebElement tab : productDetailPage.tabs)
        {
            tab.click();
            Assert.assertTrue(tab.getAttribute("class").equals("active"));
        }
    }

    @Test
    public void testProductTitle() throws InterruptedException
    {
        navigateProductDetail(PRODUCT_NAME);
        Assert.assertEquals(PRODUCT_NAME, productDetailPage.productTitle.getText());
    }

    @Test
    public void testProductVariant() throws InterruptedException
    {
        navigateProductDetail(PRODUCT_NAME);
        String redVariantImage = productDetailPage.currentActiveSliderImage.getAttribute("src");
        WebElement blueVariantButton = driver.findElement(By.linkText("Blue"));
        blueVariantButton.click();
        waitForAjax();
        Assert.assertFalse(redVariantImage.equals(productDetailPage.currentActiveSliderImage.getAttribute("src")));
        Assert.assertEquals("Blue", driver.findElement(By.cssSelector(".configurable-options li.active a")).getText());
    }

    @Test
    public void testProductQuantity() throws InterruptedException
    {
        navigateProductDetail(PRODUCT_NAME);
        waitForAjax();
        for (int i = 0; i < 9; i++)
        {
            productDetailPage.increaseQuantityButton.click();
        }

        Assert.assertEquals(productDetailPage.quantityInput.getAttribute("value"), "10");

        for (int i = 0; i < 4; i++)
        {
            productDetailPage.decreaseQuantityButton.click();
        }

        Assert.assertEquals(productDetailPage.quantityInput.getAttribute("value"), "6");

    }


    @Test
    public void testProductAddToBasket() throws InterruptedException
    {
        navigateProductDetail(PRODUCT_NAME);
        waitForAjax();
        for (int i = 0; i < 6; i++)
        {
            productDetailPage.increaseQuantityButton.click();
        }

        productDetailPage.addTocardButton.click();
        waitForAjax();

        productDetailPage.shoppingCart.click();

        Assert.assertEquals("7", shoppingCartPage.updateQuantityInput.getAttribute("value"));
        Assert.assertEquals(PRODUCT_NAME, shoppingCartPage.productTitle.getText());


    }


    //

    private void removeProductFromFavorites(String productName) throws InterruptedException
    {

        driver.get(UrlFactory.FAVORITES_PAGE.url);
        if (favoritesPage.listFavoritesProductList.size() > 0)
        {
            webDriverWait.until(ExpectedConditions.visibilityOf(favoritesPage.favoriteProductContainer));
            Actions actions = new Actions(driver);
            actions.moveToElement(favoritesPage.listFavoritesProductList.get(0)).moveToElement(favoritesPage.removeFromFavorites).click().build().perform();
            waitVisibilityAndClick(favoritesPage.confirmRemoveFavorite);
            waitForAjax();
        }
        else
        {
            logger.info(productName + "product already deleted from favorites");
        }


    }
}
