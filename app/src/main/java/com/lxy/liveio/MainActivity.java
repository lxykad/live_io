package com.lxy.liveio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {

    private TextView mTvStatus;

    IO.Options opts = new IO.Options();

    Socket mJoinSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTvStatus = (TextView) findViewById(R.id.tv_status);


        opts.forceNew = true;
        opts.reconnection = true;
        opts.path = "/v1/ws/live";

        try {

            mJoinSocket = IO.socket("https://test.58caimi.com", opts);

            mJoinSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                   // System.out.println("connect===live==conn==" + args);
                }
            });
            mJoinSocket.on(Socket.EVENT_MESSAGE, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("connect==live==msg===" + args);
                }
            });
            mJoinSocket.on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("connect==live==disconn===" + args);
                    mTvStatus.setText("断开连接");
                }
            });
            mJoinSocket.on("join", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("connect==live==join===" + args);

                    sendMsg();
                }
            });
            mJoinSocket.on("chat", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("connect==live==chat===" + args);
                }
            });

            mJoinSocket.connect();


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    //连接服务器
    public void connServer(View view) {

        boolean b = mJoinSocket.connected();
       // System.out.println("connect=====live==b====" + b);

        if (b) {
            mTvStatus.setText("直播连接");
        }

    }

    //加入直播间
    public void joinRoom(View view) {

        JSONObject obj = new JSONObject();

        try {
            obj.put("roomId", "5825f1ff7f6267a11e77762f");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //System.out.println("connect=========room=====" + obj);
        mJoinSocket.emit("join", obj);

    }

    //发送文本消息
    public void sendMsg() {
        JSONObject obj = new JSONObject();

        try {
            obj.put("roomId", "5825f1ff7f6267a11e77762f");
            obj.put("messageType", "text");
            obj.put("content", "我是三石666");
            obj.put("isSystem", "fasle");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("connect=========sendmsg=====" + obj);
        mJoinSocket.emit("chat", obj);
    }

    //发送图片消息
    public void sendImgMsg() {
        JSONObject obj = new JSONObject();

        try {
            obj.put("roomId", "5825f1ff7f6267a11e77762f");
            obj.put("messageType", "text");
            obj.put("content", "我是三石666");
            obj.put("isSystem", false);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("connect=========sendmsg=====" + obj);
        mJoinSocket.emit("chat", obj);
    }

}
