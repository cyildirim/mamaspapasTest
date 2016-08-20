package com.mamaspapas.selenium.tests;

import com.mamaspapas.selenium.helper.UrlFactory;
import com.mamaspapas.selenium.pages.FacebookPage;
import com.mamaspapas.selenium.pages.HomePage;
import org.apache.http.annotation.NotThreadSafe;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Created by can on 15/08/16.
 */
@NotThreadSafe
@Category(SeleniumRegressionTestSuite.class)
public class FacebookLoginTest extends AbstractSeleniumTest
{
    private static final String FB_GRANTED_ACCESS_USER_EMAIL = "ktwxjgb_sharpeman_1465123980@tfbnw.net";
    private static final String FB_GRANTED_ACCESS_USER_NAME = "Bill";

    private static final String FB_NOT_GRANTED_USER_EMAIL = "zointpr_thurnson_1465123943@tfbnw.net";
    private static final String FB_NOT_GRANTED_USER_NAME = "Rick";
    private static final String FB_USER_PASS = "1234qwe";

    private HomePage homePage;
    private FacebookPage facebookPage;


    @Before
    public void setUp()
    {
        super.setUp();
        homePage = new HomePage(driver);
        facebookPage = new FacebookPage(driver);
        logOutFromFacebook();
    }

    //--


    @Test
    public void testLoginWithAlreadyGrantedAccess()
    {
        clickLoginWithFb();
        loginToFacebook(FB_GRANTED_ACCESS_USER_EMAIL, FB_USER_PASS);
        webDriverWait.until(ExpectedConditions.urlMatches(UrlFactory.BASE_URL.url + ".*"));
        Assert.assertTrue(homePage.userInfoBoxUserName.getText().equals(FB_GRANTED_ACCESS_USER_NAME));
    }

    @Test
    public void testLoginWithGrantAccess()
    {
        deleteAppAccessIfExist(FB_NOT_GRANTED_USER_EMAIL, FB_USER_PASS);
        driver.get(UrlFactory.BASE_URL.url);
        clickLoginWithFb();
        loginToFacebook(FB_NOT_GRANTED_USER_EMAIL, FB_USER_PASS);
        webDriverWait.until(ExpectedConditions.visibilityOf(facebookPage.approveApplication));
        facebookPage.approveApplication.click();
        webDriverWait.until(ExpectedConditions.urlMatches(UrlFactory.BASE_URL.url + ".*"));
        Assert.assertTrue(homePage.userInfoBoxUserName.getText().equals(FB_NOT_GRANTED_USER_NAME));
    }

    @Test
    public void testCancelApplication()
    {
        deleteAppAccessIfExist(FB_NOT_GRANTED_USER_EMAIL, FB_USER_PASS);
        driver.get(UrlFactory.BASE_URL.url);
        clickLoginWithFb();
        loginToFacebook(FB_NOT_GRANTED_USER_EMAIL, FB_USER_PASS);
        webDriverWait.until(ExpectedConditions.visibilityOf(facebookPage.cancelApplication));
        facebookPage.cancelApplication.click();
        webDriverWait.until(ExpectedConditions.urlMatches(UrlFactory.BASE_URL.url + "/customer/fb-callback\\?error=access_denied.*"));
    }

    //--

    private void loginToFacebook(String username, String pass)
    {
        facebookPage.emailInput.sendKeys(username);
        facebookPage.passInput.sendKeys(pass);
        facebookPage.loginButton.click();
    }


    private void deleteAppAccessIfExist(String username, String pass)
    {
        driver.get(UrlFactory.FB_APP_PAGE_URL.url);
        loginToFacebook(username, pass);

        try
        {
            webDriverWait.until(ExpectedConditions.visibilityOf(facebookPage.noSuchApp));
        }
        catch (TimeoutException e)
        {
            webDriverWait.until(ExpectedConditions.visibilityOf(facebookPage.mpFbApp));
            facebookPage.mpFbApp.click();
            webDriverWait.until(ExpectedConditions.visibilityOf(facebookPage.removeAppLinkOnAppLB));
            facebookPage.removeAppLinkOnAppLB.click();
            webDriverWait.until(ExpectedConditions.visibilityOf(facebookPage.confirmRemoveApp));
            facebookPage.confirmRemoveApp.click();
            webDriverWait.until(ExpectedConditions.visibilityOf(facebookPage.noSuchApp));

        }
        logOutFromFacebook();
        driver.get(UrlFactory.BASE_URL.url);
    }


    private void logOutFromFacebook()
    {
        driver.get(UrlFactory.FB_BASE_URL.url);
        driver.manage().deleteAllCookies();
        driver.navigate().refresh();
        driver.get(UrlFactory.BASE_URL.url);
    }

    private void clickLoginWithFb()
    {
        homePage.headerLoginLink.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.loginForm));
        Assert.assertTrue("Login Form Displayed", homePage.loginForm.isDisplayed());
        homePage.loginWithFacebook.click();
        webDriverWait.until(ExpectedConditions.urlMatches("https://www.facebook.com/.*"));
    }

}

