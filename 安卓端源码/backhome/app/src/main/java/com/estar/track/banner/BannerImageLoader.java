package com.estar.track.banner;

import android.content.Context;
import android.widget.ImageView;

import com.baidu.track.R;

import java.util.ArrayList;
import java.util.List;


public class BannerImageLoader  {

    public class DataBean {
        public int imageRes;
        public String imageUrl;
        public String title;
        public int viewType;

        public DataBean(int imageRes, String title, int viewType) {
            this.imageRes = imageRes;
            this.title = title;
            this.viewType = viewType;
        }

        public DataBean(String imageUrl, String title, int viewType) {
            this.imageUrl = imageUrl;
            this.title = title;
            this.viewType = viewType;
        }



        public List<DataBean> getTestData3() {
            List<DataBean> list = new ArrayList<>();
            list.add(new DataBean(R.drawable.happiness,"1",1));
            list.add(new DataBean(R.drawable.loveoldman,"1",1));
            return list;
        }


    }
}
