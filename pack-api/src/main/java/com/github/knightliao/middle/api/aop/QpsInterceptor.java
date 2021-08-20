package com.github.knightliao.middle.api.aop;

import java.util.concurrent.CompletionStage;

import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.knightliao.middle.api.aop.helper.QpsAopLogUtils;
import com.github.knightliao.middle.api.core.dto.BaseResponse;
import com.github.knightliao.middle.lang.constants.PackConstants;

import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/20 01:21
 */
@Slf4j
@Aspect
public class QpsInterceptor {

    private static final String LOGGER_NAME_REQUEST_LOG = "RL";

    private static final Logger logger = LoggerFactory.getLogger(LOGGER_NAME_REQUEST_LOG);

    @Pointcut("@annotation(com.github.knightliao.middle.api.aop.QpsAnnotation)")
    public void around() {
    }

    @Around("around()")
    public Object process(ProceedingJoinPoint joinPoint) throws Throwable {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        boolean isSuccess = true;
        int statusCode = PackConstants.DEFAULT_ERROR_VALUE_INT;
        Object ret = null;
        QpsAnnotation qpsAnnotation = null;

        try {

            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            qpsAnnotation = signature.getMethod().getAnnotation(QpsAnnotation.class);
            if (qpsAnnotation == null) {
                return joinPoint.proceed();
            }

            //
            ret = joinPoint.proceed();
            statusCode = getStatusCode(ret);

            return ret;

        } catch (Throwable throwable) {

            isSuccess = false;
            throw new Throwable(throwable);

        } finally {

            if (qpsAnnotation != null) {
                try {
                    boolean isAsync = doAsyncLogIfExist(ret, joinPoint, stopWatch, qpsAnnotation);
                    if (!isAsync) {
                        QpsAopLogUtils.doLog(logger, "", joinPoint, qpsAnnotation, stopWatch, isSuccess, true,
                                statusCode);
                    }
                } catch (Exception e) {
                    log.error(e.toString(), e);
                }
            }
        }
    }

    private int getStatusCode(Object result) {

        if (result instanceof BaseResponse) {
            BaseResponse baseResponse = (BaseResponse) result;
            return baseResponse.getStatus();
        }

        return PackConstants.DEFAULT_ERROR_VALUE_INT;
    }

    private boolean doAsyncLogIfExist(Object ret, ProceedingJoinPoint joinPoint, StopWatch stopWatch,
                                      QpsAnnotation qpsAnnotation) {

        if (!(ret instanceof CompletionStage)) {
            return false;
        }

        CompletionStage completionStage = (CompletionStage) ret;
        completionStage.whenComplete((result, throable) -> {

            if (throable != null) {
                QpsAopLogUtils.doLog(logger, "", joinPoint, qpsAnnotation, stopWatch, false, true,
                        PackConstants.DEFAULT_ERROR_VALUE_INT);
            } else {

                int statusCode = getStatusCode(result);
                QpsAopLogUtils.doLog(logger, "", joinPoint, qpsAnnotation, stopWatch, false, true,
                        statusCode);
            }
        });

        return true;
    }

}
