package com.sameal.dd.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.sameal.dd.R;

public class TipDialog extends Dialog {
    public TipDialog(@NonNull Context context) {
        super(context);
    }

    private ImageView ivClose;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_tips);
        ivClose = findViewById(R.id.iv_close);
        btnBack = findViewById(R.id.btn_back);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                getOwnerActivity().finish();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                getOwnerActivity().finish();
            }
        });
    }
}
