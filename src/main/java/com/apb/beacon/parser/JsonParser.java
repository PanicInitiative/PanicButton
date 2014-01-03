package com.apb.beacon.parser;

import android.util.Log;

import com.apb.beacon.AppConstants;
import com.apb.beacon.model.MarkDownResponse;
import com.apb.beacon.model.ServerResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class JsonParser {

    static InputStream is   = null;
    static JSONObject jObj = null;
    static String json = "";

    private static final String TAG  = JsonParser.class.getSimpleName();

    // constructor
    public JsonParser() {

    }

    public ServerResponse retrieveServerData(int reqType, String url, List<NameValuePair> urlParams, String content, String appToken) {
        Log.d(TAG, "in retrieveServerData method");

        int status = 0;

        StringBuilder sb = null;

        if (urlParams != null) {
            String paramString = URLEncodedUtils.format(urlParams, "utf-8");
            url += "?" + paramString;            
        }
        Log.e(TAG, "url after param added = " + url);
        Log.e(TAG, "content body = " + content);

        // Making HTTP request
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = null;

            if(reqType == AppConstants.HTTP_REQUEST_TYPE_GET){
                HttpGet httpGet = new HttpGet(url);
                httpGet.setHeader("Content-Type", "application/json");
                httpGet.setHeader("Accept", "application/json");
                if (appToken != null){
                    httpGet.setHeader("token", appToken);
                }

                httpResponse = httpClient.execute(httpGet);

            } else if(reqType == AppConstants.HTTP_REQUEST_TYPE_POST){
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader("Content-Type", "application/json");
                httpPost.setHeader("Accept", "application/json");
                if (appToken != null){
                    httpPost.setHeader("token", appToken);
                }

                StringEntity se = new StringEntity(content);
                se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                httpPost.setEntity(se);

                httpResponse = httpClient.execute(httpPost);                
            } else if(reqType == AppConstants.HTTP_REQUEST_TYPE_PUT){
                HttpPut httpPut = new HttpPut(url);
                httpPut.setHeader("Content-Type", "application/json");
                httpPut.setHeader("Accept", "application/json");
                if (appToken != null){
                    httpPut.setHeader("token", appToken);
                }

                StringEntity se = new StringEntity(content);
                se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                httpPut.setEntity(se);

                httpResponse = httpClient.execute(httpPut);                
            }

            status = httpResponse.getStatusLine().getStatusCode();
            Log.e(TAG, "STATUS = " + status);

            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
            Log.e(TAG, "is = " + is.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Log.e(TAG, "trying to read input stream.");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
//            Log.e(TAG, "sb = " + sb.toString());
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return ServerResponse
        return new ServerResponse(jObj, status);
    }



    public MarkDownResponse retrieveMarkDownData(int reqType, String url, List<NameValuePair> urlParams) {
        Log.e(TAG, "in retrieveMarkdownData method");

        int status = 0;
        String mdContent = null;

        StringBuilder sb = null;

        if (urlParams != null) {
            String paramString = URLEncodedUtils.format(urlParams, "utf-8");
            url += "." + paramString;
        }
        Log.e(TAG, "url after param added = " + url);

        // Making HTTP request
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = null;

            if(reqType == AppConstants.HTTP_REQUEST_TYPE_GET){
                HttpGet httpGet = new HttpGet(url);
//                httpGet.setHeader("Content-Type", "application/json");
//                httpGet.setHeader("Accept", "application/json");

                httpResponse = httpClient.execute(httpGet);
            }

            status = httpResponse.getStatusLine().getStatusCode();
            Log.e(TAG, "STATUS = " + status);

            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
            Log.e(TAG, "is = " + is.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Log.e(TAG, "trying to read input stream.");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            Log.e(TAG, "sb = " + sb.toString());
            mdContent = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }


        // return ServerResponse
        return new MarkDownResponse(mdContent, status);
    }

}
