package com.mamaspapas.selenium.helper;

/**
 * Created by can on 15/08/16.
 */
public enum UrlFactory
{
    BASE_URL("https://www.mamasandpapas.ae");

    //--

    public final String pageUrl;

    UrlFactory(String pageUrl)
    {
        this.pageUrl = pageUrl;
    }

    UrlFactory(UrlFactory baseUrl, String pageUrl)
    {
        this.pageUrl = baseUrl.pageUrl + pageUrl;
    }
}
