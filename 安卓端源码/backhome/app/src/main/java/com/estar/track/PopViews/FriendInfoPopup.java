package com.estar.track.PopViews;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.baidu.track.R;
import com.lxj.xpopup.core.CenterPopupView;

public class FriendInfoPopup extends CenterPopupView {

    //组件
    TextView f_name_tv,f_sex_tv,f_area_tv,f_phone_tv,f_time_tv,f_transportion_tv;
    TextView f_status_tv;


    public FriendInfoPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.friend_popup;
    }


    //初始化页面 findView
    @Override
    protected void onCreate() {
        super.onCreate();
        f_name_tv = findViewById(R.id.friend_name);
        f_sex_tv = findViewById(R.id.friend_sex);
        f_area_tv = findViewById(R.id.friend_area);
        f_phone_tv = findViewById(R.id.friend_phonenum);
        f_time_tv = findViewById(R.id.friend_time);
        f_transportion_tv = findViewById(R.id.friend_transportation);
        f_status_tv = findViewById(R.id.friend_status);

    }
}
