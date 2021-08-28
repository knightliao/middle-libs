package com.github.knightliao.middle.web.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/28 17:49
 */
public interface HttpServletRequestFilterCallback {

    void before(ServletRequest request, ServletResponse servletResponse);

    void after(ServletRequest request, ServletResponse servletResponse);
}
