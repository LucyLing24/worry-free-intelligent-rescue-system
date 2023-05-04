package com.estar.track.fragment;

import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.baidu.track.R;
import com.estar.track.entity.User;
import com.suke.widget.SwitchButton;

public class MeFragment extends Fragment {
    private LinearLayout personalLayout;
    private TextView meNicknameTextview;
    private TextView mePersonalStatusTv;
    private ImageView meHeadImageImageview;
    private TextView meNameEdx;
    private TextView meSexEdx;
    private TextView meAreaEdx;
    private TextView mePhonenumTv;
    private TextView meTimeEdx;
    private TextView meTransportationTv;
    private LinearLayout changeStatusLinear;
    private SwitchButton meStatusChangeSwitch;
    private Button meUsrLogOutBtn;
    private Button meChangeInfo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_me,container,false);
        init(view);

        return view;


    }

    private void init(View view){

        personalLayout = (LinearLayout) view.findViewById(R.id.personal_layout);
        meNicknameTextview = (TextView) view.findViewById(R.id.me_nickname_textview);
        mePersonalStatusTv = (TextView) view.findViewById(R.id.me_personal_status_tv);
        meHeadImageImageview = (ImageView) view.findViewById(R.id.me_head_image_imageview);
        meNameEdx = (TextView) view.findViewById(R.id.me_name_edx);
        meSexEdx = (TextView) view.findViewById(R.id.me_sex_edx);
        meAreaEdx = (TextView) view.findViewById(R.id.me_area_edx);
        mePhonenumTv = (TextView) view.findViewById(R.id.me_phonenum_tv);
        meTimeEdx = (TextView) view.findViewById(R.id.me_time_edx);
        meTransportationTv = (TextView) view.findViewById(R.id.me_transportation_tv);
        changeStatusLinear = (LinearLayout) view.findViewById(R.id.change_status_linear);
        meStatusChangeSwitch = (SwitchButton) view.findViewById(R.id.me_status_change_switch);
        meUsrLogOutBtn = (Button) view.findViewById(R.id.me_usr_log_out_btn);
        meChangeInfo = (Button) view.findViewById(R.id.me_change_info);
    }












}
