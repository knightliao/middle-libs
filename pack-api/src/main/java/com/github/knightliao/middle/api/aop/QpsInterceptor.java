package com.github.knightliao.middle.api.aop;

import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/20 01:21
 */
@Aspect
public class QpsInterceptor {

    private static final String LOGGER_NAME_REQUEST_LOG = "RL";

    private static final Logger logger = LoggerFactory.getLogger(LOGGER_NAME_REQUEST_LOG);

}
