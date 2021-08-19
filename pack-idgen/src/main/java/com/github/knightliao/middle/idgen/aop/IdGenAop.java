package com.github.knightliao.middle.idgen.aop;

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
public class IdGenAop {

    private static final Logger logger = LoggerFactory.getLogger("MIDDLE_IDGEN_LOG");

    @Setter
    private boolean debug = false;

    @Setter
    private boolean metricStatistic = false;

    public IdGenAop() {
    }

    @Pointcut("execution(public * com.github.knightliao.middle.idgen.IIdgenService.*(..)) ")
    public void iidgenServicePoint() {

    }

    @Around("iidgenServicePoint()")
    public Object logExecuteJob(ProceedingJoinPoint joinPoint) throws Throwable {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        String key = "";
        Object ret = "";
        boolean success = true;

        try {

            //
            ret = joinPoint.proceed();
            return ret;

        } catch (Throwable ex) {

            success = false;
            throw ex;

        } finally {

            if (debug) {
                try {
                    doLog(joinPoint, key, ret, stopWatch, success);
                } catch (Exception ex) {
                    log.error(ex.toString(), ex);
                }
            }
        }
    }

    protected void doLog(final ProceedingJoinPoint joinPoint, final String key, Object ret,
                         final StopWatch stopWatch, final boolean success) {

        String methodName = joinPoint.getSignature().getName();
        stopWatch.stop();
        String result = String.valueOf(ret);

        if (debug) {
            LoggerUtil.info(logger, "* [{0}] {1} {2} {3} {4}",
                    methodName, key, result, stopWatch.getTime(), success);
        } else {
            LoggerUtil.infoIfNeed(logger, "* [{0}] {1} {2} {3} {4}",
                    methodName, key, result, stopWatch.getTime(), success);
        }

        if (this.metricStatistic) {
            MonitorHelper.fastCompassOneKey("MIDDLE_IDGEN", methodName, 1, stopWatch.getTime(), success);
        }
    }
}
