package com.estar.track.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.baidu.track.R;
import com.estar.track.event.MessageEvent;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class Activity_change_user_info extends AppCompatActivity {

    private Button finishChangeBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_info);
        finishChangeBtn = (Button) findViewById(R.id.finish_change_btn);
        EventBus.getDefault().register(this);
        finishChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Activity_change_user_info.this,"修改成功",Toast.LENGTH_SHORT).show();
                finish();
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
                    Toast.makeText(Activity_change_user_info.this, "收到任务", Toast.LENGTH_SHORT).show();

                    new XPopup.Builder(Activity_change_user_info.this).asConfirm("有新的任务", "是否接受任务",
                            new OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    Toast.makeText(Activity_change_user_info.this, "接受任务成功", Toast.LENGTH_SHORT).show();
                                }
                            }).show();
                }
            });

        } else if (event.getMessage().equals("locktask")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(Activity_change_user_info.this, "锁定消息", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (event.getMessage().equals("checktask")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(Activity_change_user_info.this, "审核任务", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
