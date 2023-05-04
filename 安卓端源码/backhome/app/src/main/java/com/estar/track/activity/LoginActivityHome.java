package com.estar.track.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.baidu.track.R;

import com.estar.track.TrackApplication;
import com.estar.track.entity.User;
import com.estar.track.service.MessageService;
import com.estar.track.utils.Address;
import com.estar.track.utils.BitmapUtil;
import com.estar.track.utils.CommonUtil;
import com.estar.track.utils.Constants;
import com.estar.track.utils.OkHttp3;
import com.estar.track.utils.SharedPreferenceUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivityHome extends AppCompatActivity {

    EditText accountEdx,passwordEdx;
    Button loginBtn;
    private String permissionInfo;

    //做一些必要的初始化
    private TrackApplication trackApp;

    /**权限**/
    String[] permissions = {Manifest.permission.WRITE_SETTINGS};

    private static final MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");

    private final int SDK_PERMISSION_REQUEST = 127;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);
        init();

        BitmapUtil.init();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jo = new JSONObject();
                jo.put("username",accountEdx.getText().toString());
                jo.put("password",passwordEdx.getText().toString());
                RequestBody requestBody = RequestBody.create(JSON_TYPE,jo.toString());

                OkHttp3.sendOkHttpPostRequest(Address.LOGINADDRESS, requestBody, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivityHome.this,"网络连接失败",Toast.LENGTH_SHORT).show();
                            }
                        });
                        Intent intent = new Intent(LoginActivityHome.this, TracingActivity.class);
                        startActivity(intent);

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String resBody = response.body().string();
                        JSONObject jsonObject = JSONObject.parseObject(resBody);
                        if ( jsonObject.getString("code").equals("20001")){

                            /**获取cookie**/
                            Headers headers = response.headers();
                            List<String> cookies = headers.values("Set-Cookie");
                            String cookie = "";
                            if(cookies.size() > 0){
                                 cookie = cookies.get(0);
                            }
                            if(cookie !=null){
                                String [] coos = cookie.split(";");
                                cookie = coos[0];
                            }
//                            Intent intent = new Intent(LoginActivityHome.this, TracingActivity.class);
                            //开启页面
                            Intent intent = new Intent(LoginActivityHome.this,MainAllTabActivity.class);

                            intent.putExtra("token",jsonObject.getJSONObject("data").getString("token"));
                            intent.putExtra("cookie",cookie);
                            intent.putExtra("user",jsonObject.getJSONObject("data").getString("user"));

                            MessageService.setUser(jsonObject.getJSONObject("data").getString("user"));
                            Intent serviceIntent = new Intent(LoginActivityHome.this, MessageService.class);
                            startService(serviceIntent);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivityHome.this,"登录成功",Toast.LENGTH_SHORT).show();
                                }
                            });
                            startActivity(intent);
                        }
                    }
                });
            }
        });
        getPersimmions();
    }

    /**初始化组件**/
    private void init(){
        accountEdx = findViewById(R.id.account_edx_main);
        passwordEdx = findViewById(R.id.pwd_edx_main);
        loginBtn = findViewById(R.id.login_btn_main);

        //一些必要的初始化
        trackApp = (TrackApplication) getApplicationContext();
        //获取权限
        boolean isContainsPermissionKey = SharedPreferenceUtil.contains(this, Constants.PERMISSIONS_DESC_KEY);
        if (!isContainsPermissionKey) {
            showDialog();
        } else {
            boolean isAccessPermission = SharedPreferenceUtil
                    .getBoolean(this, Constants.PERMISSIONS_DESC_KEY, false);
            if (!isAccessPermission) {
                showDialog();
            }
        }
    }


    /**
     * 显示提示信息
     */
    private void showDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示: ");
        TextView textView = new TextView(this);
        textView.setText(R.string.privacy_permission_desc);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setPadding( 50 , 30 , 50 , 10 );
        builder.setView(textView);

        builder.setPositiveButton("同意", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferenceUtil
                        .putBoolean(LoginActivityHome.this, Constants.PERMISSIONS_DESC_KEY, true);
                dialog.cancel();
            }
        });

        builder.setNegativeButton("不同意", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferenceUtil
                        .putBoolean(LoginActivityHome.this, Constants.PERMISSIONS_DESC_KEY, false);
                dialog.cancel();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            /*
             * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
             */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoginActivityHome.this,permissionInfo,Toast.LENGTH_SHORT).show();
                    }
                });
            }
            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
        /**shouldShowRequestPermissionRationale
         * 1，在允许询问时返回true ； 2，在权限通过 或者权限被拒绝并且禁止询问时返回false
         * 但是有一个例外，就是重来没有询问过的时候，也是返回的false 所以单纯的使用shouldShowRequestPermissionRationale去做什么判断，是没用的，只能在请求权限回调后再使用。
         * Google的原意是：
         * 1，没有申请过权限，申请就是了，所以返回false； 2，申请了用户拒绝了，那你就要提示用户了，所以返回true；
         * 3，用户选择了拒绝并且不再提示，那你也不要申请了，也不要提示用户了，所以返回false； 4，已经允许了，不需要申请也不需要提示，所以返回false；
        **/
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(permission)) {//允许询问 返回true
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }
        } else {
            return false;//表明已经拿到权限了 不需要添加了
        }
    }
    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        CommonUtil.saveCurrentLocation(trackApp);
        if (trackApp.trackConf.contains("is_trace_started")
                && trackApp.trackConf.getBoolean("is_trace_started", true)) {
            // 退出app停止轨迹服务时，不再接收回调，将OnTraceListener置空
            trackApp.mClient.setOnTraceListener(null);
            trackApp.mClient.stopTrace(trackApp.mTrace, null);
        } else {
            trackApp.mClient.clear();
        }
        trackApp.isTraceStarted = false;
        trackApp.isGatherStarted = false;
        SharedPreferences.Editor editor = trackApp.trackConf.edit();
        editor.remove("is_trace_started");
        editor.remove("is_gather_started");
        editor.apply();

        BitmapUtil.clear();

    }
}
