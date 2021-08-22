package com.github.knightliao.middle.api.support.aop.helper;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;

import com.github.knightliao.middle.api.support.aop.QpsAnnotation;
import com.github.knightliao.middle.api.core.dto.MyBaseRequest;
import com.github.knightliao.middle.log.LoggerUtil;
import com.github.knightliao.middle.metrics.MonitorHelper;
import com.github.knightliao.middle.utils.log.LogControllerTemplate;
import com.github.knightliao.middle.utils.trans.JsonUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/20 13:58
 */
@Slf4j
public class QpsAopLogUtils {

    public static void doLog(Logger logger, String loggerName, final ProceedingJoinPoint joinPoint,
                             QpsAnnotation qpsAnnotation, final StopWatch stopWatch,
                             final boolean success, boolean isAsync, int statusCode) {

        try {

            // name
            String className = joinPoint.getTarget().getClass().getSimpleName();
            String methodName = joinPoint.getSignature().getName();

            //
            stopWatch.stop();
            long costTime = stopWatch.getTime();
            doLog(logger, loggerName, joinPoint, qpsAnnotation,
                    className, methodName, costTime,
                    success, isAsync, statusCode);

            //
            memoryStatistic(qpsAnnotation, className, methodName, costTime, success, statusCode);

        } catch (Exception e) {

            LogControllerTemplate.doFewLog(() -> {
                log.error(e.toString(), e);
            });

        } finally {

        }
    }

    private static void doLog(Logger logger, String loggerName, final ProceedingJoinPoint joinPoint,
                              QpsAnnotation qpsAnnotation,
                              String className, String methodName, long costTime,
                              final boolean success, boolean isAsync, int statusCode) {

        // 参数
        String params = getParam(joinPoint, qpsAnnotation);

        // format if need
        String format = "";
        boolean doFormatLog = true;
        if (qpsAnnotation.logIfLongTime() > 0 && costTime > qpsAnnotation.logIfLongTime()) {
        } else {
            doFormatLog = false;
        }

        if (doFormatLog) {
            format = String.format("[%s],%s,%s,%d,%b,%b,%d,%s",
                    loggerName, className, methodName,
                    costTime,
                    success, isAsync,
                    statusCode,
                    params);
        }

        //
        if (qpsAnnotation.logIfNeed()) {
            LoggerUtil.infoIfNeed(logger, format);
        } else {
            LoggerUtil.info(logger, format);
        }

    }

    private static String getParam(final ProceedingJoinPoint joinPoint, QpsAnnotation qpsAnnotation) {

        if (qpsAnnotation == null || !qpsAnnotation.printParam()) {
            return "";
        }

        List<String> paramList = new ArrayList<>();
        Object[] sigs = joinPoint.getArgs();

        for (Object sig : sigs) {

            if (sig != null) {

                if (sig instanceof MyBaseRequest) {
                    paramList.add(sig.toString());
                }
            }
        }

        return JsonUtils.toJson(paramList);
    }

    private static void memoryStatistic(QpsAnnotation qpsAnnotation, String className, String method,
                                        long costTime, boolean success, int statusCode) {

        MonitorHelper.fastCompassTwoKey("qps", className, method, statusCode, costTime, success);
    }
}
