package com.github.knightliao.middle.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.github.knightliao.middle.trace.MyTraceUtils;
import com.github.knightliao.middle.thread.MyThreadContext;

import lombok.extern.slf4j.Slf4j;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/28 17:47
 */
@Slf4j
public class HttpServletRequestFilter implements Filter {

    private HttpServletRequestFilterCallback httpServletRequestFilterCallback;

    public HttpServletRequestFilter(HttpServletRequestFilterCallback httpServletRequestFilterCallback) {
        this.httpServletRequestFilterCallback = httpServletRequestFilterCallback;
    }

    public HttpServletRequestFilter() {

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        try {

            MyThreadContext.init();

            //
            traceProcess(request, response);

            //
            if (httpServletRequestFilterCallback != null) {
                httpServletRequestFilterCallback.before(request, response);
            }

            //
            chain.doFilter(request, response);

            //
            if (httpServletRequestFilterCallback != null) {
                httpServletRequestFilterCallback.after(request, response);
            }

        } finally {

            MyThreadContext.clean();
        }

    }

    private void traceProcess(ServletRequest serverRequest, ServletResponse response) {

        MyTraceUtils.startTraceFromHttpRequest(serverRequest);

        MyTraceUtils.addTraceToResponse(response);
    }

    @Override
    public void destroy() {

    }
}
