package com.estar.track.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;


import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.track.R;
import com.estar.track.event.MessageEvent;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class ActingActivity extends AppCompatActivity {

    /**侧边栏**/
    private DrawerLayout personalInformationLayout; //这里是拿到一个drawer页面

    /**图片按钮**/
    ImageView leftPersonalImv;


    /**地图定位**/
//    public LocationClient mLocationClient = null;
//    private MyLocationListener myLocationListener = new MyLocationListener();
    private BaiduMap mBaiduMap;

    /**组件**/
    Button locationBtn;
    Button angleLocationBtn; //鹰眼定位btn

    /**地图显示**/
    private MapView mMapView = null;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isacting);

        init();

//        mLocationClient = new LocationClient(getApplicationContext());
//        mLocationClient.registerLocationListener(myLocationListener);
        //初始化定位
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(15f));
//        initLocationOption();
//        mLocationClient.start(); //开始定位

        /**按钮监听**/
        leftPersonalImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personalInformationLayout.openDrawer(Gravity.LEFT); // 这里拿的是整个页面 而不是单单那个fragment的id
            }
        });
//        locationBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ActingActivity.this, LocationFilter.class);
//                startActivity(intent);
//            }
//        });
        angleLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActingActivity.this, TracingActivity.class);
                startActivity(intent);
            }
        });


        /**策划菜单监听**/
        personalInformationLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }
            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
//                Toast.makeText(ActingActivity.this,"打开了",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
//                Toast.makeText(ActingActivity.this,"关闭了",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

    }


    private void init(){

        personalInformationLayout = findViewById(R.id.drawerLayout);
        leftPersonalImv =  findViewById(R.id.personal_info_drawer);
        locationBtn = findViewById(R.id.location_btn);
        angleLocationBtn = findViewById(R.id.angle_location);
        mMapView = findViewById(R.id.baidu_map);

    }

    //收到事件之后的处理方法 收到对应的事件了 来处理  参数要是订阅事件的类型
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.getMessage().equals("needpeople")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ActingActivity.this, "收到任务", Toast.LENGTH_SHORT).show();

                    new XPopup.Builder(ActingActivity.this).asConfirm("有新的任务", "是否接受任务",
                            new OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    Toast.makeText(ActingActivity.this, "接受任务成功", Toast.LENGTH_SHORT).show();
                                }
                            }).show();
                }
            });

        } else if (event.getMessage().equals("locktask")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ActingActivity.this, "锁定消息", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (event.getMessage().equals("checktask")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ActingActivity.this, "审核任务", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }



    /**声明周期**/
    @Override
    protected void onStart() {
//        mLocationInstance.start();
        super.onStart();
    }

    @Override
    protected void onStop() {
//        mLocationInstance.stop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

}
