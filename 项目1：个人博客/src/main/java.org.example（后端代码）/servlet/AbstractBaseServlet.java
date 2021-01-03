package org.example.servlet;

import jdk.nashorn.internal.scripts.JS;
import org.example.exception.AppException;
import org.example.model.JSONResponse;
import org.example.util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//工具类（基类）:结合jackson序列化响应的统一数据格式，结合自定义异常完成统一的异常处理
public abstract class AbstractBaseServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置请求体的编码格式
        req.setCharacterEncoding("UTF-8");
        //设置响应体的编码
        resp.setCharacterEncoding("UTF-8");
        //设置响应体的数据类型（浏览器要采取什么方式执行）
        resp.setContentType("application/json"); // 设置响应头
        //session会话管理：除登录和注册接口，其他都需要登录后访问
        req.getServletPath();//获取当前请求路径

        JSONResponse json = new JSONResponse();

        try{
            // 调用子类重写方法
            Object data = process(req, resp);
            //子类的process方法执行完没有抛异常，表示业务执行成功
            json.setSuccess(true);
            json.setData(data);
        } catch(Exception e) {
            //异常如何处理？JDBC的异常SQLException,JSON处理的异常，自定义异常返回错误
            e.printStackTrace();
            String code = "unknown";
            String s = "未知的错误";
            if (e instanceof AppException) {
                code = ((AppException) e).getCode();
                s = e.getMessage();
            }
            json.setCode(code);
            json.setMessage(s);
        }
        PrintWriter pw = resp.getWriter();
        pw.println(JSONUtil.serialize(json));
        pw.flush();
        pw.close();
    }

    protected abstract Object process(HttpServletRequest req, HttpServletResponse resp) throws Exception;

}
