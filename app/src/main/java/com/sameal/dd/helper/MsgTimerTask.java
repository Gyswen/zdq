package com.sameal.dd.helper;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.sameal.dd.R;

import java.util.Timer;
import java.util.TimerTask;

public class MsgTimerTask {
    private Timer timer;
    private TextView textView;
    private int count = 60;

    public static MsgTimerTask getInstance(TextView textView) {
        return new MsgTimerTask(textView);
    }

    private MsgTimerTask(TextView textView){
        this.textView = textView;
        textView.setClickable(false);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.arg1 = count;
                if (count != -1){
                    count--;
                } else {
                    return;
                }
                handler.sendMessage(message);
            }
        },1000,1000);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            int count = msg.arg1;
            if (count == 0){
                textView.setText(R.string.get_code);
                textView.setClickable(true);
            } else {
                textView.setText(count+"");
            }
        }
    };
}
