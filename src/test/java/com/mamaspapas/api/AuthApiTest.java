package com.mamaspapas.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mamaspapas.api.helper.Oauth1SigningInterceptor;
import com.mamaspapas.selenium.helper.UrlFactory;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Random;

/**
 * Created by can on 19/08/16.
 */
@Category(ApiRegressionTestSuite.class)
public class AuthApiTest extends AbstractApiTest
{
    private final static String CONSUMER_KEY = "{CONSUMER_KEY_HERE}";
    private final static String CONSUMER_SECRET = "{CONSUMER_SECRET_HERE}";
    private final static String TOKEN = "{TOKEN_HERE}";
    private final static String TOKEN_SECRET = "{TOKEN_SECRET_HERE}";
    Request request;
    Request signed;
    Response response;
    private Logger logger = Logger.getLogger(AuthApiTest.class);
    private OkHttpClient client;
    private Clock clock;
    private Oauth1SigningInterceptor oauth1;
    private JsonElement rootElement;

    @Before
    public void setUp() throws IOException
    {
        client = new OkHttpClient();
        clock = Clock.fixed(Instant.ofEpochMilli((System.currentTimeMillis() / 1000L)), ZoneId.systemDefault());
        oauth1 = new Oauth1SigningInterceptor.Builder()
                .consumerKey(CONSUMER_KEY)
                .consumerSecret(CONSUMER_SECRET)
                .accessToken(TOKEN)
                .accessSecret(TOKEN_SECRET)
                .random(new Random())
                .clock(clock)
                .build();
        request = new Request.Builder()
                .url(UrlFactory.API_GIFT_URL.url)
                .get()
                .build();
        signed = oauth1.signRequest(request);
        response = client.newCall(signed).execute();

        rootElement = new JsonParser().parse(response.body().string());

    }

    @Test
    public void testOauthClientShould200OK() throws IOException
    {
        Assert.assertEquals("HTTP Response Code Should 200 OK", HttpStatus.SC_OK, response.code());
    }

    @Test
    public void testItemsNodesAsArrayAndAtLeastHasOneItem() throws IOException
    {
        JsonArray itemsJsonArray = rootElement.getAsJsonObject().getAsJsonArray("items");
        Assert.assertTrue("Items array size more than 0", itemsJsonArray.size() > 0);
        for (JsonElement element : itemsJsonArray)
        {
            Assert.assertTrue("Base currency code should has a string value", element.getAsJsonObject().get("base_currency_code").getAsString().length() > 0);
            Assert.assertTrue("Base currency code should has a float value", element.getAsJsonObject().get("base_price").getAsDouble() > 0);
        }
    }

    @Test
    public void testUnauthorized() throws IOException
    {
        Response unAuthResponse = client.newCall(request).execute();
        Assert.assertEquals("Response code should UnAuthorized 401", HttpStatus.SC_UNAUTHORIZED, unAuthResponse.code());
    }


}
