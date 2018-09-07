package com.dmw.eteachswayam.exo.model;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by ChetanAndroid-pc on 2/15/2016.
 */
public class EteachRestClient {
    private static final String BASE_URL = "https://www.dmwerp.com";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get( String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post( String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static
    String getAbsoluteUrl( String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
