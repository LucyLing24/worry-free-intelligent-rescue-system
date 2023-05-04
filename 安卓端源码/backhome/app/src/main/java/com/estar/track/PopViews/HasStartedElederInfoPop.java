package com.estar.track.PopViews;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.baidu.track.R;
import com.lxj.xpopup.core.CenterPopupView;

public class HasStartedElederInfoPop extends CenterPopupView {
    private TextView hasstartedElderlyNameTv;
    private TextView hasstartedElderlyAgeTv;
    private TextView hasstartedElderlySexTv;
    private TextView hasstartedElderlyLostAreaTv;
    private TextView hasstartedElderlyLostTimeTv;
    private TextView hasstartedElderlyCharacterAllTv;
    private TextView hasstartedElderlyEmergencyLevelTv;
    private TextView hasstartedClothesCharaTv;
    private TextView hasstartedActionCharaTv;
    private TextView hasstartedFaceCharaTv;
    private TextView hasstartedElderlyStatusTv;

    public HasStartedElederInfoPop(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.popup_hasstarted_old;
    }


    @Override
    protected void onCreate() {
        super.onCreate();
        init();
    }

    public void  init(){
        hasstartedElderlyNameTv = (TextView) findViewById(R.id.hasstarted_elderly_name_tv);
        hasstartedElderlyAgeTv = (TextView) findViewById(R.id.hasstarted_elderly_age_tv);
        hasstartedElderlySexTv = (TextView) findViewById(R.id.hasstarted_elderly_sex_tv);
        hasstartedElderlyLostAreaTv = (TextView) findViewById(R.id.hasstarted_elderly_lost_area_tv);
        hasstartedElderlyLostTimeTv = (TextView) findViewById(R.id.hasstarted_elderly_lost_time_tv);
        hasstartedElderlyCharacterAllTv = (TextView) findViewById(R.id.hasstarted_elderly_character_all_tv);
        hasstartedElderlyEmergencyLevelTv = (TextView) findViewById(R.id.hasstarted_elderly_emergency_level_tv);
        hasstartedClothesCharaTv = (TextView) findViewById(R.id.hasstarted_clothes_chara_tv);
        hasstartedActionCharaTv = (TextView) findViewById(R.id.hasstarted_action_chara_tv);
        hasstartedFaceCharaTv = (TextView) findViewById(R.id.hasstarted_face_chara_tv);
        hasstartedElderlyStatusTv = (TextView) findViewById(R.id.hasstarted_elderly_status_tv);

    }

}
