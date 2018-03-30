package com.lnbinfotech.msplfootwearex.post;

import com.lnbinfotech.msplfootwearex.constant.Constant;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;

// Created by lnb on 8/11/2016.

public class Post {

    static public String POST(String url) {
        String responseBody = null;
        try{
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams,Constant.TIMEOUT_CON);
            HttpConnectionParams.setSoTimeout(httpParams,Constant.TIMEOUT_SO);
            HttpClient httpclient = new DefaultHttpClient(httpParams);
            HttpGet httpget = new HttpGet(url);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            responseBody = httpclient.execute(httpget, responseHandler);
        }catch (Exception e){
            e.printStackTrace();
            Constant.showLog("POST - Timeout-"+url);
        }
        return responseBody;

        /*final DefaultHttpClient client = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(u);
        try{
            HttpResponse response = client.execute(httpget);
            final int statuscode = response.getStatusLine().getStatusCode();
            if(statuscode != HttpStatus.SC_OK){
                return null;
            }
            final HttpEntity entity = response.getEntity();
            responseBody = new BasicResponseHandler().handleResponse(response);
        }catch(IOException e){
            httpget.abort();
            e.printStackTrace();
        }
        return  responseBody;*/
    }
}
