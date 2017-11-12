package com.lisn.socketdemo;

import android.text.TextUtils;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.engineio.client.Transport;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Manager;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import static android.content.ContentValues.TAG;

/*
 ****************************
 * 项目名称：socket.io-android-chat-master  
 * 创建人：lisn 
 * 邮箱：cnlishan@163.com
 * 创建时间：2017/9/20.
 * ***************************
 */
public class WebSocketAPI {
    private static final String TAG = "WebSocketAPI";
    private static WebSocketAPI instance;

    private WebSocketAPI(){

    }
    public static WebSocketAPI getInstance(){

        if (instance == null) {
            synchronized (WebSocketAPI.class){
                if (instance == null) {
                    instance = new WebSocketAPI();
                }
            }
        }
        return instance;
    }
    public Socket getSocket() {
        return mSocket;
    }

    private Socket mSocket;

    public void initSocket(String ip, String port,Emitter.Listener onMessageListener) {
        try {

            if (mSocket == null) {
                mSocket = IO.socket("http://" + ip + ":" + port);
                //mSocket = IO.socket("https://" + ip + ":" + port);
//                mSocket.io().on(Manager.EVENT_TRANSPORT,
//                        new Emitter.Listener() {
//                            private final Map<String, String> setCookie = new HashMap<String, String>();
//
//                            @Override
//                            public void call(Object... objects) {
//                                Transport transport = (Transport) objects[0];
//                                transport.on(Transport.EVENT_REQUEST_HEADERS,
//                                        new Emitter.Listener() {
//                                            @Override
//                                            public void call(Object... args) {
//                                                @SuppressWarnings("unchecked")
//                                                Map<String, String> header = (Map<String, String>) args[0];
//                                                Log.i("EVENT_REQUEST_HEADERS",
//                                                        args + "");
//
//                                            }
//                                        });
//
//                                transport.on(Transport.EVENT_RESPONSE_HEADERS,
//                                        new Emitter.Listener() {
//                                            @Override
//                                            public void call(Object... args) {
//                                                @SuppressWarnings("unchecked")
//                                                final Map<String, String> headers = (Map<String, String>) args[0];
//                                                String cookie = headers
//                                                        .get("Set-Cookie");
//                                                Log.i("EVENT_RESPONSE_HEADERS",
//                                                        args + "");
//
//                                            }
//                                        });
//                            }
//                        });

                mSocket.on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        Log.i("EVENT_CONNECT_ERROR", args + "");
                        for (Object o : args) {
                            Log.i("IO " + Socket.EVENT_CONNECT_ERROR,
                                    o.toString());
                        }
                    }
                }).on(Socket.EVENT_CONNECT_TIMEOUT, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        Log.i("EVENT_CONNECT_TIMEOUT", args + "");
                        Log.i("IO", Socket.EVENT_CONNECT_TIMEOUT);
                    }
                }).on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        Log.i("EVENT_CONNECT", args + "");
                        Log.i("IO", Socket.EVENT_CONNECT);
                    }
                }).on("message", onMessageListener);
            }
            mSocket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

//    private Emitter.Listener onMessageListener = new Emitter.Listener() {
//        @Override
//        public void call(Object... objects) {
//            Log.e(TAG, "call: ="+objects.toString() );
//            Log.e(TAG, "call: =="+((String) objects[0]));
//
//        }
//    };
}
