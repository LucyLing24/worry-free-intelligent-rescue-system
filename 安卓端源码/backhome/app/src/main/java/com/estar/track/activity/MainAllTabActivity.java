package com.estar.track.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.track.R;
import com.estar.track.adapter.WaitRecvTasksAdapter;
import com.estar.track.entity.Task;
import com.estar.track.entity.User;
import com.estar.track.event.MessageEvent;
import com.estar.track.fragment.HomeFragment;
import com.estar.track.fragment.LeftPersonalInformationFragment;
import com.estar.track.fragment.MeFragment;
import com.estar.track.fragment.MessageFragment;
import com.estar.track.fragment.TeamsFragment;
import com.estar.track.utils.Address;
import com.estar.track.utils.OkHttp3;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainAllTabActivity extends FragmentActivity  {

    //声明四个Tab的布局文件
    private RelativeLayout mTabTeams;
    private RelativeLayout mTabHome;
    private RelativeLayout mTabMessage;
    private RelativeLayout mTabMe;


    //声明四个Tab的ImageButton
    private ImageView mTeams;
    private ImageView mHome;
    private ImageView mMessage;
    private ImageView mMe;

    //声明四个Tab分别对应的Fragment
    private Fragment mFragmentTeams;
    private Fragment mFragmentHome;
    private Fragment mFragmentMessage;
    private Fragment mFragmentMe;

    private HomeFragment homeFragment;

    //TOP
    private TextView TopTv;

    /**当前Task 可以获得老人信息、队员信息**/
    Task task = new Task();
    //当前队友
    List<User> nowMissionUsers = new ArrayList<>();
    //当前任务列表
    List<Task> taskList = new ArrayList<>();


    /**公共可获取token和cookie**/
    public static String[] headers = new String[2];


    /**
     * 左拉框
     * @param savedInstanceState
     */
    private DrawerLayout personalInformationLayout; //这里是拿到一个drawer页面
    public LeftPersonalInformationFragment leftFragment = new LeftPersonalInformationFragment();


    /**用户intent**/
    Intent intent; //从登录页面传来的用户数据

    //处理Tab点击
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.activity_main_all_tab);
            initViews();//初始化控件

            selectTab(1);//默认选中第一个Tab

            intent = getIntent();

            getPostVolInfo(intent);

            getandSetMissions(intent);

            getMissions(intent);

            /**主mFragmentHome数据设置**/
            ((HomeFragment)mFragmentHome).setLoginIntent(intent); //页面初始化好之后就开始设置

            //开启Ev
            EventBus.getDefault().register(this);


            mTabTeams.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TopTv.setText("队友");
                    selectTab(0);//当点击的是微信的Tab就选中微信的Tab
                    //这边选中初始化之后发送过去
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((TeamsFragment)mFragmentTeams).setTask(task);
                        }
                    });
                }
            });
            mTabHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectTab(1);//当点击的是微信的Tab就选中微信的Tab
                    TopTv.setText("首页");
                }
            });
            mTabMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TopTv.setText("公告");
                    selectTab(2);//当点击的是微信的Tab就选中微信的Tab
                }
            });

            mTabMe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TopTv.setText("我的信息");
                    selectTab(3);//当点击的是微信的Tab就选中微信的Tab
                }
            });

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
        }

        //收到事件之后的处理方法 收到对应的事件了 来处理  参数要是订阅事件的类型
        @Subscribe(threadMode = ThreadMode.MAIN)
        public void onMessageEvent(MessageEvent event) {
            if (event.getMessage().equals("needpeople")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainAllTabActivity.this, "收到任务", Toast.LENGTH_SHORT).show();

                        new XPopup.Builder(MainAllTabActivity.this).asConfirm("有新的任务", "是否接受任务",
                                new OnConfirmListener() {
                                    @Override
                                    public void onConfirm() {
                                        Toast.makeText(MainAllTabActivity.this, "接受任务成功", Toast.LENGTH_SHORT).show();
                                    }
                                }).show();
                    }
                });

            } else if (event.getMessage().equals("locktask")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainAllTabActivity.this, "锁定消息", Toast.LENGTH_SHORT).show();
                    }
                });
            } else if (event.getMessage().equals("checktask")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainAllTabActivity.this, "审核任务", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void getMissions(Intent intent){

            MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");
            JSONObject jsonObject = new JSONObject();
            JSONObject tmpJSON = new JSONObject();
            tmpJSON.put("current",1);
            tmpJSON.put("size",20);
            jsonObject.put("page",tmpJSON);
            JSONObject tmpJSON2 = new JSONObject();
            jsonObject.put("query",tmpJSON2);

            RequestBody requestBody = RequestBody.create(JSON_TYPE,jsonObject.toString());

            OkHttp3.sendOkHttpPostWithTokensRequest(Address.MISSIONLIST,requestBody, new String[]{intent.getStringExtra("token"), intent.getStringExtra("cookie")}, new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {

                    Toast.makeText(MainAllTabActivity.this,"网络连接失败",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String strBody = response.body().string();
                    JSONObject jsonObject = JSONObject.parseObject(strBody);
                    jsonObject = jsonObject.getJSONObject("data");
                    JSONArray ja = jsonObject.getJSONArray("items");
                    for(int i = 0; i < ja.size() ; i++){
                        Task task = new Task();
                    jsonObject = (JSONObject) ja.get(i);

                    task.setId(jsonObject.getString("id"));
                    task.setName(jsonObject.getString("name"));
                    task.setCode(jsonObject.getString("code"));
                    task.setStatus(jsonObject.getInteger("status"));
                    task.setMemberNum(jsonObject.getInteger("memberNum"));
                    task.setLevel(jsonObject.getString("level"));
                    task.setCurrNum(jsonObject.getInteger("currNum"));
                    task.setCreateTime(jsonObject.getDate("createTime"));
                    task.setElderlyId(jsonObject.getString("elderlyId"));
                    task.setElderlyName(jsonObject.getString("elderlyName"));
                    task.setElderlyGender(jsonObject.getInteger("elderlyGender"));
                    task.setElderlyAge(jsonObject.getInteger("elderlyAge"));
                    task.setElderlyHeight(jsonObject.getDouble("elderlyHeight"));
                    task.setElderlyLostTime(jsonObject.getDate("elderlyLostTime"));
                    task.setElderlyProvince(jsonObject.getString("elderlyProvince"));

                    task.setElderlyProvince(jsonObject.getString("elderlyCity"));
                    task.setElderlyProvince(jsonObject.getString("elderlyArea"));
                    task.setElderlyProvince(jsonObject.getString("elderlyLostAddress"));
                    task.setElderlyLostLng(jsonObject.getDouble("elderlyLostLng"));
                    task.setElderlyLostLat(jsonObject.getDouble("elderlyLostLat"));
                    task.setElderlyMentalMedicalHistory(jsonObject.getBoolean("elderlyMentalMedicalHistory"));
                    task.setElderlyLook(jsonObject.getString("elderlyLook"));
                    task.setElderlyJob(jsonObject.getString("elderlyJob"));
                    task.setElderlyIdCard(jsonObject.getString("elderlyIdCard"));
                    task.setElderlyDescription(jsonObject.getString("elderlyDescription"));
                    task.setCreateTime(jsonObject.getDate("elderlyCreateTime"));
                    task.setElderlyUpdateTime(jsonObject.getDate("elderlyUpdateTime"));
                    task.setElderlyRemark(jsonObject.getString("elderlyRemark"));
                    task.setLostReasonName(jsonObject.getString("lostReasonId"));
                    task.setLostReasonName(jsonObject.getString("lostReasonName"));

                    JSONArray jsonArray = jsonObject.getJSONArray("members");

                    List<User> list = new LinkedList<>();
                    for(Object jsonObject1 : jsonArray){
                        JSONObject jo = (JSONObject)jsonObject1; //TODO 这里没有添加个人信息
                        User user = new User();
                        list.add(user);
                    }
                    task.setMembers(list);

                    taskList.add(task);

                }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((HomeFragment)mFragmentHome).setNowListTasks(taskList);
                        }
                    });

                }
            });
        }




        public Task getandSetMissions(Intent intent){

            OkHttp3.sendOkHttpGetWithTokensRequest(Address.NOWMISSION, new String[]{intent.getStringExtra("token"),intent.getStringExtra("cookie")}, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Toast.makeText(MainAllTabActivity.this,"网络连接错误",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String strBody = response.body().string();
                    JSONObject jsonObject = JSONObject.parseObject(strBody);
                    jsonObject = jsonObject.getJSONObject("data");
                    //final类型给
                    final JSONObject temp = (JSONObject) jsonObject.clone();
                    task.setId(jsonObject.getString("id"));
                    task.setName(jsonObject.getString("name"));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((HomeFragment)mFragmentHome).setNowTaskTitle(temp.getString("name")); //页面初始化好之后就开始设置
                        }
                    });
                    task.setCode(jsonObject.getString("code"));
                    task.setStatus(jsonObject.getInteger("status"));
                    task.setMemberNum(jsonObject.getInteger("memberNum"));
                    task.setLevel(jsonObject.getString("level"));
                    task.setCurrNum(jsonObject.getInteger("currNum"));
                    task.setCreateTime(jsonObject.getDate("createTime"));
                    task.setElderlyId(jsonObject.getString("elderlyId"));
                    task.setElderlyName(jsonObject.getString("elderlyName"));
                    task.setElderlyGender(jsonObject.getInteger("elderlyGender"));
                    task.setElderlyAge(jsonObject.getInteger("elderlyAge"));
                    task.setElderlyHeight(jsonObject.getDouble("elderlyHeight"));
                    task.setElderlyLostTime(jsonObject.getDate("elderlyLostTime"));
                    task.setElderlyProvince(jsonObject.getString("elderlyProvince"));

                    task.setElderlyProvince(jsonObject.getString("elderlyCity"));
                    task.setElderlyProvince(jsonObject.getString("elderlyArea"));
                    task.setElderlyProvince(jsonObject.getString("elderlyLostAddress"));
                    task.setElderlyLostLng(jsonObject.getDouble("elderlyLostLng"));
                    task.setElderlyLostLat(jsonObject.getDouble("elderlyLostLat"));
                    task.setElderlyMentalMedicalHistory(jsonObject.getBoolean("elderlyMentalMedicalHistory"));
                    task.setElderlyLook(jsonObject.getString("elderlyLook"));
                    task.setElderlyJob(jsonObject.getString("elderlyJob"));
                    task.setElderlyIdCard(jsonObject.getString("elderlyIdCard"));
                    task.setElderlyDescription(jsonObject.getString("elderlyDescription"));
                    task.setCreateTime(jsonObject.getDate("elderlyCreateTime"));
                    task.setElderlyUpdateTime(jsonObject.getDate("elderlyUpdateTime"));
                    task.setElderlyRemark(jsonObject.getString("elderlyRemark"));
                    task.setLostReasonName(jsonObject.getString("lostReasonId"));
                    task.setLostReasonName(jsonObject.getString("lostReasonName"));
                    JSONArray ja = jsonObject.getJSONArray("members");

                    for(int i = 0; i < ja.size(); i++ ){
                        JSONObject jo = (JSONObject) ja.get(i);
                        User user = new User();
                        user.setId(jo.getString("id"));
                        user.setName(jo.getString("name"));
                        if(jo.getString("gender").equals("FEMALE")){
                            user.setGender(0);
                        }
                        else
                            user.setGender(1);
                        user.setPhoneNumber(jo.getString("phoneNumber"));
                        user.setTransportationType(jo.getString("transportationType"));
                        user.setCanGo(jo.getBoolean("canGo"));
                        user.setGroupId(jo.getString("groupId"));
                        user.setUserId(jo.getString("userId"));
                        user.setName(jo.getString("username"));
                        nowMissionUsers.add(user);

                    }
                    task.setMembers(nowMissionUsers);

                }
            });

            return task;

        }


        public static String[] getHeaders(){
            if(headers[0] != null){
                return headers;
            }
            return null;
        }


        public void getPostVolInfo(Intent intent){ //通过非空intent拿信息
            String token = "";
            String cookie = "";
            String userName = "";
            token = intent.getStringExtra("token");
            cookie = intent.getStringExtra("cookie");
            userName = intent.getStringExtra("user");

            headers[0] = token;
            headers[1] = cookie;


            OkHttp3.sendOkHttpGetWithTokensRequest(Address.USERINFOADDRESS, new String[]{token, cookie}, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"网络连接失败,请检查网络状态",Toast.LENGTH_LONG).show();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    setLeftInfo(response.body().string());

                    //TODO 拿队伍和老人信息

                }
            });

        }

        private void setLeftInfo(String strResponse){

            JSONObject jsonObject = JSONObject.parseObject(strResponse);
            JSONObject userJSON = jsonObject.getJSONObject("data");
            User user = new User();
            user.setAge(userJSON.getInteger("age"));
            user.setCanGo(userJSON.getBoolean("canGo"));
            user.setName(userJSON.getString("name"));
            user.setPhoneNumber(userJSON.getString("phoneNumber"));
            user.setTransportationType(userJSON.getString("transportationType"));
            if(userJSON.getString("gender").equals("MALE")){
                user.setGender(1);
            }else {
                user.setGender(0);
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    leftFragment.initWidget(MainAllTabActivity.this,user);
                }
            });
        }

        private void initViews() {
            //初始化四个Tab的布局文件
            mTabTeams = findViewById(R.id.id_tab_teams_layout);
            mTabHome = findViewById(R.id.id_tab_home_layout);
            mTabMessage =findViewById(R.id.id_tab_message_layout);
            mTabMe = findViewById(R.id.id_tab_me_layout);

            //初始化四个ImageButton
            mTeams = findViewById(R.id.id_tab_teams);
            mHome = findViewById(R.id.id_tab_home);
            mMessage = findViewById(R.id.id_tab_message);
            mMe = findViewById(R.id.id_tab_me);

            TopTv = findViewById(R.id.top_tv);

            //左拉框
            personalInformationLayout = findViewById(R.id.drawerLayout_all_tab);


        }


    //记录用户首次点击返回键的时间
    private long firstTime = 0;


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {

                Toast.makeText(MainAllTabActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            } else {
                System.exit(0);
            }
        }

        return super.onKeyUp(keyCode, event);
    }



        //进行选中Tab的处理
        private void selectTab(int i) {
            //获取FragmentManager对象
            FragmentManager manager = getSupportFragmentManager();
            //获取FragmentTransaction对象
            FragmentTransaction transaction = manager.beginTransaction();
            //先隐藏所有的Fragment
            hideFragments(transaction);
            switch (i) {
                //当选中点击的是微信的Tab时
                case 0:
                    //设置微信的ImageButton为绿色
                    resetImgs();
                    mTeams.setImageDrawable(getResources().getDrawable(R.drawable.comui_tab_person_selected));
                    //如果微信对应的Fragment没有实例化，则进行实例化，并显示出来
                    if (mFragmentTeams == null) {
                        mFragmentTeams = new TeamsFragment();
                        transaction.add(R.id.id_content, mFragmentTeams);
                    } else {
                        //如果微信对应的Fragment已经实例化，则直接显示出来
                        transaction.show(mFragmentTeams);
                    }
                    break;
                case 1:
                    resetImgs();
                    mHome.setImageDrawable(getResources().getDrawable(R.drawable.comui_tab_home_selected));
                    if (mFragmentHome == null) {
                        mFragmentHome = new HomeFragment();
                        transaction.add(R.id.id_content, mFragmentHome);
                    } else {
                        transaction.show(mFragmentHome);
                    }
                    break;
                case 2:
                    resetImgs();
                    mMessage.setImageDrawable(getResources().getDrawable(R.drawable.comui_tab_message_selected));
                    if (mFragmentMessage == null) {
                        mFragmentMessage = new MessageFragment();
                        transaction.add(R.id.id_content, mFragmentMessage);
                    } else {
                        transaction.show(mFragmentMessage);
                    }
                    break;
                case 3:
                    resetImgs();
                    mMe.setImageDrawable(getResources().getDrawable(R.drawable.my_img_selected));
                    if (mFragmentMe == null) {
                        mFragmentMe = new MeFragment();
                        transaction.add(R.id.id_content, mFragmentMe);
                    } else {
                        transaction.show(mFragmentMe);
                    }
                    break;
                default:
                    break;
            }
            //不要忘记提交事务
            transaction.commit();
        }

        //将四个的Fragment隐藏
        private void hideFragments(FragmentTransaction transaction) {
            if (mFragmentTeams != null) {
                transaction.hide(mFragmentTeams);
            }
            if (mFragmentHome != null) {
                transaction.hide(mFragmentHome);
            }
            if (mFragmentMessage != null) {
                transaction.hide(mFragmentMessage);
            }
            if(mFragmentMe != null ){
                transaction.hide(mFragmentMe);
            }

        }

        //将四个ImageButton置为灰色
        private void resetImgs() {
            mTeams.setImageDrawable(getResources().getDrawable(R.drawable.comui_tab_person));
            mHome.setImageDrawable(getResources().getDrawable(R.drawable.comui_tab_home));
            mMessage.setImageDrawable(getResources().getDrawable(R.drawable.comui_tab_message));
            mMe.setImageDrawable(getResources().getDrawable(R.drawable.my_img));
        }

}
