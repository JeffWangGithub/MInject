package com.glanwang.minject.inject;

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
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Inject {
    int value();
}
