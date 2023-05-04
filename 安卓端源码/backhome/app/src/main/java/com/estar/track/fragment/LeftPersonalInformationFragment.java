package com.estar.track.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
//import androidx.fragment.app.Fragment;
import android.app.Fragment;

import com.baidu.mapapi.model.LatLng;
import com.baidu.track.R;
import com.estar.track.activity.Activity_change_user_info;
import com.estar.track.activity.LoginActivityHome;
import com.estar.track.activity.MainAllTabActivity;
import com.estar.track.activity.TracingActivity;
import com.estar.track.entity.User;
import com.estar.track.service.PointService;
import com.estar.track.utils.Address;
import com.estar.track.utils.OkHttp3;
import com.suke.widget.SwitchButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LeftPersonalInformationFragment extends Fragment {
    private ImageView headImageV;
    private TextView nickNameEdx;
    private TextView nameEdx,sexEdx,areaEdx,timeLengthEdx,phoneNumTv,tansportTv;
    private static TextView statusEdx;
    private static SwitchButton changeStatusSwitchBtn;

    private LinearLayout statusLinear;

    private Button usrLogOutBtn;
    private Button changeInfo;

    private Boolean isCanGo = false;

    Intent pointIntent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_left_personalinformation,container,false);
        init(view);

        changeStatusSwitchBtn.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if(isChecked == true){
                        getToast(true);
//                        statusEdx.setText("可出任务");
                        pointIntent = new Intent(getActivity(),PointService.class);
                        getActivity().startService(pointIntent);

                        //TODO 开启Service
                }else {
                        getToast(false);
//                        statusEdx.setText("停出任务");
                        getActivity().stopService(pointIntent);
                        //TODO 更改志愿者状态
                }
            }
        });
        usrLogOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivityHome.class);
                startActivity(intent);
            }
        });

        changeInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(getActivity(), Activity_change_user_info.class);
                startActivity(intent2);
            }
        });

        return view;
    }




//
//    public void setNickName(String nickName){
//        nickNameEdx.setText(nickName);
//    }
//
//    public void setGender(String gender){
//        sexEdx.setText(gender);
//    }
//    public void setTransportationType(String transport){
//        tansportTv.setText(transport);
//    }
////    public void setAge(String age){
////        sexEdx.setText(age);
////    }
//    public void setPhoneNumber(String phoneNumber){
//        phoneNumTv.setText(phoneNumber);
//    }


    private void getToast(Boolean isOk){
        if(isOk){
            Toast toast = Toast.makeText(getActivity(),"状态已被修改为\"可出任务\"",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP,0,0);
            toast.show();
        }else {
            Toast toast = Toast.makeText(getActivity(),"状态已被修改为\"停出任务\"",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP,0,0);
            toast.show();
        }
    }

    private void init(View view){
        headImageV = view.findViewById(R.id.head_image_imageview);
        nickNameEdx = view.findViewById(R.id.nickname_textview);
//        statusEdx = view.findViewById(R.id.personal_status_tv);
        nameEdx = view.findViewById(R.id.name_edx);
        sexEdx = view.findViewById(R.id.sex_edx);
        areaEdx = view.findViewById(R.id.area_edx);
        timeLengthEdx = view.findViewById(R.id.time_edx);
        changeStatusSwitchBtn = view.findViewById(R.id.status_change_switch);
        phoneNumTv = view.findViewById(R.id.phonenum_tv);
        tansportTv = view.findViewById(R.id.transportation_tv);

        usrLogOutBtn = (Button) view.findViewById(R.id.usr_log_out_btn);
        changeInfo = (Button) view.findViewById(R.id.change_info);

        statusLinear = view.findViewById(R.id.change_status_linear);
    }

//
//    public static void setSwitch(String status){
//        if(status.equals("停出任务")){
//            changeStatusSwitchBtn.setChecked(false);
//        }else {
//            changeStatusSwitchBtn.setChecked(true);
//        }
//    }

    public void setStatusAndSwitch(Activity activity ,String status){
//        statusEdx = activity.findViewById(R.id.personal_status_tv);
        changeStatusSwitchBtn = activity.findViewById(R.id.status_change_switch);
        if(status.equals("停出任务")){
//            statusEdx.setText("停出任务");
            changeStatusSwitchBtn.setChecked(false);
        }else {
//            statusEdx.setText("可出任务");
            changeStatusSwitchBtn.setChecked(true);
        }
    }

    public void initWidget(Activity activity, User user){
        nickNameEdx = activity.findViewById(R.id.nickname_textview);
//        statusEdx = activity.findViewById(R.id.personal_status_tv);
        nameEdx = activity.findViewById(R.id.name_edx);
        sexEdx = activity.findViewById(R.id.sex_edx);
        areaEdx = activity.findViewById(R.id.area_edx);
        timeLengthEdx = activity.findViewById(R.id.time_edx);
        phoneNumTv = activity.findViewById(R.id.phonenum_tv);
        tansportTv = activity.findViewById(R.id.transportation_tv);
        statusLinear = activity.findViewById(R.id.change_status_linear);

        nickNameEdx.setText(user.getName());
        nameEdx.setText(user.getName());
        if(user.getGender() == 1){
            sexEdx.setText("男");
        }else {
            sexEdx.setText("女");
        }
//        areaEdx.setText(); 暂时没有
        phoneNumTv.setText(user.getPhoneNumber());
        if(user.getTransportationType().equals("FOOT")) //步行
        {
            tansportTv.setText("步行");
        }else if(user.getTransportationType() == "NONE_MOTOR_VEHICLE"){
            tansportTv.setText("非机动车");
        }else if (user.getTransportationType() == "MOTOR_VEHICLE"){
            tansportTv.setText("机动车");
        }
    }

    public static String getStatus(){
        if(changeStatusSwitchBtn.isChecked() == true){
            return "可出任务";
        }
        return "停出任务";
    }

}
