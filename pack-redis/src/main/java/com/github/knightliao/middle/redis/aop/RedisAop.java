package com.github.knightliao.middle.redis.aop;

import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.knightliao.middle.log.LoggerUtil;
import com.github.knightliao.middle.metrics.MonitorHelper;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/19 17:00
 */
@Slf4j
@Aspect
public class RedisAop {

    private static final Logger logger = LoggerFactory.getLogger("MIDDLE_REDIS_LOG");

    @Setter
    private boolean debug = false;

    @Setter
    private boolean metricStatistic = false;

    public RedisAop() {
    }

    @Pointcut("execution(public * com.github.knightliao.middle.redis.IMyRedisService.*(..)) "
            + "|| execution(public * com.github.knightliao.middle.redis.IMyRedisBatchService.*(..))")
    public void myRedisServicePoint() {

    }

    @Around("myRedisServicePoint()")
    public Object logExecuteJob(ProceedingJoinPoint joinPoint) throws Throwable {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        String key = "";
        String key2 = "";
        boolean success = true;

        try {

            //
            Object object = joinPoint.getTarget();

            //
            if (debug) {
                Object[] args = joinPoint.getArgs();
                if (args.length >= 1 && args[0] instanceof String) {
                    key = (String) args[0];
                }

                if (args.length >= 2 && args[1] instanceof String) {
                    key2 = (String) args[1];
                }
            }

            return joinPoint.proceed();

        } catch (Throwable ex) {

            success = false;
            throw ex;

        } finally {

            if (debug) {
                try {
                    doLog(joinPoint, key, key2, stopWatch, success);
                } catch (Exception ex) {
                    log.error(ex.toString(), ex);
                }
            }
        }
    }

    protected void doLog(final ProceedingJoinPoint joinPoint, final String key, final String key2,
                         final StopWatch stopWatch, final boolean success) {

        String methodName = joinPoint.getSignature().getName();
        stopWatch.stop();

        if (debug) {
            LoggerUtil.info(logger, "* [{0}] {1} {2} {3} {4}",
                    methodName, key, key2, stopWatch.getTime(), success);
        } else {
            LoggerUtil.infoIfNeed(logger, "* [{0}] {1} {2} {3} {4}",
                    methodName, key, key2, stopWatch.getTime(), success);
        }

        if (this.metricStatistic) {
            MonitorHelper.fastCompassOneKey("MIDDLE_REDIS", methodName, 1, stopWatch.getTime(), success);
        }
    }
}
