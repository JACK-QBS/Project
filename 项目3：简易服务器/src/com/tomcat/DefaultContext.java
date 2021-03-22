package com.tomcat;

import com.standard.Servlet;

// 处理没人处理的请求
public class DefaultContext extends Context{
    public DefaultContext(ConfigReader reader) {
        super(reader, "/");
    }

    @Override
    public Servlet get(String servletPath) {
        return HttpServer.notFoundServlet;
    }
}
