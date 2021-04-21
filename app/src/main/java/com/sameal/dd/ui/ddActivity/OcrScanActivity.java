package com.sameal.dd.ui.ddActivity;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.sameal.dd.R;
import com.sameal.dd.common.MyActivity;
import com.sameal.dd.ui.dialog.TipDialog;

/**
 * @author zhangj
 * @date 2021/2/2 19:26
 * desc 身份证扫描
 */
public class OcrScanActivity extends MyActivity {

    TipDialog tipDialog;
    ImageView ivBack;
    RelativeLayout rlTitleBar;
//    @BindView(R.id.ocr_view)
//    OcrScanView ocrView;
    TextView tvScanType;
    TextView tvScanTypeDesc;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ocr_scan;
    }

    @Override
    protected void initView() {
        rlTitleBar = (RelativeLayout) findViewById(R.id.rl_title_bar);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvScanType = (TextView) findViewById(R.id.tv_scan_type);
        tvScanTypeDesc = (TextView) findViewById(R.id.tv_scan_type_desc);
    }

    @Override
    protected void initData() {
        tipDialog = new TipDialog(this);
//        boolean isBackOcr = "back".equals(getString(IntentKey.SCANTYPE));
//        if (isBackOcr) {
//            OcrScanner.getInstance().setScanType(OcrScanner.SCAN_TYPE_NATIONAL_EMBLEM);
//            tvScanType.setText("国徽面拍摄");
//            tvScanTypeDesc.setText("将身份证国徽面放入采集框中");
//        } else {
//            OcrScanner.getInstance().setScanType(OcrScanner.SCAN_TYPE_AVATAR);
//            tvScanType.setText("人像面拍摄");
//            tvScanTypeDesc.setText("将身份证人像面放入采集框中");
//        }
////        OcrScanner.getInstance()
////                .init(getApplicationContext(), ocrView, AppConfig.ocrBusinessId);
//        OcrScanner.getInstance().setCropListener(new OcrCropListener() {
//            @Override
//            public void onSuccess(String s) {
//                Intent intent = new Intent();
//                intent.putExtra(IntentKey.PIC_PATH, s);
//                setResult(1001, intent);
//                finish();
//            }
//
//            @Override
//            public void onError(int i, String s) {
//                toast(s);
//            }
//
//            @Override
//            public void onOverTime() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        tipDialog.show();
//                    }
//                });
//                toast("检测超时");
//            }
//        });
//        OcrScanner.getInstance().setTimeOut(10000);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                OcrScanner.getInstance().start();
//            }
//        }, 500);
//        ivBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        OcrScanner.getInstance().stop();
    }
}