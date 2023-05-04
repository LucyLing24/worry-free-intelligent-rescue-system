package com.estar.track.PopViews;

import android.content.Context;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.baidu.track.R;
import com.lxj.xpopup.core.CenterPopupView;

public class MessageInfoPopup extends CenterPopupView {

    //组件



    public MessageInfoPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.message_popup;
    }


    //初始化页面 findView
    @Override
    protected void onCreate() {
        super.onCreate();


    }
}
