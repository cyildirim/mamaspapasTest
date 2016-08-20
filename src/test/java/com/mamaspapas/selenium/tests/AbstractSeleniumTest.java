package com.mamaspapas.selenium.tests;

import com.mamaspapas.selenium.helper.UrlFactory;
import com.mamaspapas.selenium.pages.HomePage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.util.Arrays;
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
    protected WebDriverWait webDriverWait;
    protected HomePage homePage;
    private Long testStart;
    private Long testEnd;
    private Long duration;

    @Rule
    public TestRule testRule = new TestWatcher()
    {
        @Override
        protected void starting(Description description)
        {
            logger.info(" === TEST STARTED === " + description.getDisplayName());
            testStart = System.currentTimeMillis();
        }

        @Override
        protected void succeeded(Description description)
        {
            logger.info(" === TEST PASSED === " + description.getDisplayName());
        }

        @Override
        protected void failed(Throwable e, Description description)
        {
            logger.error(" === TEST FAILED === " + description.getMethodName());
            String filename = description.getDisplayName() + "_" + RandomStringUtils.randomAlphanumeric(5);
            logger.info("target/screenshot" + filename);
            saveScreenShot(filename);
        }

        @Override
        protected void finished(Description description)
        {
            logger.info(" === TEST FINISHED === " + description.getDisplayName() + "\n\n");
            testEnd = System.currentTimeMillis();

            duration = testEnd - testStart;
            duration = duration / 1000L;
            logger.info("--- DURATION = " + duration + " second  " + description.getDisplayName());
        }
    };

    @BeforeClass
    public static void setUpClass()
    {
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.default_content_setting_values.notifications", 2);
        ChromeOptions options = new ChromeOptions();
        options.addArguments(Arrays.asList("--start-maximized", "allow-running-insecure-content", "ignore-certificate-errors"));
        options.addArguments("--no-sandbox");
        options.setExperimentalOption("prefs", prefs);

        DesiredCapabilities capabilities = DesiredCapabilities.chrome();

        capabilities.setCapability("enable-restore-session-state", true);
        capabilities.setJavascriptEnabled(true);
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        driver = new EventFiringWebDriver(new ChromeDriver(capabilities));

        driver.manage().window().setSize(new Dimension(1200, 800));
        driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);


    }

    @AfterClass
    public static void tearDown()
    {
        driver.quit();
    }

    @Before
    public void setUp()
    {
        webDriverWait = new WebDriverWait(driver, 30);
        homePage = new HomePage(driver);
        ensureNoLogin();
        driver.get(UrlFactory.BASE_URL.url);
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

    //--

    private void saveScreenShot(String fileName)
    {

        String dir = "target/screenshot";
        driver.manage().window().setSize(new Dimension(1200, 800));

        try
        {
            Thread.sleep(DEFAULT_SLEEP);
            String imagePath = dir + "/" + fileName + ".png";

            File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshotFile, new File(imagePath));
            logger.info("Screenshot Image Path = [" + imagePath + "]");
        }
        catch (Exception e)
        {
            logger.info("Cannot take screenshot!!!");
            e.printStackTrace();
        }
    }


}
