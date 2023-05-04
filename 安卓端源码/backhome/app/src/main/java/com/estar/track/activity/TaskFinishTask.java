package com.estar.track.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class TaskFinishTask  extends AppCompatActivity {
    private ImageView foundEldlyPhoto;
    private Button takeElederlyPhotoBtn;
    private Button upPhotoBtn;

    /**相机参数**/
    private static final int CAPTURE_MEDIA = 368;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_finishtask);
        init();

        EventBus.getDefault().register(this);

        upPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TaskFinishTask.this,"已提交",Toast.LENGTH_SHORT).show();
            }
        });

        takeElederlyPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //系统常量， 启动相机的关键
                startActivityForResult(openCameraIntent, 1); // 参数常量为自定义的request code, 在取返回结果时有用

            }

        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){

            if ( resultCode == RESULT_OK) {
                String sdStatus = Environment.getExternalStorageState();

                if(!sdStatus.equals(Environment.MEDIA_MOUNTED)){
                    System.out.println(" ------------- sd card is not avaiable ---------------");
                    return;
                }


                String name = "photo.jpg";

                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");

                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/");
                file.mkdirs(); //创建文件夹

                String fileName = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+name;

                FileOutputStream b =null;

                try {
                    b=new FileOutputStream(fileName);
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,b);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        b.flush();
                        b.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                foundEldlyPhoto.setImageBitmap(bitmap);

                Toast.makeText(this, "Media captured.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //收到事件之后的处理方法 收到对应的事件了 来处理  参数要是订阅事件的类型
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if (event.getMessage().equals("needpeople")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(TaskFinishTask.this, "收到任务", Toast.LENGTH_SHORT).show();

                    new XPopup.Builder(TaskFinishTask.this).asConfirm("有新的任务", "是否接受任务",
                            new OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    Toast.makeText(TaskFinishTask.this, "接受任务成功", Toast.LENGTH_SHORT).show();
                                }
                            }).show();
                }
            });

        } else if (event.getMessage().equals("locktask")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(TaskFinishTask.this, "锁定消息", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (event.getMessage().equals("checktask")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(TaskFinishTask.this, "审核任务", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void init(){
        foundEldlyPhoto = (ImageView) findViewById(R.id.found_eldly_photo);
        takeElederlyPhotoBtn = (Button) findViewById(R.id.take_elederly_photo_btn);
        upPhotoBtn = (Button) findViewById(R.id.up_photo_btn);
    }
}
