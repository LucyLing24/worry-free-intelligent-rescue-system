package com.estar.track.utils;

public class Address {

//    public static final String IP = "114.213.72.208";
    public static final String IP = "192.168.3.191";  //619
//    public static final String IP = "192.168.43.112";   //热点
    //登录路由
    public static final String LOGINADDRESS = "http://"+IP +":8888/user/login";
    //获得志愿者信息
    public static final String USERINFOADDRESS = "http://"+ IP  +":8888/volunteer/getInfo";
    //WS地址
    public static final String WSIP = "ws://" + IP + ":8888/websocket/";


    /**banner图片**/
    public static final String BannerPhotoHap = "http://" + IP  + ":8888/rphoto/happiness.png";
    public static final String BannerPhotoLove = "http://" + IP  + ":8888/rphoto/loveoldman.png";


    /**
     * 任务模块
     */
    //获取列表
    public static final String MISSIONLIST = "http://" + IP  + ":8888/mission/list";
    //接受任务
    public static final String RECEIVEMISSION = "http://" + IP  + ":8888/mission/";//    "{missionId}/accept"
    //队员提交任务
    public static final String POSTMISSION = "http://" + IP  + ":8888/mission/"; //  {missionId}/commit
    //通过token获得当前正在参与的任务
    public static final String NOWMISSION = "http://" + IP  + ":8888/mission/getMissionByToken"; //  {missionId}/commit


    /**
     * 志愿者
     */
    //获得志愿者信息
    //上边

    //获得志愿者列表
    public static final String VOLUNTEERLIST = "http://" + IP  + ":8888/volunteer/list";
    //更新志愿者信息
    public static final String UPDATEVOLUNTEER = "http://" + IP  + ":8888/volunteer/update"; //PUT
    /**
     * 用户模块
     */
    //修改密码
    public static final String CHANGEUSERPWD = "http://" + IP  + ":8888/user/changePwd";
    //用户登出
    public static final String USERLOGOUT = "http://" + IP  + ":8888/user/logout";
    //修改用户信息
    public static final String CHANGEUSERINFO = "http://" + IP  + ":8888/user/update";

    /**
     * 走失原因
     */
    //获得走失原因列表
    public static final String GETLOSTREADSONLIST = "http://" + IP  + ":8888/lostReason/list";

    /**
     * 走失老人模块
     */
    //对比人脸照片
    public static final String COMPAREELERDLY = "http://" + IP  + ":8888/elderly/compare";
    //获得走失老人的人脸图片
    public static final String GETELEDLYPHOTO = "http://" + IP  + ":8888/elderly/getPhotos/"; //{elderlyId}
    //获得老人的家属信息
    public static final String GETELEDLYPARENTSINFO = "http://" + IP  + ":8888/elderly/getRelatives/"; //{id}
    //获取走失老人列表
    public static final String GETLOSTELEDLYLIST = "http://" + IP  + ":8888/elderly/list";
    /**
     * 位置模块
     */
    //获取某实体位置
    public static final String GETENTITYPOINT = "http://" + IP  + ":8888/track/getPoint";
    //获取鹰眼平台中同一队伍的所有人的实时位置
    public static final String GETTEAMALLPEOPLEPOINT = "http://" + IP  + ":8888/track/getPoints";
    //把某人的位置上传到鹰眼平台
    public static final String UPLOADMYPOINT = "http://" + IP  + ":8888/track/upload";
    /**
     * 队伍
     */
    //获取队伍列表
    public static final String GETGROUPLIST = "http://" + IP  + ":8888/group/list";

}
