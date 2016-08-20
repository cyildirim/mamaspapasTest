package com.mamaspapas.selenium.helper;

/**
 * Created by can on 15/08/16.
 */
public enum UrlFactory
{

    API_BASE_URL("http://magento-dev.atg.digital/rest/default/V1"),
    API_CATEGORIES_URL(API_BASE_URL,"/categories"),
    API_GIFT_URL(API_BASE_URL,"/gift-wrappings?searchCriteria[filterGroups][0][filters][0][field]=status&searchCriteria[filterGroups][0][filters][0][value]=1"),
    BASE_URL("https://www.mamasandpapas.ae"),
    FAVORITES_PAGE(BASE_URL, "/customer/favorites"),
    FB_APP_PAGE_URL("https://www.facebook.com/settings?tab=applications"),
    FB_BASE_URL("https://www.facebook.com/");

    //--

    public final String url;

    UrlFactory(String url)
    {
        this.url = url;
    }

    UrlFactory(UrlFactory baseUrl, String url)
    {
        this.url = baseUrl.url + url;
    }
}
