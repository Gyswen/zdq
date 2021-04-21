package com.sameal.dd.socket;

import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public class JWebSocketClient extends WebSocketClient {
    public JWebSocketClient(URI serverUri) {
        super(serverUri, new Draft_6455());
        Log.d("EasyHttp", "JWebSocketClient: "+serverUri.getPath());
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.d("EasyHttp", "onOpen()");
    }

    @Override
    public void onMessage(String message) {
        Log.d("EasyHttp", "onMessage()" + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.d("EasyHttp", "onClose()");
    }

    @Override
    public void onError(Exception ex) {
        Log.d("EasyHttp", "onError()" + ex.toString());
    }
}