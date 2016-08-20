package com.mamaspapas.selenium.tests;

import com.mamaspapas.selenium.pages.HomePage;
import com.mamaspapas.selenium.pages.SearchPage;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Created by can on 15/08/16.
 */
@Category(SeleniumRegressionTestSuite.class)
public class SearchTest extends AbstractSeleniumTest
{
    private final String SEARCH_WORD = "red";
    private final String NOT_FOUND_SEARCH_WORD = "noitemswiththiskeyword";
    private final String BLANK_SEARCH_MESSAGE = "You searched for noitemswiththiskeyword, we're sorry we couldn't find anything to match your search.";
    private final String SEARCH_SUGGESTION_KEYWORD = "Fox All-in-One";
    private final String SEARCH_SUGGESTION_URL = "https://www.mamasandpapas.ae/fox-all-in-one-s94fx33.html";
    private HomePage homePage;
    private SearchPage searchPage;
    private Logger logger = Logger.getLogger(SearchTest.class);

    @Before
    public void setUp()
    {
        super.setUp();
        homePage = new HomePage(driver);
        searchPage = new SearchPage(driver);
    }

    @Test
    public void testSearchLoadMoreWhenSearchResultSizeMoreThanTwelve() throws InterruptedException
    {
        homePage.searchInput.sendKeys(SEARCH_WORD + Keys.ENTER);
        waitForAjax();
        Assert.assertEquals(12, searchPage.searchResultItems.size());
        searchPage.loadMore.click();
        waitForAjax();
        Assert.assertEquals(24, searchPage.searchResultItems.size());
    }

    @Test
    public void testCouldNotFindAnything() throws InterruptedException
    {
        homePage.searchInput.sendKeys(NOT_FOUND_SEARCH_WORD + Keys.ENTER);
        waitForAjax();
        Assert.assertEquals(searchPage.blankSearchMessage.getText(), BLANK_SEARCH_MESSAGE);
        //suggestion may add
    }

    @Test
    public void testSearchSuggestion() throws InterruptedException
    {
        navigateProductDetail(SEARCH_SUGGESTION_KEYWORD);
        Assert.assertEquals(SEARCH_SUGGESTION_URL, driver.getCurrentUrl());
    }

    @Test
    public void testSearchFilter() throws InterruptedException
    {
        homePage.searchInput.sendKeys(SEARCH_WORD + Keys.ENTER);
        webDriverWait.until(ExpectedConditions.visibilityOf(searchPage.newBornFilter));
        searchPage.newBornFilter.click();
        waitForAjax();
        Assert.assertTrue(searchPage.searchResultItems.size() > 0);

    }

    @Test
    public void testSortType() throws InterruptedException
    {
        homePage.searchInput.sendKeys(SEARCH_WORD + Keys.ENTER);
        waitForAjax();

        for (int i = 1; i < searchPage.sortingOptionsList.size(); i++)
        {
            searchPage.sortingOptionsSelect.selectByValue(searchPage.sortingOptionsList.get(i).getAttribute("value"));
            waitForAjax();
            Assert.assertTrue(searchPage.searchResultItems.size() > 0);
        }

    }

}
