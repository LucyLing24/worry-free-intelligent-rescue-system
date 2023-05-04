package com.estar.track.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.track.R;
import com.estar.track.PopViews.ElederInfoPop;
import com.estar.track.entity.Task;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WaitRecvTasksAdapter extends BaseAdapter {

        private List<Map<String,Object>> mData;

        private Context mContext;

        public WaitRecvTasksAdapter(List<Map<String,Object>> mData, Context mContext){

            this.mData=mData;

            this.mContext=mContext;

        }
        @Override
        public int getCount() {

            return mData.size();

        }
        @Override
        public Object getItem(int position) {
             return mData.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;

        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView ==null){
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_task_wait_recv,parent,false);
            }

            HashMap<String,Object> task = (HashMap<String, Object>) mData.get(position);

            //初始化item的各个组件
            //老人照片
            ImageView elderlyPhoto =(ImageView)convertView.findViewById(R.id.elerdly_hoto);
            RatingBar taskMergency = convertView.findViewById(R.id.tasks_imortance);
            TextView lostAreaTv =convertView.findViewById(R.id.lost_area_tv);
            TextView nowCraws =convertView.findViewById(R.id.now_numbers_craws);
            TextView TaskName = convertView.findViewById(R.id.elerdly_name);

            Button lookAllInfo = convertView.findViewById(R.id.more_elderly_info);
            Button recvTask = convertView.findViewById(R.id.rcv_task_btn);

            TaskName.setText(String.valueOf(mData.get(position).get("taskname")));
            Integer maxMum = (Integer) mData.get(position).get("maxNum");
            String level = (String) mData.get(position).get("level");
            Integer nowUsersNum = (Integer) mData.get(position).get("nowUsersNum");

            String nownumStatus = String.valueOf(nowUsersNum) + "/" + String.valueOf(maxMum);
            nowCraws.setText(nownumStatus);
            if(level.equals("TOP")){
                taskMergency.setRating((float) 5.0);
            }
            //查看任务详细信息
            lookAllInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new XPopup.Builder(mContext)
                            .asCustom(new ElederInfoPop(mContext))
                            .show();
                }
            });
            //接受任务
            recvTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new XPopup.Builder(mContext).asConfirm("接受任务", "确认接受任务吗",
                            new OnConfirmListener() {
                                @Override
                                public void onConfirm() {
                                    Toast.makeText(mContext,"接受任务成功,即将为您分配队友",Toast.LENGTH_LONG).show();
                                    //TODO list减少
                                }
                            }).show();
                }
            });

            return convertView;

        }

        }
