package com.estar.track.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.alibaba.fastjson.JSONObject;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.trace.api.entity.OnEntityListener;
import com.baidu.trace.api.fence.FenceAlarmPushInfo;
import com.baidu.trace.api.fence.MonitoredAction;
import com.baidu.trace.api.track.LatestPoint;
import com.baidu.trace.api.track.LatestPointResponse;
import com.baidu.trace.api.track.OnTrackListener;
import com.baidu.trace.model.LocationMode;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.PushMessage;
import com.baidu.trace.model.SortType;
import com.baidu.trace.model.StatusCodes;
import com.baidu.trace.model.TraceLocation;
import com.baidu.track.R;
import com.estar.mapapi.overlayutil.WalkingRouteOverlay;
import com.estar.track.Listener.WSListener;
import com.estar.track.Listener.WsManager;
import com.estar.track.TrackApplication;
import com.estar.track.entity.Point;
import com.estar.track.entity.User;
import com.estar.track.event.MessageEvent;
import com.estar.track.fragment.LeftPersonalInformationFragment;
import com.estar.track.model.CurrentLocation;
import com.estar.track.receiver.TrackReceiver;
import com.estar.track.utils.Address;
import com.estar.track.utils.CommonUtil;
import com.estar.track.utils.Constants;
import com.estar.track.utils.MUtil;
import com.estar.track.utils.MapUtil;
import com.estar.track.utils.OkHttp3;
import com.estar.track.utils.SharedPreferenceUtil;
import com.estar.track.utils.ViewUtil;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.michaldrabik.tapbarmenulib.TapBarMenu;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 轨迹追踪
 */
public class TracingActivity extends BaseActivity implements View.OnClickListener, OnGetGeoCoderResultListener {

    private TrackApplication trackApp = null;

    private ViewUtil viewUtil = null;

    private Button traceBtn = null;

    private Button sendMessage2crawsBtn = null;

    private Button openTabBtn = null;

    private Button oldmanInfoBtnTra;
    private Button taskInfoTrac;


    private Button walkLineBtn = null; //步行线路

    private NotificationManager notificationManager = null;

    private PowerManager powerManager = null;

    private PowerManager.WakeLock wakeLock = null;

    private TrackReceiver trackReceiver = null;

    /**
     * 地图工具
     */
    public MapUtil mapUtil = null;

    /**
     * 轨迹服务监听器
     */
    private OnTraceListener traceListener = null;

    /**
     * 轨迹监听器(用于接收纠偏后实时位置回调)
     */
    private OnTrackListener trackListener = null;

    /**
     * Entity监听器(用于接收实时定位回调)
     */
    private OnEntityListener entityListener = null;

    /**
     * 实时定位任务
     */
    private RealTimeHandler realTimeHandler = new RealTimeHandler();

    private RealTimeLocRunnable realTimeLocRunnable = null;

    private boolean isRealTimeRunning = true;

    private int notifyId = 0;

    /**
     * 打包周期
     */
    public int packInterval = Constants.DEFAULT_PACK_INTERVAL;

    /**侧边栏**/
    private DrawerLayout personalInformationLayout; //这里是拿到一个drawer页面

    /**图片按钮**/
    ImageView leftPersonalImv;

    /**
     * 是否需要画大圈范围
     */
    Boolean isNeededDrawCircle = true;

    /**
     * 当前登录用户
     */
    User loginUser = null;

    /**
     * 当前用户TOKEN
     * 还有Cookie
     */
    public static String TOKEN = "";
    public static String COOKIE = "";
    public String USER = "";
    public String CANGO = "";
    String[] headers;


    /**侧拉框Fra**/
    public LeftPersonalInformationFragment leftFragment = new LeftPersonalInformationFragment();

    /**初始化WSManager**/
    WsManager wsManager ;

    /**底部拉的框**/

    TapBarMenu tapBarMenu;

    /**当前位置**/
    static LatLng nowLtL;
    double lastLat = 31.980685,lastLongi = 120.917042; //存放上次的位置
    //当前所有人的位置
    static JSONObject nowAllMenAddress = new JSONObject();

    //绘制地图上的线
    List<LatLng> pointList = new ArrayList<LatLng>();
    static HashMap<String,List<LatLng>> memberPoints = new HashMap<>();

    //步行导航路线搜寻
    RoutePlanSearch mSearch = RoutePlanSearch.newInstance();

    //步行导航监听器
    OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
        @Override
        public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
            //创建WalkingRouteOverlay
            if(mapUtil !=null){
                WalkingRouteOverlay overlay = new WalkingRouteOverlay(mapUtil.getBaiduMap());
                if(walkingRouteResult.getRouteLines().size() > 0){
                    overlay.setData(walkingRouteResult.getRouteLines().get(0)); //返回的第一条数据
                    //在地图上绘制
                    overlay.addToMap();

                    Log.d("MapSS","地图绘制成功");
                }
            }else {
                Log.d("FFF","获取地图失败");
            }
        }

        /**地理栅栏的中心**/
        LatLng center = null;

        @Override
        public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

        }

        @Override
        public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

        }

        @Override
        public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

        }

        @Override
        public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

        }

        @Override
        public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
        }
    };
    /**
     * 起点和终点
     */
//    PlanNode startNode = PlanNode.withCityNameAndPlac eName("北京", "西二旗地铁站");
//    PlanNode endNode = PlanNode.withCityNameAndPlaceName("北京","百度科技园");
    PlanNode startNode = PlanNode.withLocation(new LatLng(33.479362,118.222739));
    PlanNode endNode = PlanNode.withLocation(new LatLng(33.509662,118.252839));


    public static LatLng getLatLng(){
        return nowLtL;
    }
    public static String[] getTokenCo(){
        return new String[]{TOKEN,COOKIE};
    }

    //Handler
    private  Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x001:
                    //这里设置侧滑框的个人信息
                    /**处理左拉框**/
                    leftFragment.initWidget(TracingActivity.this,loginUser);
                    /**拿到信息之后初始化Manager*/
                    wsManager = new WsManager.Builder(getBaseContext())
                            .client(
                                    new OkHttpClient().newBuilder()
                                            .pingInterval(15, TimeUnit.SECONDS)
                                            .retryOnConnectionFailure(true)
                                            .build()
                            )
                            .needReconnect(true)
                            .wsUrl(Address.WSIP + USER).build();
                    wsManager.startConnect();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.tracing_title);
        setOnClickListener(this);
        init();


        /**
         * 拿到token
         */
        Intent intent = getIntent();
        headers = new String[2];
        TOKEN = intent.getStringExtra("token");
        COOKIE = intent.getStringExtra("cookie");
        USER = intent.getStringExtra("user");
        CANGO = intent.getStringExtra("cango");
        if (CANGO == null){
            return;
        }else {
            if(CANGO.equals("停出任务")){
                leftFragment.setStatusAndSwitch(TracingActivity.this,CANGO);
            }else {
                leftFragment.setStatusAndSwitch(TracingActivity.this,CANGO);
            }
        }


        headers[0] = TOKEN;
        headers[1] = COOKIE;
        Log.d("token",TOKEN);

        EventBus.getDefault().register(this);

        /**侧滑菜单监听**/
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
        /**图片按钮监听**/
        leftPersonalImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personalInformationLayout.openDrawer(Gravity.LEFT); // 这里拿的是整个页面 而不是单单那个fragment的id
            }
        });

        //根据当前用户TOKEN获得用户信息
        getVolunteerInfo();

    }




    private void getVolunteerInfo(){
        OkHttp3.sendOkHttpGetWithTokensRequest(Address.USERINFOADDRESS,headers , new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(TracingActivity.this,"网络连接失败,请检查网络连接。",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String resbody = response.body().string();
                JSONObject jsonObject = JSONObject.parseObject(resbody);
                Log.d("allJSON",jsonObject.toString());
                jsonObject = jsonObject.getJSONObject("data");
                Log.d("dataJSON",jsonObject.toString());
                int gender;
                if(jsonObject.getString("gender").equals("MALE")){
                    gender = 1;
                }else
                {
                    gender = 2 ;
                }
                loginUser = new User(
                        jsonObject.getString("id"),
                        jsonObject.getString("name"),
                        gender,
                        jsonObject.getInteger("age"),
                        jsonObject.getString("phoneNumber"),
                        jsonObject.getString("transportationType"),
                        jsonObject.getBoolean("canGo"),
                        jsonObject.getString("userId"),
                        jsonObject.getString("groupId")
                );
                mHandler.sendEmptyMessage(0x001);
            }
        });
    }

    private void init() {
        initListener();
        trackApp = (TrackApplication) getApplicationContext();
        viewUtil = new ViewUtil();
        mapUtil = MapUtil.getInstance();
        mapUtil.init((MapView) findViewById(R.id.tracing_mapView));
        mapUtil.setCenter(trackApp);
        startRealTimeLoc(3); //获得实时位置的时间间隔
        powerManager = (PowerManager) trackApp.getSystemService(Context.POWER_SERVICE);

        traceBtn = (Button) findViewById(R.id.finish_task);
        sendMessage2crawsBtn = (Button) findViewById(R.id.btn_send_2_friends);
        walkLineBtn = (Button) findViewById(R.id.btn_walk_line);

        traceBtn.setOnClickListener(this);
        sendMessage2crawsBtn.setOnClickListener(this);
        walkLineBtn.setOnClickListener(this);
//        setTraceBtnStyle();
//        setGatherBtnStyle();

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        IntentFilter intentFilter = new IntentFilter();
        // 点击home键广播，由系统发出
        intentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(mHomeAndLockReceiver, intentFilter);

        //设置顶部组件null
        setOptionsButtonInVisible();

        //左拉和图片
        personalInformationLayout = findViewById(R.id.drawerLayout_t);  //这里要拿本页面的fragment组件的id
        leftPersonalImv =  findViewById(R.id.personal_info_drawer_t);

        //设置路线规划检索监听器
        mSearch.setOnGetRoutePlanResultListener(listener);

        //两个按钮
        oldmanInfoBtnTra = (Button) findViewById(R.id.oldman_info_btn_tra);
        taskInfoTrac = (Button) findViewById(R.id.task_info_trac);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 追踪选项设置
            case R.id.btn_activity_options:
                ViewUtil.startActivityForResult(this, TracingOptionsActivity.class, Constants
                        .REQUEST_CODE);
                break;

            case R.id.finish_task:

                Intent intent = new Intent(TracingActivity.this,TaskFinishTask.class);
                startActivity(intent);
                break;

            case R.id.btn_send_2_friends:
                //打开发送消息页面
                Intent intent2 = new Intent(TracingActivity.this,ActivitySendMessage.class);
                startActivity(intent2);
                break;
            case R.id.btn_walk_line:
                mSearch.walkingSearch((new WalkingRoutePlanOption())
                        .from(startNode)
                        .to(endNode)
                );
                changeMapLocation(startNode.getLocation());
                break;

            default:
                break;
        }

    }


    /**
     * 更改地图视角到某点极其周边去
     */
    private void changeMapLocation(LatLng l1){

        //视角跳转过去
//        LatLng l1 = startNode.getLocation();
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(l1).zoom(18.0f);
        if(mapUtil.baiduMap !=null ){
            mapUtil.baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }

    }

    //latitude 纬度 左右  longitude 经度 上下
    //恒纬竖经

    /**
     * 设置服务按钮样式
     */
    private void setTraceBtnStyle() {
        boolean isTraceStarted = trackApp.trackConf.getBoolean("is_trace_started", false);
        if (isTraceStarted) {
//            traceBtn.setText(R.string.stop_trace);
            traceBtn.setTextColor(ResourcesCompat.getColor(getResources(), R.color
                    .white, null));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                traceBtn.setBackground(ResourcesCompat.getDrawable(getResources(),
                        R.mipmap.bg_btn_sure, null));
            } else {
                traceBtn.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(),
                        R.mipmap.bg_btn_sure, null));
            }
        } else {
//            traceBtn.setText(R.string.start_trace);
            traceBtn.setTextColor(ResourcesCompat.getColor(getResources(), R.color.layout_title, null));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                traceBtn.setBackground(ResourcesCompat.getDrawable(getResources(),
                        R.mipmap.bg_btn_cancel, null));
            } else {
                traceBtn.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(),
                        R.mipmap.bg_btn_cancel, null));
            }
        }
    }

    /**
     * 设置采集按钮样式
     */


    /**
     * 实现从经纬度获取当前位置
     * 和从当前位置获得经纬度
     * @param geoCodeResult
     */
    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

    }
    /**
     * 实时定位任务
     *
     * @author baidu
     */
    class RealTimeLocRunnable implements Runnable {

        private int interval = 0;

        public RealTimeLocRunnable(int interval) {
            this.interval = interval;
        }

        @Override
        public void run() {
            if (isRealTimeRunning) {
                trackApp.getCurrentLocation(entityListener, trackListener);
                realTimeHandler.postDelayed(this, interval * 1000);
//                realTimeHandler.postDelayed(this,50);
            }
        }
    }

    public void startRealTimeLoc(int interval) {
        Log.d("Tracingggggg","istracing");
        isRealTimeRunning = true;
        realTimeLocRunnable = new RealTimeLocRunnable(interval);
        realTimeHandler.post(realTimeLocRunnable);
    }

    public void stopRealTimeLoc() {
        isRealTimeRunning = false;
        if (null != realTimeHandler && null != realTimeLocRunnable) {
            realTimeHandler.removeCallbacks(realTimeLocRunnable);
        }
        trackApp.mClient.stopRealTimeLoc();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null == data) {
            return;
        }

        if (data.hasExtra("locationMode")) {
            LocationMode locationMode = LocationMode.valueOf(data.getStringExtra("locationMode"));
            trackApp.mClient.setLocationMode(locationMode);
        }

        if (data.hasExtra("isNeedObjectStorage")) {
            boolean isNeedObjectStorage = data.getBooleanExtra("isNeedObjectStorage", false);
            trackApp.mTrace.setNeedObjectStorage(isNeedObjectStorage);
        }

        if (data.hasExtra("gatherInterval") || data.hasExtra("packInterval")) { //设置收集间隔和打包间隔
            int gatherInterval = data.getIntExtra("gatherInterval", 1); //这里是收集位置的间隔
            int packInterval = data.getIntExtra("packInterval", Constants.DEFAULT_PACK_INTERVAL);
            TracingActivity.this.packInterval = packInterval;
            trackApp.mClient.setInterval(gatherInterval, packInterval);
        }

        //        if (data.hasExtra("supplementMode")) {
        //            mSupplementMode = SupplementMode.valueOf(data.getStringExtra("supplementMode"));
        //        }
    }

    private void initListener() {

        trackListener = new OnTrackListener() {

            @Override
            public void onLatestPointCallback(LatestPointResponse response) {
                if (StatusCodes.SUCCESS != response.getStatus()) {
                    return;
                }

                LatestPoint point = response.getLatestPoint();
                if (null == point || CommonUtil.isZeroPoint(point.getLocation().getLatitude(), point.getLocation()
                        .getLongitude())) {
                    return;
                }

                LatLng currentLatLng = mapUtil.convertTrace2Map(point.getLocation());
                if (null == currentLatLng) {
                    return;
                }
                CurrentLocation.locTime = point.getLocTime();
                CurrentLocation.latitude = currentLatLng.latitude;
                CurrentLocation.longitude = currentLatLng.longitude;

                if (null != mapUtil) {
                    mapUtil.updateStatus(currentLatLng, true);
                }
            }
        };




        entityListener = new OnEntityListener() {
            @Override
            public void onReceiveLocation(TraceLocation location) {

                if (StatusCodes.SUCCESS != location.getStatus() || CommonUtil.isZeroPoint(location.getLatitude(),
                        location.getLongitude())) {
                    return;
                }
                LatLng currentLatLng = mapUtil.convertTraceLocation2Map(location);
                if (null == currentLatLng) {
                    return;
                }
                CurrentLocation.locTime = CommonUtil.toTimeStamp(location.getTime());
                CurrentLocation.latitude = currentLatLng.latitude;
                CurrentLocation.longitude = currentLatLng.longitude;

                if (null != mapUtil) {
                    mapUtil.updateStatus(currentLatLng, true);

                    //画圆圈
                    if(isNeededDrawCircle){
                        drawActionCircle(currentLatLng,1400,mapUtil.baiduMap);
                        isNeededDrawCircle = false;
                    }
                }
                Log.d("locationSituation",location.toString());

//                LatLng p1 = new LatLng(location.getLatitude(),location.getLongitude());
                lastLat = lastLat + MUtil.getRandom()*0.0001;
                lastLongi = lastLongi + MUtil.getRandom()*0.0001;
                nowLtL = new LatLng(lastLat,lastLongi);
//                nowLtL = new LatLng(location.getLatitude(),location.getLongitude());
//                pointList.add(p1);
                pointList.add(nowLtL);

                Boolean needRemove = true;

                for(String  member : memberPoints.keySet()){
                    ArrayList<LatLng> tempLatLng = (ArrayList<LatLng>) memberPoints.get(member);

                    mapUtil.drawHistoryTrack(tempLatLng, SortType.asc,mapUtil.baiduMap,currentLatLng,1);
//                    mapUtil.drawHistoryTrack(tempLatLng, SortType.asc,mapUtil.baiduMap,currentLatLng,1);
                    needRemove = false;
                }

                //TODO 关闭视角转动
//                mapUtil.drawHistoryTrack(pointList, SortType.asc,mapUtil.baiduMap,currentLatLng,0);
                if(needRemove){
                    mapUtil.removeOverlay();
                }
                mapUtil.drawAnotherMemberHistoryTrack(pointList, SortType.asc,mapUtil.baiduMap,currentLatLng,0);



            }

        };

        traceListener = new OnTraceListener() {

            /**
             * 绑定服务回调接口
             * @param errorNo  状态码
             * @param message 消息
             *                <p>
             *                <pre>0：成功 </pre>
             *                <pre>1：失败</pre>
             */
            @Override
            public void onBindServiceCallback(int errorNo, String message) {
                viewUtil.showToast(TracingActivity.this,
                        String.format("onBindServiceCallback, errorNo:%d, message:%s ", errorNo, message));
            }


            /**
             * 开启服务回调接口
             * @param errorNo 状态码
             * @param message 消息
             *                <p>
             *                <pre>0：成功 </pre>
             *                <pre>10000：请求发送失败</pre>
             *                <pre>10001：服务开启失败</pre>
             *                <pre>10002：参数错误</pre>
             *                <pre>10003：网络连接失败</pre>
             *                <pre>10004：网络未开启</pre>
             *                <pre>10005：服务正在开启</pre>
             *                <pre>10006：服务已开启</pre>
             */
            @Override
            public void onStartTraceCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.START_TRACE_NETWORK_CONNECT_FAILED <= errorNo) {
                    trackApp.isTraceStarted = true;
                    SharedPreferences.Editor editor = trackApp.trackConf.edit();
                    editor.putBoolean("is_trace_started", true);
                    editor.apply();
//                    setTraceBtnStyle();
                    registerReceiver();
                }
                viewUtil.showToast(TracingActivity.this,
                        String.format("onStartTraceCallback, errorNo:%d, message:%s ", errorNo, message));
            }

            /**
             * 停止服务回调接口
             * @param errorNo 状态码
             * @param message 消息
             *                <p>
             *                <pre>0：成功</pre>
             *                <pre>11000：请求发送失败</pre>
             *                <pre>11001：服务停止失败</pre>
             *                <pre>11002：服务未开启</pre>
             *                <pre>11003：服务正在停止</pre>
             */
            @Override
            public void onStopTraceCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.CACHE_TRACK_NOT_UPLOAD == errorNo) {
                    trackApp.isTraceStarted = false;
                    trackApp.isGatherStarted = false;
                    // 停止成功后，直接移除is_trace_started记录（便于区分用户没有停止服务，直接杀死进程的情况）
                    SharedPreferences.Editor editor = trackApp.trackConf.edit();
                    editor.remove("is_trace_started");
                    editor.remove("is_gather_started");
                    editor.apply();
//                    setTraceBtnStyle();
//                    setGatherBtnStyle();
                    unregisterPowerReceiver();
                }
                viewUtil.showToast(TracingActivity.this,
                        String.format("onStopTraceCallback, errorNo:%d, message:%s ", errorNo, message));
            }

            /**
             * 开启采集回调接口
             * @param errorNo 状态码
             * @param message 消息
             *                <p>
             *                <pre>0：成功</pre>
             *                <pre>12000：请求发送失败</pre>
             *                <pre>12001：采集开启失败</pre>
             *                <pre>12002：服务未开启</pre>
             */
            @Override
            public void onStartGatherCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.GATHER_STARTED == errorNo) {
                    trackApp.isGatherStarted = true;
                    SharedPreferences.Editor editor = trackApp.trackConf.edit();
                    editor.putBoolean("is_gather_started", true);
                    editor.apply();
//                    setGatherBtnStyle();
                }
                viewUtil.showToast(TracingActivity.this,
                        String.format("onStartGatherCallback, errorNo:%d, message:%s ", errorNo, message));
            }

            /**
             * 停止采集回调接口
             * @param errorNo 状态码
             * @param message 消息
             *                <p>
             *                <pre>0：成功</pre>
             *                <pre>13000：请求发送失败</pre>
             *                <pre>13001：采集停止失败</pre>
             *                <pre>13002：服务未开启</pre>
             */
            @Override
            public void onStopGatherCallback(int errorNo, String message) {
                if (StatusCodes.SUCCESS == errorNo || StatusCodes.GATHER_STOPPED == errorNo) {
                    trackApp.isGatherStarted = false;
                    SharedPreferences.Editor editor = trackApp.trackConf.edit();
                    editor.remove("is_gather_started");
                    editor.apply();
//                    setGatherBtnStyle();
                }
                viewUtil.showToast(TracingActivity.this,
                        String.format("onStopGatherCallback, errorNo:%d, message:%s ", errorNo, message));
            }

            /**
             * 推送消息回调接口
             *
             * @param messageType 状态码
             * @param pushMessage 消息
             *                  <p>
             *                  <pre>0x01：配置下发</pre>
             *                  <pre>0x02：语音消息</pre>
             *                  <pre>0x03：服务端围栏报警消息</pre>
             *                  <pre>0x04：本地围栏报警消息</pre>
             *                  <pre>0x05~0x40：系统预留</pre>
             *                  <pre>0x41~0xFF：开发者自定义</pre>
             */
            @Override
            public void onPushCallback(byte messageType, PushMessage pushMessage) {
                if (messageType < 0x03 || messageType > 0x04) {
                    viewUtil.showToast(TracingActivity.this, pushMessage.getMessage());
                    return;
                }
                FenceAlarmPushInfo alarmPushInfo = pushMessage.getFenceAlarmPushInfo();
                if (null == alarmPushInfo) {
                    viewUtil.showToast(TracingActivity.this,
                            String.format("onPushCallback, messageType:%d, messageContent:%s ", messageType,
                                    pushMessage));
                    return;
                }
                StringBuffer alarmInfo = new StringBuffer();
                alarmInfo.append("您于")
                        .append(CommonUtil.getHMS(alarmPushInfo.getCurrentPoint().getLocTime() * 1000))
                        .append(alarmPushInfo.getMonitoredAction() == MonitoredAction.enter ? "进入" : "离开")
                        .append(messageType == 0x03 ? "云端" : "本地")
                        .append("围栏：").append(alarmPushInfo.getFenceName());

                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                    Notification notification = new Notification.Builder(trackApp)
                            .setContentTitle(getResources().getString(R.string.alarm_push_title))
                            .setContentText(alarmInfo.toString())
                            .setSmallIcon(R.mipmap.icon_app)
                            .setWhen(System.currentTimeMillis()).build();
                    notificationManager.notify(notifyId++, notification);
                }
            }

            @Override
            public void onInitBOSCallback(int errorNo, String message) {
                viewUtil.showToast(TracingActivity.this,
                        String.format("onInitBOSCallback, errorNo:%d, message:%s ", errorNo, message));
            }
        };
    }

    static class RealTimeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    }

    public static void setNowJson(JSONObject json){
        nowAllMenAddress = json;
//        memberPoints.put("")
    }

    //存入hash
    public static void setNowHash(HashMap<String,LatLng> Lathash){

        for(String member : Lathash.keySet()){
            LatLng latLng = Lathash.get(member);  //当前队员的当前LatLng
            if(memberPoints.containsKey(member)){
                ArrayList<LatLng> thisMemberPoints = (ArrayList<LatLng>) memberPoints.get(member);
                thisMemberPoints.add(latLng);
                memberPoints.put(member,thisMemberPoints);
            }else {
                ArrayList<LatLng> thisMemberPoints = new ArrayList<>();
                thisMemberPoints.add(latLng);
                memberPoints.put(member,thisMemberPoints);
            }
        }
    }



    /**
     * 注册广播（电源锁、GPS状态）
     */
    @SuppressLint("InvalidWakeLockTag")
    private void registerReceiver() {
        if (trackApp.isRegisterReceiver) {
            return;
        }

        if (null == wakeLock) {
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "track upload");
        }
        if (null == trackReceiver) {
            trackReceiver = new TrackReceiver(wakeLock);
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        filter.addAction(StatusCodes.GPS_STATUS_ACTION);
        trackApp.registerReceiver(trackReceiver, filter);
        trackApp.isRegisterReceiver = true;

    }

    private void unregisterPowerReceiver() {
        if (!trackApp.isRegisterReceiver) {
            return;
        }
        if (null != trackReceiver) {
            trackApp.unregisterReceiver(trackReceiver);
        }
        trackApp.isRegisterReceiver = false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 适配android M，检查权限
        List<String> permissions = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && isNeedRequestPermissions(permissions)) {
            requestPermissions(permissions.toArray(new String[permissions.size()]), 0);
        }
        startRealTimeLoc(packInterval);
    }

    private boolean isNeedRequestPermissions(List<String> permissions) {
        // 定位精确位置
        addPermission(permissions, Manifest.permission.ACCESS_FINE_LOCATION);
        // 存储权限
        addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // 前台服务权限
            addPermission(permissions, Manifest.permission.FOREGROUND_SERVICE);
        }

        return permissions.size() > 0;
    }

    private void addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapUtil.onResume();
        requestBackgroundLocationPermission();
        // 在Android 6.0及以上系统，若定制手机使用到doze模式，请求将应用添加到白名单。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = trackApp.getPackageName();
            boolean isIgnoring = powerManager.isIgnoringBatteryOptimizations(packageName);
            if (!isIgnoring) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                try {
                    startActivity(intent);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void requestBackgroundLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                            0);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (trackApp.isGatherStarted) {
            showDialog(getResources().getString(R.string.background_privacy_desc));
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 显示提示信息
     */
    private void showDialog(String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示: ");
        builder.setMessage(message);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                TracingActivity.super.onBackPressed();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (trackApp.isGatherStarted) {
                    trackApp.mClient.stopGather(traceListener);
                    trackApp.mClient.stopTrace(trackApp.mTrace, traceListener);
                    stopRealTimeLoc();
                    dialog.cancel();
                    TracingActivity.super.onBackPressed();
                }
            }
        });

        builder.setCancelable(false);
        builder.show();
    }

    /**
     * 监听是否点击了home键将客户端退到后台
     */
    private BroadcastReceiver mHomeAndLockReceiver = new BroadcastReceiver() {
        final String SYSTEM_DIALOG_REASON_KEY = "reason";
        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
        final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
        final String MESSAGE = " 根据相关法律法规规定，切换到后台后，若无必要可不必收集用户信息。" ;

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (TextUtils.equals(reason, SYSTEM_DIALOG_REASON_HOME_KEY)) {
                    if (trackApp.isGatherStarted) {
                        viewUtil.showToast(TracingActivity.this, MESSAGE);
                    }
                } else if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
                    if (trackApp.isGatherStarted) {
                        viewUtil.showToast(TracingActivity.this, MESSAGE);
                    }
                }
            }
        }
    };

    /**
     * 画一个圆形范围
     * @param center
     * @param radius
     * @param map
     */
    public static void drawActionCircle(LatLng center,int radius , BaiduMap map){
        CircleOptions mCircleOptions = new CircleOptions()
                .center(center)
                .radius(radius)
                .fillColor(0x20FFFF33)//填充颜色
                .stroke(new Stroke(5,0xAAFF3333)); //边框和颜色
        Overlay mCircle = map.addOverlay(mCircleOptions);
    }

    //收到事件之后的处理方法 收到对应的事件了 来处理  参数要是订阅事件的类型
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.getMessage().equals("needpeople")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(TracingActivity.this, "收到任务", Toast.LENGTH_SHORT).show();

                    new XPopup.Builder(TracingActivity.this).asConfirm("有新的任务", "是否接受任务",
                            new OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    Toast.makeText(TracingActivity.this, "接受任务成功", Toast.LENGTH_SHORT).show();
                                }
                            }).show();
                }
            });

        } else if (event.getMessage().equals("locktask")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(TracingActivity.this, "锁定消息", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (event.getMessage().equals("checktask")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(TracingActivity.this, "审核任务", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        mapUtil.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopRealTimeLoc();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapUtil.clear();
        unregisterReceiver(mHomeAndLockReceiver);
        stopRealTimeLoc();
        mSearch.destroy(); //步行检索实例
        EventBus.getDefault().unregister(this);

    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_tracing;
    }

}
