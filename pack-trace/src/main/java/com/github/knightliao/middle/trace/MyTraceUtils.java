package com.github.knightliao.middle.trace;

import java.util.UUID;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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
    public static final String REQ_ID = "reqId";

    // 从web request继承
    public static void startTraceFromHttpRequest(ServletRequest servletRequest) {

        try {

            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            String traceId = httpServletRequest.getHeader(TRACE_ID);

            if (StringUtils.isEmpty(traceId)) {
                traceId = newTrace();
            } else {
                // put to mdc
                MDC.put(TRACE_ID, traceId);
            }

            //
        } catch (Exception ex) {
            log.error(ex.toString(), ex);
        }
    }

    // 新开一个trace
    public static String newTrace() {

        try {

            String traceId = UUID.randomUUID().toString().replace("-", "");

            // put to mdc
            MDC.put(TRACE_ID, traceId);

            //

            return traceId;

        } catch (Exception ex) {
            log.error(ex.toString(), ex);

            return "";
        }
    }

    // 新开一个 reqId
    public static String newReqId() {

        try {

            String reqId = UUID.randomUUID().toString().replace("-", "");

            // put to mdc
            MDC.put(REQ_ID, reqId);

            //

            return reqId;

        } catch (Exception ex) {
            log.error(ex.toString(), ex);

            return "";
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
