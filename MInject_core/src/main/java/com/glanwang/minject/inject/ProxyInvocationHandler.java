package com.glanwang.minject.inject;

import android.support.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @title:
 * @description:
 * @company: 美丽说（北京）网络科技有限公司
 * @author: Glan
 * @version: Created on 16/2/1.
 */
public class ProxyInvocationHandler implements InvocationHandler {

    private Map<String, Method> methodMap = new HashMap<>();
    private WeakReference<Object> target;
    private long invokeTime = -1;
    private long snakeTime = 800;

    public ProxyInvocationHandler(@NonNull Object target) {
        this.target = new WeakReference<Object>(target);
    }

    public void addHandleMethod(String proxyedMethodName, Method proxyMethod) {
        methodMap.put(proxyedMethodName, proxyMethod);
    }

    /**
     * 设置防抖动时间，单位为毫秒
     *
     * @param snakeTime
     */
    public void setSnakeTime(long snakeTime) {
        this.snakeTime = snakeTime;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        String proxyedMethodName = method.getName();//被代理的方法的名字
        if (proxyedMethodName.equalsIgnoreCase("onClick")) {
            //onClick方法
            Method proxyMethod = methodMap.get(proxyedMethodName);
            if (proxyMethod != null && target != null) {
                proxyMethod.setAccessible(true);

                if (invokeTime > 0 && System.currentTimeMillis() - invokeTime < snakeTime) {
                    return null;
                }
                invokeTime = System.currentTimeMillis();
            }
            return target != null && proxyMethod != null ? proxyMethod.invoke(target.get(), args) : null;
        } else {
            return proxy != null ? method.invoke(proxy, args) : null;
        }

    }
}