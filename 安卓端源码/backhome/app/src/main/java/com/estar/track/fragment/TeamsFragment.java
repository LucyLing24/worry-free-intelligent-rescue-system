package com.estar.track.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.baidu.track.R;
import com.estar.track.adapter.FriendAdapter;
import com.estar.track.entity.Task;
import com.estar.track.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TeamsFragment extends Fragment {

    ListView crewsLV;
    View view;
    Context context;

    static Task nowTask = new Task();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_tab_team, container, false);
        init();

        context = getContext();

        setnowTask2getMembers(nowTask);
        return view;
    }

    private void setAdapter(){

    }

    private void init() {
        crewsLV = view.findViewById(R.id.teams_listView);
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

    /**对外接口**/
    public void setnowTask2getMembers(Task task){

        List<User> list= task.getMembers();
        List<Map<String, Object>> membersData = new LinkedList<>();

        for(int i = 0; i < list.size(); i++){
            User user = list.get(i);
            Map<String,Object> uMap = new HashMap<>();
            uMap.put("name",user.getName());
            uMap.put("age",user.getAge());
            uMap.put("tans",user.getTransportationType());
            uMap.put("phoneN",user.getPhoneNumber());
            uMap.put("gender",user.getGender());
            membersData.add(uMap);
        }

        crewsLV = view.findViewById(R.id.teams_listView);

        FriendAdapter friendAdapter = new FriendAdapter(membersData,view.getContext());

        crewsLV.setAdapter(friendAdapter);
    }

    public void setTask(Task task){
        nowTask =task;
    }




}