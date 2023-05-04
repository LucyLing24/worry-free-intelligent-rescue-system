package com.estar.track.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.baidu.track.R;
import com.estar.track.PopViews.FriendInfoPopup;
import com.lxj.xpopup.XPopup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class FriendAdapter extends BaseAdapter {

        private List<Map<String,Object>> mData;

        private Context mContext;

        public FriendAdapter(List<Map<String,Object>> mData,Context mContext){

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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_one_friend,parent,false);
            }

            //初始化item的各个组件
            final ImageView imageView1=(ImageView)convertView.findViewById(R.id.hotelPicture);

            final TextView originPrice=(TextView)convertView.findViewById(R.id.originPrice);




            final TextView currentPrice=(TextView)convertView.findViewById(R.id.currentPrice);

//            currentPrice.setText(mData.get(position)+"");

            final TextView numName=(TextView) convertView.findViewById(R.id.hotelNameText);


            final TextView hotelLocation=(TextView) convertView.findViewById(R.id.hotelLocationText);

            Button ContactWithTeamMember=(Button)convertView.findViewById(R.id.connect_friend_btn);

            Button LookTeamMsInfo=(Button)convertView.findViewById(R.id.look_friend_info_btn);

            RatingBar ratingBar=(RatingBar)convertView.findViewById(R.id.ratingBar);

            if(mData.get(position).containsKey("name")){
                String s = "";
                if((int)(mData.get(position)).get("gender") == 0){
                    s = s + "性别: 女 ";
                }else {
                    s = s + "性别: 男 ";
                }
                s = s + "交通工具: ";
                if((mData.get(position)).get("tans").equals("FOOT")){
                    s = s + "步行 ";
                }else if ((mData.get(position)).get("tans").equals("MOTOR_VEHICLE")){ //MOTOR_VEHICLE
                    s = s + "机动车 ";
                }else {
                    s = s + "非机动车 ";
                }
                s = s + "年龄:" + (mData.get(position)).get("age").toString();
                numName.setText((String)(mData.get(position).get("name")));
                originPrice.setText(s);
            }else if(mData.get(position).containsKey("friend_name")){

            }



            //和队友联系
            ContactWithTeamMember.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //调转到打电话界面
                    Intent intent =  new Intent(android.content.Intent.ACTION_DIAL, Uri.parse("tel:" + "12345678910"));//跳转到拨号界面，同时传递电话号码
                    mContext.startActivity(intent);
                }
            });

            //查看队友信息
            LookTeamMsInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new XPopup.Builder(mContext)
                            .asCustom(new FriendInfoPopup(mContext))
                            .show();
                }
            });
            return convertView;

        }

        }
