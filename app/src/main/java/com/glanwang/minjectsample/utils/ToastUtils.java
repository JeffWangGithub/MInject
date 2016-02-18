package com.glanwang.minjectsample.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * @title:
 * @description:
 * @company: 美丽说（北京）网络科技有限公司
 * @author: Glan
 * @version: Created on 16/2/18.
 */
public class ToastUtils {

    private static Toast mToast;

    public static void showShort(Context context, String text){
        if(mToast == null){
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            if (isToastShowing(mToast)) {
                mToast.cancel();
            }
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            mToast.setDuration(Toast.LENGTH_SHORT);
            mToast.setText(text);
        }
        mToast.show();
    }

    public static void showLong(Context context, String text){
        if(mToast == null){
            mToast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        } else {
            if (isToastShowing(mToast)) {
                mToast.cancel();
            }
            mToast = Toast.makeText(context, text, Toast.LENGTH_LONG);
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        mToast.show();
    }

    private static boolean isToastShowing(Toast toast){
        if(toast != null){
            return  toast.getView().isShown();
        } else {
            return false;
        }
    }
}
