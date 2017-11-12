package com.lisn.socketdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Socket mSocket;
    private EditText et_input;
    private Button bt_send;
    private static final String TAG = "MainActivity";
    private LinearLayout ll_container;

    private android.content.Context mContext;
    private Button bt_disconnect;
    private Button bt_connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initView();
        WebSocketAPI socketAPI = WebSocketAPI.getInstance();
        socketAPI.initSocket(Constants.IP, Constants.PORT, onMessageListener);
        mSocket = socketAPI.getSocket();

    }

    @Override
    protected void onDestroy() {
//        mSocket.disconnect();
        super.onDestroy();
    }

    private Emitter.Listener onMessageListener = new Emitter.Listener() {
        @Override
        public void call(Object... objects) {

            final String data = (String) objects[0];
            Log.e(TAG, "call: ==" + data);

            if (!TextUtils.isEmpty(data)) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView textView = new TextView(mContext);
                        textView.setText(data);
                        ll_container.addView(textView);
                    }
                });

            }

        }
    };

    private void initView() {
        et_input = (EditText) findViewById(R.id.et_input);
        bt_send = (Button) findViewById(R.id.bt_send);
        bt_disconnect = (Button) findViewById(R.id.bt_disconnect);
        bt_connect = (Button) findViewById(R.id.bt_connect);
        ll_container = (LinearLayout) findViewById(R.id.ll_container);

        bt_send.setOnClickListener(this);
        bt_connect.setOnClickListener(this);
        bt_disconnect.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_send:
                sendMessage();
                break;
            case R.id.bt_disconnect:
                mSocket.disconnect();
                break;
            case R.id.bt_connect:
                mSocket.connect();
                break;
        }
    }

    private void sendMessage() {
        String input = et_input.getText().toString().trim();
        if (TextUtils.isEmpty(input)) {
            Toast.makeText(this, "发送内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        mSocket.emit("message", input);

    }
}
