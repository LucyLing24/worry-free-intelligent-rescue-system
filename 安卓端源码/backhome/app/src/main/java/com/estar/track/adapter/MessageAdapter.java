package com.estar.track.adapter;

import android.content.Context;
import android.os.Message;
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
import com.estar.track.PopViews.MessageInfoPopup;
import com.lxj.xpopup.XPopup;

import java.util.List;
import java.util.Map;



public class MessageAdapter extends BaseAdapter {

        private List<Map<String,Object>> mData;

        private Context mContext;

        public MessageAdapter(List<Map<String,Object>> mData, Context mContext){

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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_message,parent,false);
            }

            Button lookInfoBtn;
            Button RecvInfoBtn;
            TextView titleTv,infoTv;
            lookInfoBtn = convertView.findViewById(R.id.look_info_btn);
            RecvInfoBtn = convertView.findViewById(R.id.recv_btn);
            titleTv = convertView.findViewById(R.id.message_title_tv);
            infoTv = convertView.findViewById(R.id.message_content_tv);


            //初始化Message内容
            titleTv.setText(mData.get(position).get("title").toString());
            infoTv.setText(mData.get(position).get("littlemessage").toString());

            //查看信息
            lookInfoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new XPopup.Builder(mContext)
                            .asCustom(new MessageInfoPopup(mContext))
                            .show();
                }
            });


            //收到
            RecvInfoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext,"确认收到成功",Toast.LENGTH_SHORT).show();
                }
            });

            return convertView;

        }

        }
