package com.estar.track.Listener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.estar.track.event.MessageEvent;
import com.estar.track.service.MessageService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WsManager implements IWsManager {
    private final static int RECONNECT_INTERVAL = 10 * 1000;    //重连自增步长
    private final static long RECONNECT_MAX_TIME = 120 * 1000;   //最大重连间隔
    private Context mContext;
    private String wsUrl;
    private WebSocket mWebSocket;
    private OkHttpClient mOkHttpClient;
    private Request mRequest;
    private int mCurrentStatus = WsStatus.DISCONNECTED;     //websocket连接状态
    private boolean isNeedReconnect;          //是否需要断线自动重连
    private boolean isManualClose = false;         //是否为手动关闭websocket连接
    private Lock mLock;
    private Handler wsMainHandler = new Handler(Looper.getMainLooper());
    private int reconnectCount = 0;   //重连次数
    private String testStr = "aa";
    private Runnable reconnectRunnable = new Runnable() {
        @Override
        public void run() {
            Log.e("websocket", "服务器重连接中...");
            buildConnect();
        }
    };

    private WebSocketListener mWebSocketListener = new WebSocketListener() {
 
        @Override
        public void onOpen(WebSocket webSocket, final Response response) {
            mWebSocket = webSocket;
            setCurrentStatus(WsStatus.CONNECTED);
            connected();
            if (Looper.myLooper() != Looper.getMainLooper()) {
//                JSONObject jo = new JSONObject();
//                jo.put("act", 1);
//                sendMessage(jo.toJSONString());
                Log.d("wssssNotMain","非主线程连接成功");
            } else {
                Log.e("wsssssMainLooper", "服务器连接成功");
            }
        }
 
        @Override
        public void onMessage(WebSocket webSocket, final ByteString bytes) {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                wsMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("websocket", "WsManager-----onMessage");
                    }
                });
            } else {
                Log.e("websocket", "WsManager-----onMessage");
            }
        }

        @Override
        public void onMessage(WebSocket webSocket, final String text) {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                wsMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("websocket", text);
                        if(text.equals("pong")){

                        }else {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject = JSONObject.parseObject(text);
                        }

                        //此时通知页面收到队友
                        if(text.equals("pong")){
//                            EventBus.getDefault().post(new MessageEvent("needpeople"));
                        }
                        if(text.equals("90003")){
                            //召集队员
                            EventBus.getDefault().post(new MessageEvent("needpeople"));
                        }
                        if (text.equals("90002")){
                            //锁定任务
                            EventBus.getDefault().post(new MessageEvent("locktask"));
                        }
                        if(text.equals("90001")){
                            //审核此任务
                            EventBus.getDefault().post(new MessageEvent("checktask"));
                        }
                    }
                });
            } else {
                Log.e("websocket", text);
            }
        }
 
        @Override
        public void onClosing(WebSocket webSocket, final int code, final String reason) {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                wsMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("websocket", "服务器连接关闭中");
                    }
                });
            } else {
                Log.e("websocket", "服务器连接关闭中");
            }
        }
 
        @Override
        public void onClosed(WebSocket webSocket, final int code, final String reason) {
 
            if (Looper.myLooper() != Looper.getMainLooper()) {
                wsMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("websocket", "服务器连接已关闭");
                    }
                });
            } else {
                Log.e("websocket", "服务器连接已关闭");
            }
        }
 
        @Override
        public void onFailure(WebSocket webSocket, final Throwable t, final Response response) {
            try {
                tryReconnect();
                Log.e("liusehngjei", "连接失败");
//                Log.d("ResponseisW",response.body().toString());
                Log.d("TTTT",t.toString());
                if (Looper.myLooper() != Looper.getMainLooper()) {
                    wsMainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("websocket", "服务器连接失败");
                        }
                    });
                } else {
                    Log.e("websocket", "服务器连接失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
 
    public WsManager(Builder builder) {
        mContext = builder.mContext;
        wsUrl = builder.wsUrl;
        isNeedReconnect = builder.needReconnect;
        mOkHttpClient = builder.mOkHttpClient;
        this.mLock = new ReentrantLock();
    }


 
    private void initWebSocket() {
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .build();
        }
        if (mRequest == null) {
            mRequest = new Request.Builder()
                    .url(wsUrl)
                    .build();
        }
        mOkHttpClient.dispatcher().cancelAll();
        try {
            mLock.lockInterruptibly();
            try {
                mOkHttpClient.newWebSocket(mRequest, mWebSocketListener);
            } finally {
                mLock.unlock();
            }
        } catch (InterruptedException e) {
        }
    }
 
    @Override
    public WebSocket getWebSocket() {
        return mWebSocket;
    }
 
 
    @Override
    public synchronized boolean isWsConnected() {
        return mCurrentStatus == WsStatus.CONNECTED;
    }
 
    @Override
    public synchronized int getCurrentStatus() {
        return mCurrentStatus;
    }
 
    @Override
    public synchronized void setCurrentStatus(int currentStatus) {
        this.mCurrentStatus = currentStatus;
    }
 
    @Override
    public void startConnect() {
        isManualClose = false;
        buildConnect();
    }
 
    @Override
    public void stopConnect() {
        isManualClose = true;
        disconnect();
    }
 
    private void tryReconnect() {
        if (!isNeedReconnect | isManualClose) {
            return;
        }
        Log.e("reC", "reconnectCount2222222[" + reconnectCount + "]");
        if (!isNetworkConnected(mContext)) {
            setCurrentStatus(WsStatus.DISCONNECTED);
            Log.e("checkNet", "[请您检查网络，未连接]");
//            return;
        }
        setCurrentStatus(WsStatus.RECONNECT);
        Log.e("reCCC", "reconnectCount11111111[" + reconnectCount + "]");
        long delay = reconnectCount * RECONNECT_INTERVAL;
//        wsMainHandler.postDelayed(reconnectRunnable, delay > RECONNECT_MAX_TIME ? RECONNECT_MAX_TIME : delay);
        wsMainHandler.postDelayed(reconnectRunnable, 10000);
        Log.e("recc", "reconnectCount[" + reconnectCount + "]");
        reconnectCount++;
    }
 
    private void cancelReconnect() {
        wsMainHandler.removeCallbacks(reconnectRunnable);
        reconnectCount = 0;
    }
 
    private void connected() {
        cancelReconnect();
    }
 
    private void disconnect() {
        if (mCurrentStatus == WsStatus.DISCONNECTED) {
            return;
        }
        cancelReconnect();
        if (mOkHttpClient != null) {
            mOkHttpClient.dispatcher().cancelAll();
        }
        if (mWebSocket != null) {
            boolean isClosed = mWebSocket.close(WsStatus.CODE.NORMAL_CLOSE, WsStatus.TIP.NORMAL_CLOSE);
            //非正常关闭连接
            if (!isClosed) {
                Log.e("websocket", "服务器连接失败");
            }
        }
        setCurrentStatus(WsStatus.DISCONNECTED);
    }
 
    private synchronized void buildConnect() {
        if (!isNetworkConnected(mContext)) {
            setCurrentStatus(WsStatus.DISCONNECTED);
//            return;
        }
        switch (getCurrentStatus()) {
            case WsStatus.CONNECTED:

            case WsStatus.CONNECTING:
                break;
            default:
                setCurrentStatus(WsStatus.CONNECTING);
                initWebSocket();
        }
    }
 
    //发送消息
    @Override
    public boolean sendMessage(String msg) {
        return send(msg);
    }
 
    @Override
    public boolean sendMessage(ByteString byteString) {
        return send(byteString);
    }
 
    private boolean send(Object msg) {
        boolean isSend = false;
        if (mWebSocket != null && mCurrentStatus == WsStatus.CONNECTED) {
            if (msg instanceof String) {
                isSend = mWebSocket.send((String) msg);
            } else if (msg instanceof ByteString) {
                isSend = mWebSocket.send((ByteString) msg);
            }
            //发送消息失败，尝试重连
            if (!isSend) {
                tryReconnect();
            }
        }
        return isSend;
    }
 
    //检查网络是否连接
    private boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            @SuppressLint("MissingPermission") NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
 
    public static final class Builder {
 
        private Context mContext;
        private String wsUrl;
        private boolean needReconnect = true;
        private OkHttpClient mOkHttpClient;
 
        public Builder(Context val) {
            mContext = val;
        }
 
        public Builder wsUrl(String val) {
            wsUrl = val;
            return this;
        }
 
        public Builder client(OkHttpClient val) {
            mOkHttpClient = val;
            return this;
        }
 
        public Builder needReconnect(boolean val) {
            needReconnect = val;
            return this;
        }
 
        public WsManager build() {
            return new WsManager(this);
        }
 
    }
}