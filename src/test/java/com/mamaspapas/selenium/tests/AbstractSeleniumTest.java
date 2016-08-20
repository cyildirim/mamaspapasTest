package com.mamaspapas.selenium.tests;

import com.mamaspapas.selenium.helper.UrlFactory;
import com.mamaspapas.selenium.pages.HomePage;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by can on 15/08/16.
 */
public abstract class AbstractSeleniumTest
{
    protected final static Integer DEFAULT_SLEEP = 1000;
    protected static final String STANDARD_USER_EMAIL = "mcanyildirim@gmail.com";
    protected static final String STANDARD_USER_PASS = "123qweasd";
    private static final String AJAX_WAIT_SCRIPT = "return typeof jQuery != 'undefined' && jQuery.active != 0";
    private static final Logger logger = Logger.getLogger(AbstractSeleniumTest.class);
    protected static WebDriver driver;
    @Rule
    public TestRule testRule = new TestWatcher()
    {
        @Override
        protected void starting(Description description)
        {
            logger.info(" === TEST STARTED === " + description.getDisplayName());
        }

        @Override
        protected void succeeded(Description description)
        {
            logger.info(" === TEST PASSED === " + description.getDisplayName());
        }

        @Override
        protected void failed(Throwable e, Description description)
        {
            logger.error(" === TEST FAILED === " + description.getDisplayName());
        }

        @Override
        protected void finished(Description description)
        {
            logger.info(" === TEST FINISHED === " + description.getDisplayName() + "\n\n");
        }
    };
    protected WebDriverWait webDriverWait;
    protected HomePage homePage;

    @Before
    public void setUp()
    {
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.default_content_setting_values.notifications", 2);
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);
        options.setBinary("/etc/chromedriver");
        driver = new ChromeDriver(options);
        webDriverWait = new WebDriverWait(driver, 30);
        driver.manage().window().setSize(new Dimension(1024, 768));
        homePage = new HomePage(driver);

        ensureNoLogin();
        driver.get(UrlFactory.BASE_URL.url);
    }

    @After
    public void tearDown()
    {
        driver.quit();
    }

    protected void ensureNoLogin()
    {
        driver.get(UrlFactory.BASE_URL.url);
        driver.manage().deleteCookieNamed("prodconnect.sid");
    }

    protected void waitForAjax() throws InterruptedException
    {
        Thread.sleep(250);
        JavascriptExecutor executor = (JavascriptExecutor) driver;

        boolean stillRunningAjax = (Boolean) executor.executeScript(AJAX_WAIT_SCRIPT);

        int i = 0;
        while (stillRunningAjax && i < 20)
        {
            i++;
            Thread.sleep(TimeUnit.SECONDS.toMillis(1L));
            stillRunningAjax = (Boolean) executor.executeScript(AJAX_WAIT_SCRIPT);
        }
    }

    protected void navigateProductDetail(String searchSuggestionKeyword) throws InterruptedException
    {
        waitVisibilityAndSendKeys(homePage.searchInput, searchSuggestionKeyword);
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.searchSuggestionFirstLink));
        homePage.searchSuggestionFirstLink.click();
        waitForAjax();
        logger.info("Navigated to product with " + searchSuggestionKeyword);

    }

    protected void login(String username, String password) throws InterruptedException
    {
        driver.get(UrlFactory.BASE_URL.url);
        homePage.headerLoginLink.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.inputEmail));
        homePage.inputEmail.sendKeys(username);
        homePage.inputPassword.sendKeys(password);
        homePage.loginSubmitButton.click();
        webDriverWait.until(ExpectedConditions.visibilityOf(homePage.userInfoBox));

    }

    protected void waitVisibilityAndClick(WebElement webElement)
    {
        webDriverWait.until(ExpectedConditions.visibilityOf(webElement));
        webElement.click();
    }

    protected void waitVisibilityAndSendKeys(WebElement webElement, String keys) throws InterruptedException
    {
        webDriverWait.until(ExpectedConditions.visibilityOf(webElement));
        webElement.sendKeys(keys);
    }

}
