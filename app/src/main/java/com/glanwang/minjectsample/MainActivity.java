package com.glanwang.minjectsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.glanwang.minject.inject.Inject;
import com.glanwang.minject.inject.MInject;
import com.glanwang.minject.inject.MOnclick;
import com.glanwang.minjectsample.utils.ToastUtils;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MInject";

    @Inject(R.id.btn_textview)
    TextView mBtnTextView;
    @Inject(R.id.btn_button)
    Button mBtnButton;
    @Inject(R.id.btn_imageview)
    ImageView mBtnImageView;
    @Inject(R.id.btn_textview2)
    TextView mTextView2;
    @Inject(R.id.btn_reset)
    TextView mBtnReset;
    @Inject(R.id.btn_checkview)
    TextView mBtnCheckView;
    @Inject(R.id.show)
    TextView mShow;
    @Inject(R.id.btn_dialog)
    Button mBtnDialog;

    private long clickCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //注入View，病支持点击事件方法的注入
        MInject.inject(this);
    }


    @MOnclick(value = R.id.btn_textview)
    private void click1(View view){
        if(view.getId() == R.id.btn_textview){
            clickCount();
            ToastUtils.showShort(this,"点击了TextView按钮"+clickCount);
        }
    }

    @MOnclick(value = {R.id.btn_imageview, R.id.btn_button})
    private void click2(View view){
        switch (view.getId()) {
            case R.id.btn_button:
                clickCount();
                ToastUtils.showShort(this,"点击了Button按钮"+clickCount);
                break;
            case R.id.btn_imageview:
                clickCount();
                ToastUtils.showShort(this,"点击了ImageView按钮"+clickCount);
                break;
        }
    }


    //设置了btn_textview2的点击抖动时间为5000毫秒，5秒内不能连续点击
    @MOnclick(value = R.id.btn_textview2, shakeTime = 5000)
    private void click3(View view){
        switch (view.getId()) {
            case R.id.btn_textview2:
                clickCount();
                ToastUtils.showShort(this,"点击抖动时间为5s，5s内不能连续点击"+clickCount);
                break;
        }
    }

    @MOnclick(value = {R.id.btn_checkview, R.id.btn_reset},shakeTime = 1500)
    private void click4(View view){
        switch (view.getId()) {
            case R.id.btn_checkview:
                clickCount();
                checkView(mBtnTextView,mBtnButton,mBtnImageView,mTextView2,mBtnReset,mBtnCheckView,mShow);
                break;
            case R.id.btn_reset:
                clickCount = 0;
                mShow.setText("点击计数="+clickCount);
                ToastUtils.showShort(this,"计数已经重置");
                break;
        }

    }



    private void checkView(View... views){
        int i;
        for (i = 0; i < views.length; i++) {
            Log.d(TAG, views[i].getClass().getName());
        }
        if(i == views.length){
            ToastUtils.showShort(this,"注入成功,请看Log");
        } else {
            ToastUtils.showShort(this,"注入失败");
        }
    }


    /**
     * 记录点击数目
     */
    private void clickCount() {
        clickCount++;
        mShow.setText("点击计数="+clickCount);
    }

}
