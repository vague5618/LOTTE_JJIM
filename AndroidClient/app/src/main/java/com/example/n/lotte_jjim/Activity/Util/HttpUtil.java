package com.example.n.lotte_jjim.Activity.Util;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class HttpUtil extends Thread{


    private static final String TAG = "HttpUtil";

    static String targetURL = "http://172.20.10.4:3000/";
 //   static String targetURL = "http://192.168.0.23:3000/";

    String requestType = null;
    JSONArray jsonArray =null;

    HttpClient httpClient;
    HttpPost httpPost;
    JSONArray responseJSON = null;

    public HttpUtil(String requestTable, String requestType, JSONArray jsonArray)
    {
        httpClient = new DefaultHttpClient();
        httpPost = new HttpPost(targetURL+requestTable);

        this.requestType = requestType;
        this.jsonArray = jsonArray;
    };

    @Override
    public void run() {
        super.run();

        httpPost.setHeader("Content-type","application/json; charset=utf-8");

        try {

            JSONObject reqType = new JSONObject();

            reqType.put("RequestType", requestType);

            this.jsonArray.put(reqType);

            httpPost.setEntity(new StringEntity(jsonArray.toString(), HTTP.UTF_8));

            HttpResponse response = httpClient.execute(httpPost);

            HttpEntity resEntity = response.getEntity();

            if (resEntity != null) {
                String res = EntityUtils.toString(resEntity);
                getJSONArray(res);
            }
        }
        catch(JSONException e)
        {
            Log.d(TAG, e.toString());
        }
        catch(UnsupportedEncodingException e)
        {
            Log.d(TAG, e.toString());
        }
        catch(org.apache.http.client.ClientProtocolException e)
        {
            Log.d(TAG, e.toString());
        }
        catch (IOException e)
        {
            Log.d(TAG,e.toString());
        }
    }

    public void getJSONArray(String res)
    {
        try {
            JSONArray jsonArray = new JSONArray(res);
            this.responseJSON = jsonArray;
        }
        catch (org.json.JSONException e)
        {
            Log.d(TAG, e.toString());
        }
    }

    public JSONArray getResponse()
    {
        return this.responseJSON;
    }
}
