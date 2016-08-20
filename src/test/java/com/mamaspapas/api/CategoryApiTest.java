package com.mamaspapas.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mamaspapas.selenium.helper.UrlFactory;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by can on 19/08/16.
 */
public class CategoryApiTest extends AbstractApiTest
{

    private Logger logger = Logger.getLogger(CategoryApiTest.class);
    private HttpUriRequest request;
    private HttpResponse httpClient;
    private JsonObject rootObject;

    @Before
    public void setUp() throws IOException, URISyntaxException, HttpException
    {
        request = new HttpGet(UrlFactory.API_CATEGORIES_URL.url);
        logger.info("Request URL = " + UrlFactory.API_CATEGORIES_URL.url);
        HttpClient client = new DefaultHttpClient();
        httpClient = client.execute(request);
        rootObject = new JsonParser().parse(EntityUtils.toString(httpClient.getEntity())).getAsJsonObject();
    }

    @Test
    public void testApiUrlReturnHttpOk() throws IOException
    {
        Assert.assertEquals("HttpResponseCode Should be 200", httpClient.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
    }

    @Test
    public void testFirstNodeHasMoreThan10Categories() throws IOException
    {
        Assert.assertTrue("First node should have more than 10 categories on children_data node ", rootObject.getAsJsonArray("children_data").size() > 10);
    }

    @Test
    public void testEveryChildrenDataNodeHaveIdAndName()
    {
        for (JsonElement element : rootObject.getAsJsonArray("children_data"))
        {
            Assert.assertTrue("Every node should have an \"id\"",element.getAsJsonObject().get("id").getAsInt() > 0);
            Assert.assertTrue("Every node should have an \"name\"",element.getAsJsonObject().get("name").getAsString().length() > 0);
        }
    }

}
