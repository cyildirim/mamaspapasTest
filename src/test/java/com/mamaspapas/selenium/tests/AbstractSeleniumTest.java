package com.mamaspapas.selenium.tests;

import com.mamaspapas.selenium.helper.UrlFactory;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by can on 15/08/16.
 */
public class AbstractSeleniumTest
{
    protected static WebDriver driver;
    protected WebDriverWait webDriverWait;

    @Before
    public void setUp()
    {
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.default_content_setting_values.notifications", 2);
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);
        driver = new ChromeDriver(options);
        webDriverWait = new WebDriverWait(driver, 30);
        ensureNoLogin();
        driver.get(UrlFactory.BASE_URL.pageUrl);
    }

    @After
    public void tearDown()
    {
        driver.quit();
    }

    protected void ensureNoLogin()
    {
        driver.get(UrlFactory.BASE_URL.pageUrl);
        driver.manage().deleteCookieNamed("prodconnect.sid");
    }

}
