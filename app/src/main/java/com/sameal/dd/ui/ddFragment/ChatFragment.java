package com.sameal.dd.ui.ddFragment;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.sameal.dd.R;
import com.sameal.dd.common.MyAdapter;
import com.sameal.dd.common.MyFragment;
import com.sameal.dd.helper.LogUtils;
import com.sameal.dd.other.AppConfig;
import com.sameal.dd.socket.JWebSocketClient;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangj
 * @date 2021/1/13 22:20
 * desc 聊天
 */
public class ChatFragment extends MyFragment {


    RecyclerView recy;
    EditText etContent;
    AppCompatButton btnSend;
    private JWebSocketClient webSocketClient;

    private ChatAdapter chatAdapter;
    private LinearLayoutManager manager;

    private String id;

    public static ChatFragment newInstance(String id) {
        return new ChatFragment(id);
    }

    public ChatFragment(String id) {
        this.id = id;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chat;
    }

    @Override
    protected void initView() {
        recy = (RecyclerView) findViewById(R.id.recy);
        etContent = (EditText) findViewById(R.id.et_content);
        btnSend = (AppCompatButton) findViewById(R.id.btn_send);

    }

    @Override
    protected void initData() {
        recy.setLayoutManager(new LinearLayoutManager(getActivity()));
        manager = (LinearLayoutManager) recy.getLayoutManager();
        chatAdapter = new ChatAdapter(getActivity());
        recy.setAdapter(chatAdapter);
        setOnClickListener(R.id.btn_send);

        URI uri = URI.create("ws://chat.workerman.net:7272");
        webSocketClient = new JWebSocketClient(uri) {
            @Override
            public void onMessage(String message) {
                super.onMessage(message);
                LogUtils.d(TAG, "onMessage: " + message);
                try {
                    JSONObject object = new JSONObject(message);
                    if (object.getString("type").equals("login")) {
                        String client_name = object.getString("client_name");
                        String msg = "<font color='#808080'>" + client_name + "</font>  进入直播间";
                        Message message1 = new Message();
                        message1.obj = msg;
                        handler.sendMessage(message1);
                    } else if (object.getString("type").equals("say")) {
                        String client_name = object.getString("from_client_name");
                        String content = object.getString("content");
                        String msg = "<font color='#808080'>" + client_name + "</font>  " + content;
                        Message message1 = new Message();
                        message1.obj = msg;
                        handler.sendMessage(message1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        try {
            webSocketClient.connectBlocking();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (webSocketClient != null && webSocketClient.isOpen()) {
            Map<String, String> map = new HashMap<>();
            map.put("type", "login");
            map.put("room_id", id);
            map.put("client_name", AppConfig.getLoginBean().getNickname());
            Log.e("JWebSocketClient1", "send:" + new Gson().toJson(map));
            webSocketClient.send(new Gson().toJson(map));
        }
    }

    @Override
    public void onDestroy() {
        if (webSocketClient != null) {
            webSocketClient.close();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (TextUtils.isEmpty(etContent.getText().toString())) {
            toast("请输入发送内容");
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("type", "say");
        map.put("room_id", id);
        map.put("to_client_id", "all");
        map.put("to_client_name", "所有人");
        map.put("client_name", AppConfig.getLoginBean().getNickname());
        map.put("content", etContent.getText().toString());
        Log.e("JWebSocketClient1", "send:" + new Gson().toJson(map));
        webSocketClient.send(new Gson().toJson(map));
        etContent.setText("");
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            int size = chatAdapter.getItemCount();
            chatAdapter.addItem(msg.obj.toString());
            chatAdapter.notifyItemInserted(size);
            int lastItemPosition = manager.findLastCompletelyVisibleItemPosition();
            if (lastItemPosition != size - 1) {
                recy.smoothScrollToPosition(size);
            } else {
                recy.scrollToPosition(size);
            }
        }
    };

    class ChatAdapter extends MyAdapter<String> {

        public ChatAdapter(@NonNull Context context) {
            super(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(R.layout.item_chat);
        }

        class ViewHolder extends MyAdapter.ViewHolder {

            private TextView tvContent;

            public ViewHolder(int id) {
                super(id);
                tvContent = (TextView) findViewById(R.id.tv_content);
            }

            @Override
            public void onBindView(int position) {
                tvContent.setText(Html.fromHtml(getItem(position)));
            }
        }
    }

}