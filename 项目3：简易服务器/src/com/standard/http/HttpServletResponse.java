package com.standard.http;

import com.standard.ServletResponse;

public interface HttpServletResponse extends ServletResponse {
    void addCookie(Cookie cookie);

    void sendError(int sc);

    //发送跳转及情况
    void sendRedirect(String location);

    void setHeader(String name,String value);

    //设置状态码
    void setStatus(int sc);
}
