package com.estar.track.PopViews;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.baidu.track.R;
import com.lxj.xpopup.core.CenterPopupView;

public class ElederInfoPop extends CenterPopupView {


    private TextView elderlyNameTv;
    private TextView elderlyAgeTv;
    private TextView elderlySexTv;
    private TextView elderlyLostAreaTv;
    private TextView elderlyLostTimeTv;
    private TextView elderlyCharacterAllTv;
    private TextView elderlyEmergencyLevelTv;
    private TextView clothesCharaTv;
    private TextView actionCharaTv;
    private TextView faceCharaTv;
    private TextView elderlyStatusTv;

    public ElederInfoPop(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.eldly_popup;
    }


    @Override
    protected void onCreate() {
        super.onCreate();

        elderlyNameTv = (TextView) findViewById(R.id.elderly_name_tv);
        elderlyAgeTv = (TextView) findViewById(R.id.elderly_age_tv);
        elderlySexTv = (TextView) findViewById(R.id.elderly_sex_tv);
        elderlyLostAreaTv = (TextView) findViewById(R.id.elderly_lost_area_tv);
        elderlyLostTimeTv = (TextView) findViewById(R.id.elderly_lost_time_tv);
        elderlyCharacterAllTv = (TextView) findViewById(R.id.elderly_character_all_tv);
        elderlyEmergencyLevelTv = (TextView) findViewById(R.id.elderly_emergency_level_tv);
        clothesCharaTv = (TextView) findViewById(R.id.clothes_chara_tv);
        actionCharaTv = (TextView) findViewById(R.id.action_chara_tv);
        faceCharaTv = (TextView) findViewById(R.id.face_chara_tv);
        elderlyStatusTv = (TextView) findViewById(R.id.elderly_status_tv);
    }
}
