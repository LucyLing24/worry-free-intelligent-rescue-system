package com.estar.track.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.baidu.track.R;
import com.estar.track.adapter.MessageAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageFragment extends Fragment {

    View view;


    ListView messageLv;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab_message, container, false);
        init();
        List<Map<String, Object>> datas = getData();
        MessageAdapter adapter = new MessageAdapter(datas,getActivity());
        messageLv.setAdapter(adapter);

        return view;
    }

    private void init() {
        messageLv = view.findViewById(R.id.message_tv);
    }

    public List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", "崇川区老人李时珍已找到");
        map.put("littlemessage", "2021年3月26日队员李东于崇川区狼山镇附近...");
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("title", "高晖已找到");
        map.put("littlemessage", "2021年3月24日队员张书文在南通通州区附近...");
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("title", "四川成都沈浩已找到");
        map.put("littlemessage", "2020年6月14日队员黄方佑于成都市...");
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("title", "江西省贵溪市姚咏成已找到");
        map.put("littlemessage", "2020年4月16日队员杨云于贵溪市白田乡附近找到...");
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("title", "北京崔耀予已被找到");
        map.put("littlemessage", "2020年1月13日天气正寒，队员吴诗兵...");
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("title", "安徽省天长市吴诗兵已找到");
        map.put("littlemessage", "2019年7月26日队员郭晓楠于天长市冶山镇...");
        list.add(map);
//        for (int i = 0; i < 10; i++) {
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("title", "这是title" + i);
//            map.put("littlemessage", "这是消息缩略" + 1);
//            list.add(map);
//        }
        return list;
    }


}
