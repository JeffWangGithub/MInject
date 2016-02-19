package com.glanwang.minject.inject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @title:
 * @description:
 * @company: 美丽说（北京）网络科技有限公司
 * @author: Glan
 * @version: Created on 16/2/2.
 */
public class MInject {
    private static final String METHOD_NAME_SETONCLICKLISTENER = "setOnClickListener";


    /**
     * Dialog中使用，
     * @param dialog
     */
    public static void inject(Dialog dialog){
        inject(dialog,dialog.getWindow().getDecorView());
    }

    /**
     * 在Activity中使用
     * @param activity
     */
    public static void inject(Activity activity){
        inject(activity,activity.getWindow().getDecorView());
    }

    /**
     * 在v4包中的Fragment中使用
     * @param fragment
     */
    public static void inject(Fragment fragment){
        inject(fragment,fragment.getView());
    }

    /**
     * 在Fragment中使用
     * @param fragment
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void inject(android.app.Fragment fragment){
        inject(fragment,fragment.getView());
    }


    /**
     * 注入View，并同时注入点击事件，通用方法，可在任意类中使用
     * @param target 注解方法所归属的对象
     * @param rootView 注解id所对应的view的rootView
     */
    public static void inject(final Object target, View rootView ){
        if(target == null){
            return;
        }
        injectView(target, rootView);
        injectOnclick(target, rootView);
    }

    /**
     * 仅仅注入点击事件
     * @param target
     * @param rootView
     */
    public static void injectClickEvent(final Object target, View rootView){
        injectOnclick(target, rootView);
    }

    /**
     * 注入View
     * @param target 当前字段所属的对象
     * @param rootView 当前View所属的rootView
     */
    private static void injectView(Object target, View rootView) {
        Field[] declaredFields = target.getClass().getDeclaredFields();
        for (int i = 0; i < declaredFields.length; i++) {
            Field field = declaredFields[i];
            Inject annotation = field.getAnnotation(Inject.class);
            if(annotation != null){
                int viewId = annotation.value();
                if(viewId != -1){
                    try {
                        Object injectView = rootView.findViewById(viewId);
                        if(injectView == null){
                            throw new Resources.NotFoundException("未找到id为"+viewId+"的view");
                        }
                        field.setAccessible(true);
                        field.set(target,injectView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    throw new IllegalArgumentException("MViewInject注解的参数异常");
                }
            }
        }
    }

    /**
     * 注入Onclick事件
     * @param target 当前注解方法所属的对象
     * @param rootView 当前View所属的rootView
     */
    private static void injectOnclick(Object target, View rootView) {
        //1,获取到target中所有声明的方法
        Method[] declaredMethods = target.getClass().getDeclaredMethods();
        for (int i = 0; i < declaredMethods.length; i++) {
            final Method declaredMethod = declaredMethods[i];
            declaredMethod.setAccessible(true);//设置强制读取
            //2，获取含有MOnclick的注解方法
            MOnclick onclickAnnotation = declaredMethod.getAnnotation(MOnclick.class);
            if(onclickAnnotation != null){
                //3. 拿到注解中所有view的id
                int[] viewIds = onclickAnnotation.value();
                Class listenerType = onclickAnnotation.listenerType();
                long shakeTime = onclickAnnotation.shakeTime();//获取防抖时间

                //4，设置一个代理，并给代理指定要代理的方法
                ProxyInvocationHandler proxyInvocationHandler = new ProxyInvocationHandler(target);
                proxyInvocationHandler.addHandleMethod(onclickAnnotation.proxyMethodName(),declaredMethod);
                proxyInvocationHandler.setSnakeTime(shakeTime);//设置抖动时间

                Object proxyListener = Proxy.newProxyInstance(listenerType.getClassLoader(), new Class[]{listenerType}, proxyInvocationHandler);
                for (int viewId:viewIds) {
                    View mView = rootView.findViewById(viewId);
                    if(mView != null){
                        try {
                            Method setOnClickListenerMethod = mView.getClass().getMethod(METHOD_NAME_SETONCLICKLISTENER, View.OnClickListener.class);
                            setOnClickListenerMethod.setAccessible(true);
                            //5，将代理设置给当前View
                            setOnClickListenerMethod.invoke(mView,proxyListener);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

}
