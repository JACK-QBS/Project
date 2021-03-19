package com.standard;

import java.io.IOException;

//Servlet生命周期
public interface Servlet {

    //一生只调用一次 创建
    void init() throws ServletException;

    //每次和它相关的请求就会调用一次
    void service(ServletRequest req,ServletResponse resp) throws ServletException, IOException;

    //一生只调用一次 销毁
    void destroy();
}
