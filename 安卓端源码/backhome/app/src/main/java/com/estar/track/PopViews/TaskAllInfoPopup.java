package com.estar.track.PopViews;

import android.content.Context;

import androidx.annotation.NonNull;

import com.baidu.track.R;
import com.lxj.xpopup.core.CenterPopupView;

public class TaskAllInfoPopup extends CenterPopupView {

    //组件
    public TaskAllInfoPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.task_all_info;
    }


    //初始化页面 findView
    @Override
    protected void onCreate() {
        super.onCreate();


    }
}
