package com.estar.track.utils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttp3 {
    public static void sendOkHttpPostRequest(String address, RequestBody requestBody, okhttp3.Callback callback){
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(address)
                    .post(requestBody)
                    .build();
            client.newCall(request).enqueue(callback);
        }

        public static void sendOkHttpGetRequest(String address, okhttp3.Callback callback){
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                .url(address)
                .get()
                .build();
        client.newCall(request).enqueue(callback);
    }
    public static void sendOkHttpPutRequest(String address, RequestBody requestBody, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .put(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }


    public static void sendOkHttpPostTokenRequest(String address, RequestBody requestBody, String token, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .addHeader("Token",token)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendOkHttpPostWithTokensRequest(String address, RequestBody requestBody , String[] headers, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .addHeader("Token",headers[0])
                .addHeader("Cookie",headers[1])
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }



    public static void sendOkHttpGetTokenRequest(String address, String token, okhttp3.Callback callback ){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .addHeader("Token",token)
                .get()
                .build();
        client.newCall(request).enqueue(callback);
    }

    public static void sendOkHttpGetWithTokensRequest(String address, String[] token, okhttp3.Callback callback ){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .addHeader("Token",token[0])
                .addHeader("Cookie",token[1])
                .get()
                .build();
        client.newCall(request).enqueue(callback);
    }



    public static Response sendOkHttpExecuteGetTokenRequest(String address, String token) throws IOException {
        Response re = null;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .addHeader("Token",token)
                .get()
                .build();
        Call call = client.newCall(request);
        try {
            re = call.execute();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        return re;
    }
    public static Response sendOkHttpExecutePostTokenRequest(String address, RequestBody requestBody, String token) throws IOException {
        Response re = null;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .addHeader("Token",token)
                .post(requestBody)
                .build();
        re =  client.newCall(request).execute();
        return re;
    }

    public static void sendOkHttpPutTokenRequest(String address, RequestBody requestBody, String token, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .addHeader("Token",token)
                .put(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }



    }

