package com.standard.http;

import com.standard.ServletRequest;

public interface HttpServletRequest extends ServletRequest {
    Cookie[] getCookies();

    String getHeader(String name);

    //获取http方法
    String getMethod();

    /**
     * 比如： /sb/say-hello?target=cool
     * contextPath: "sb"
     * servletPath: "/say-hello"
     * requestURL: "/sb/say-hello"
     */
    String getRequestURI();
    String getContextPath();
    String getServletPath();

    HttpSession getSession();
}
