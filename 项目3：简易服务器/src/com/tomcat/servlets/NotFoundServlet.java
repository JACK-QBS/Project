package com.tomcat.servlets;

import com.standard.ServletException;
import com.standard.http.HttpServlet;
import com.standard.http.HttpServletRequest;
import com.standard.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

//处理 404 的情况
public class NotFoundServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(404);
        resp.setContentType("text/html");
        PrintWriter writer = resp.getWriter();
        writer.println("<h1>该资源不存在</h1>");
    }
}
