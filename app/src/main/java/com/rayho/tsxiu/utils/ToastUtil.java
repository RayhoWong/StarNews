package com.rayho.tsxiu.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.rayho.tsxiu.R;

/**
 * Created by Rayho on 2018/11/8
 */
public class ToastUtil {

    private Toast mToast;
    private TextView mTextView;
    private TimeCount timeCount;
    private String message;

    public ToastUtil(Context context, String msg) {
        message = msg;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //自定义toast布局
        View view = inflater.inflate(R.layout.toast, null);
        //自定义toast文本
        mTextView = view.findViewById(R.id.toast_msg);
        mTextView.setText(msg);

        Log.i("ToastUtil", "Toast start...");
        if (mToast == null) {
            mToast = new Toast(context);
            Log.i("ToastUtil", "Toast create...");
        }

        mToast.setGravity(Gravity.BOTTOM, 0, 0);//设置位置(默认底部弹出)
        mToast.setDuration(Toast.LENGTH_LONG);//设置默认时长
        mToast.setView(view);//加载布局
    }

    /**
     * 默认show
     */
    public void show() {
        mToast.show();
        Log.i("ToastUtil", "Toast show...");
    }

    public void showAtCenter() {
        mToast.setGravity(Gravity.CENTER, 0, 0);//居中位置
        mToast.show();
        Log.i("ToastUtil", "Toast show...");
    }

    public void showAtTop() {
        mToast.setGravity(Gravity.TOP, 0, 0);//顶部设置位置
        mToast.show();
        Log.i("ToastUtil", "Toast show...");
    }

    /**
     * 自定义时长show
     *
     * @param duration
     */
    public void show(int duration) {
        timeCount = new TimeCount(duration, 1000);
        timeCount.start();
        mToast.show();
    }

    /**
     * 隐藏toast
     */
    private void hide() {
        if (mToast != null) {
            mToast.cancel();
        }
    }


    /**
     * 自定义计时器
     */
    private class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval); //millisInFuture总计时长，countDownInterval时间间隔(一般为1000ms)
        }

        @Override
        public void onTick(long millisUntilFinished) {
//            mTextView.setText(message + ": " + millisUntilFinished / 1000 + "s后消失");
            mTextView.setText(message);
        }

        @Override
        public void onFinish() {
            hide();
        }
    }
}



