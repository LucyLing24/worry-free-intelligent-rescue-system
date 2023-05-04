package com.estar.track.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baidu.track.R;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.estar.track.PopViews.TaskAllInfoPopup;
import com.estar.track.activity.TracingActivity;
import com.estar.track.adapter.WaitRecvTasksAdapter;
import com.estar.track.entity.Task;
import com.estar.track.entity.User;
import com.estar.track.utils.Address;
import com.estar.track.utils.OkHttp3;
import com.lxj.xpopup.XPopup;
import com.stx.xhb.xbanner.XBanner;
import com.youth.banner.Banner;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HomeFragment extends Fragment implements OnItemClickListener {


    //去地图的Button
    private Button toMapBtn,allinfoBtn;
    ListView tasksLV;
    View view;
    List<Map<String, Object>> list;
    TextView nowMissionTv;

    Banner banner;

    Intent Loginintent;//登录的intent

    private XBanner xBanner;


    /**任务列表**/
    //当前任务
    public Task nowTask = new Task();
    //Tasks列表
    public List<Task> tasks = new LinkedList<>();



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_tab_home, container, false);
        init();

        setBanner();




        toMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), TracingActivity.class);
                    if(Loginintent != null){
                        intent.putExtra("token",Loginintent.getStringExtra("token"));
                        intent.putExtra("cookie",Loginintent.getStringExtra("cookie"));
                        intent.putExtra("user",Loginintent.getStringExtra("user"));
                        intent.putExtra("cango",LeftPersonalInformationFragment.getStatus());

                        startActivity(intent);
                    }else {
                        Toast.makeText(getContext(),"登陆失败",Toast.LENGTH_SHORT).show();
                    }
            }
        });
        allinfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new XPopup.Builder(getContext())
                        .asCustom(new TaskAllInfoPopup(getContext()))
                        .show();
            }
        });

        return view;
    }


    private void setBanner(){
        /**图片两张**/
        List<Bitmap> images = new ArrayList<>();
        images.add(getBmp(R.drawable.loveoldman));
        images.add(getBmp(R.drawable.happiness));

        /**设置banner**/
        //图片集合,模拟一下数据
        final List<String> imgesUrl = new ArrayList<>();
        imgesUrl.add("https://ae01.alicdn.com/kf/Ud6b55f2215bf44fda6b6e888611117c0p.jpg");
        imgesUrl.add("https://ae01.alicdn.com/kf/U27d085f027624558b0c35759aa8ad85dh.jpg");
        imgesUrl.add("http://imageprocess.yitos.net/images/public/20160910/77991473496077677.jpg");
        imgesUrl.add("http://imageprocess.yitos.net/images/public/20160906/1291473163104906.jpg");
        xBanner.setData(images,null);
        xBanner.loadImage(new XBanner.XBannerAdapter() {
            @Override
            public void loadBanner(XBanner banner, Object model, View view, int position) {
                //glide请求网络图片
                Glide.with(HomeFragment.this).load(imgesUrl.get(position)).into((ImageView) view);
            }
        });
        xBanner.setPageChangeDuration(3000);
        xBanner.setAutoPlayAble(true);
        xBanner.setAutoPalyTime(3000);

    }





    //设置adapter里面的数据
    private void setTaksData(List<Task> tasks){
        list = getData(tasks); //拆出数据
        WaitRecvTasksAdapter adapter = new WaitRecvTasksAdapter(list,getActivity());
        tasksLV.setAdapter(adapter);
    }

    @Override
    public void onResume() {
//        OkHttp3.sendOkHttpPostTokenRequest();  这里发送http
        super.onResume();
    }

    private Bitmap getBmp(int id){
        Resources re = this.getContext().getResources();
        InputStream is = re.openRawResource(id);
        BitmapDrawable bmpDr = new BitmapDrawable(is);
        Bitmap bmp = bmpDr.getBitmap();
        return bmp;
    }

    private void init(){
        tasksLV = view.findViewById(R.id.wait_rect_tasks_lv);
        nowMissionTv = view.findViewById(R.id.now_mission_title_tv);
        toMapBtn = view.findViewById(R.id.tomap_btn);
        allinfoBtn = view.findViewById(R.id.action_all_info_btn);
        xBanner = view.findViewById(R.id.mybanner);
    }

    @Deprecated //测试数据
    public List<Map<String, Object>> getData(List<Task> tasks) {

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        for (int i = 0;  i < tasks.size();i++) {
            Task task = tasks.get(i);
            List<User> list1 = task.getMembers();
            Map<String, Object> map = new HashMap<String, Object>();

            map.put("taskname",task.getName());
            map.put("maxNum",task.getMemberNum());
            map.put("level",task.getLevel());
            map.put("nowUsersNum",list1.size());

            list.add(map);
        }
        return list;
    }

    public void setLoginIntent(Intent intent){
        this.Loginintent = intent;
        //设置好intent之后立刻开始获得list并放到里面去
//        getandSetMissions(Loginintent);

    }

    @Deprecated //此方法功能放到MainAllTabActivity里面实现 然后set传进来
    public void getandSetMissions(Intent intent){

        OkHttp3.sendOkHttpGetWithTokensRequest(Address.NOWMISSION, new String[]{intent.getStringExtra("token"),intent.getStringExtra("cookie")}, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(getContext(),"网络连接错误",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String strBody = response.body().string();
                JSONObject jsonObject = JSONObject.parseObject(strBody);
                jsonObject = jsonObject.getJSONObject("data");
                Task task = new Task();

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

                JSONArray ja = jsonObject.getJSONArray("members");

                List<User> users = new ArrayList<>();
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
                    users.add(user);

                }
                task.setMembers(users);

                for(int i = 0 ; i < users.size() ; i++){
                    Map<String, Object> map = new HashMap<>();
                }
//                list = getData(); //List<Map<String, Object>>
//
//                WaitRecvTasksAdapter adapter = new WaitRecvTasksAdapter(list,getActivity());
//
//                tasksLV.setAdapter(adapter);

            }
        });
    }



    /**对外接口**/
    //TODO 接口有问题

    public void setNowListTasks(List<Task> taskList){  //设置当前task 并且设置当前正在进行的任务的标题
        tasks = taskList;
        setTaksData(taskList); //设置任务列表数据
    }

    //设置当前任务的标题
    public void setNowTaskTitle(String title){
        nowMissionTv = this.view.findViewById(R.id.now_mission_title_tv);
        nowMissionTv.setText(title);
    }

    //Banner的操作
    @Override
    public void onItemClick(int position) {
        //无
    }
}
