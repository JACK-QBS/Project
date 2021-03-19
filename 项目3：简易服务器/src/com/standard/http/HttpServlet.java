package com.standard.http;

import com.standard.Servlet;
import com.standard.ServletException;
import com.standard.ServletRequest;
import com.standard.ServletResponse;

import java.io.IOException;

//抽象类
public abstract class HttpServlet implements Servlet {
    @Override
    public void init() throws ServletException {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void service(ServletRequest req, ServletResponse resp) throws ServletException, IOException {
        if (req instanceof HttpServletRequest && resp instanceof HttpServletResponse) {
            HttpServletRequest httpReq = (HttpServletRequest) req;
            HttpServletResponse httpResp = (HttpServletResponse) resp;
            service(httpReq,httpResp);
        } else {
            throw new ServletException("不支持非 HTTP 协议");
        }
    }

    protected void service(HttpServletRequest req,HttpServletResponse resp) throws ServletException,IOException {
        if (req.getMethod().equals("GET")) {
            doGet(req, resp);
        } else {
            resp.sendError(405);//方法不支持
        }
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,IOException {
        resp.sendError(405);
    }
}
