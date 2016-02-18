package com.glanwang.minject.inject;

import android.view.View;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @title:
 * @description:
 * @company: 美丽说（北京）网络科技有限公司
 * @author: Glan
 * @version: Created on 16/2/1.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MOnclick {
    int[] value();
    Class listenerType() default View.OnClickListener.class;
    String proxyMethodName() default "onClick";
    long shakeTime() default 0;//View点击的防抖动时间，单位是毫秒
}
