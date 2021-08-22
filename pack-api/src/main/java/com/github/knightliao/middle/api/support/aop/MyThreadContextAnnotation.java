package com.github.knightliao.middle.api.support.aop;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/22 16:09
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyThreadContextAnnotation {
}
