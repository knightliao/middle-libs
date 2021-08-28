package com.github.knightliao.middle.soa.trace;

import java.util.UUID;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.MDC;

import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/28 17:58
 */
@Slf4j
public class MyTraceUtils {

    public static final String TRACE_ID = "traceId";

    // 从web request继承
    public static void startTraceFromHttpRequest(ServletRequest servletRequest) {

        try {

            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            String traceId = httpServletRequest.getHeader(TRACE_ID);

            // put to mdc
            MDC.put(TRACE_ID, traceId);

            //
        } catch (Exception ex) {
            log.error(ex.toString(), ex);
        }
    }

    // 新开一个trace
    public static void newTrace() {

        try {

            String traceId = UUID.randomUUID().toString().replace("-", "");

            // put to mdc
            MDC.put(TRACE_ID, traceId);

            //
        } catch (Exception ex) {
            log.error(ex.toString(), ex);
        }
    }

    public static void addTraceToResponse(ServletResponse servletResponse) {

        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        httpServletResponse.addHeader(TRACE_ID, getTraceId());
    }

    public static String getTraceId() {

        try {

            return MDC.get(TRACE_ID);

        } catch (Exception ex) {
            log.error(ex.toString(), ex);

            return "";
        }
    }

}
