package com.estar.track.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.baidu.track.R;
import com.estar.track.adapter.FriendAdapter;
import com.estar.track.event.MessageEvent;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.interfaces.OnInputConfirmListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivitySendMessage  extends AppCompatActivity {

    ListView send2WhoLV;
    Button send_msg_2_who;
    private EditText foundEldlyPhoto;
    private Button sendMsg2EveryoneBtn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sendmessage_2_friend);

        send2WhoLV = findViewById(R.id.send_msg_popel_list);
        send_msg_2_who = findViewById(R.id.send_btn);
        foundEldlyPhoto = (EditText) findViewById(R.id.found_eldly_photo);
        sendMsg2EveryoneBtn = (Button) findViewById(R.id.send_msg_2_everyone_btn);

        EventBus.getDefault().register(this);

        List<Map<String, Object>> list= getData();
        FriendAdapter friendAdapter = new FriendAdapter(list,ActivitySendMessage.this);
        send2WhoLV.setAdapter(friendAdapter);

        send_msg_2_who.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActivitySendMessage.this,"发送成功",Toast.LENGTH_SHORT).show();
            }
        });

        sendMsg2EveryoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActivitySendMessage.this,"发送给所有人成功",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(ActivitySendMessage.this, "收到任务", Toast.LENGTH_SHORT).show();

                    new XPopup.Builder(ActivitySendMessage.this).asConfirm("有新的任务", "是否接受任务",
                            new OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    Toast.makeText(ActivitySendMessage.this, "接受任务成功", Toast.LENGTH_SHORT).show();
                                }
                            }).show();
                }
            });

        } else if (event.getMessage().equals("locktask")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ActivitySendMessage.this, "锁定消息", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (event.getMessage().equals("checktask")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ActivitySendMessage.this, "审核任务", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", "");
            map.put("friend_name", "Uzi");
            map.put("friend_length", "3千米");
            map.put("交通工具", "骑车");
            list.add(map);
        }
        return list;
    }

}
