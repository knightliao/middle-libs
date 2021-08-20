package com.github.knightliao.middle.api.aop;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/20 01:19
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface QpsAnnotation {

    // 打印请求参数
    boolean printParam() default false;

    // 超过一定时间才进行打印，小于0则没有这个限制
    int logIfLongTime() default -1;

    // 只有需求时才打日志
    boolean logIfNeed() default false;
}
