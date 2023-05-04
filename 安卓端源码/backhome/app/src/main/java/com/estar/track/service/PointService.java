package com.estar.track.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.model.LatLng;
import com.estar.track.activity.TracingActivity;
import com.estar.track.utils.Address;
import com.estar.track.utils.MUtil;
import com.estar.track.utils.OkHttp3;

import org.json.JSONException;


import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PointService extends Service {

    private double lat =31.979553d;
    private double lng =120.915042d;

    double lastLat = 31.980685d,lastLongi = 120.917042d; //存放上次的位置

    Timer timer;

    LatLng latLng = new LatLng(31.979553d,120.915042d);
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        Log.d("startPointService","ssssss");
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    lastLat = lastLat + MUtil.getRandom()*0.0001;
                    lastLongi = lastLongi + MUtil.getRandom()*0.0001;
                    LatLng tempLaL = new LatLng(lastLat,lastLongi);
                    Log.d("nowLatLong", "lat:"+String.valueOf(tempLaL.latitude) + " longi:"+ String.valueOf(tempLaL.longitude));
                    sendPoint2Server(tempLaL);

                    //获取到所有队员的track
                    getAllMembersTrack();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(task,0,2000);

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }


    private void sendPoint2Server(LatLng latLng) throws JSONException {
        String[] tokenAndCo = TracingActivity.getTokenCo();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("latitude",latLng.latitude);
        jsonObject.put("longitude",latLng.longitude);
        MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON_TYPE,jsonObject.toString());

        OkHttp3.sendOkHttpPostWithTokensRequest(Address.UPLOADMYPOINT, requestBody, tokenAndCo, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("fffffff","upload");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("pointresponse",response.body().string());
                Log.d("SuccesssPoint","upload");
            }
        });
    }

    private void getAllMembersTrack(){
        String[] tokenAndCo = TracingActivity.getTokenCo();
        OkHttp3.sendOkHttpGetWithTokensRequest(Address.GETTEAMALLPEOPLEPOINT, tokenAndCo, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("fffffff","upload");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String strResponse = response.body().string();
                Log.d("allMemebersTrack",strResponse);
                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject.parseObject(strResponse);
                JSONArray men = jsonObject.getJSONArray("data");
                HashMap<String,LatLng> latHash = new HashMap<>();
                for(int i = 0 ; i < men.size() ; i++){
                   JSONObject jo  = (JSONObject) men.get(i);
                   String memberName = jo.getString("entityName");
                   jo = jo.getJSONObject("latestLocation");
                   LatLng templtg = new LatLng(jo.getDouble("latitude"),jo.getDouble("longitude"));
                   latHash.put(memberName,templtg);
                }

                TracingActivity.setNowJson(com.alibaba.fastjson.JSONObject.parseObject(strResponse));
                TracingActivity.setNowHash(latHash);
            }
        });
    }


}
