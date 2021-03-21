package com.tomcat.http;

import com.standard.http.Cookie;
import com.standard.http.HttpServletResponse;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Response implements HttpServletResponse {
    public final List<Cookie> cookieList;
    public final Map<String,String> headers;
    public int status = 200;
    public final ByteArrayOutputStream bodyOutputStream;
    public final PrintWriter  bodyPrintWriter;

    public Response() throws UnsupportedEncodingException {
        cookieList = new ArrayList<>();
        headers = new HashMap<>();
        bodyOutputStream = new ByteArrayOutputStream(1024);
        Writer writer = new OutputStreamWriter(bodyOutputStream,"UTF-8");
        bodyPrintWriter = new PrintWriter(writer);
    }

    @Override
    public String toString() {
        try {
            bodyPrintWriter.flush();
            bodyOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return String.format("Response{%d %s %s}",status,headers,bodyOutputStream.toString());
    }

    @Override
    public void addCookie(Cookie cookie) {
        cookieList.add(cookie);
    }

    @Override
    public void sendError(int sc) {

    }

    @Override
    public void sendRedirect(String location) {
        //设置响应
        setStatus(307);
        setHeader("Location",location);
    }

    @Override
    public void setHeader(String name, String value) {
        headers.put(name, value);
    }

    @Override
    public void setStatus(int sc) {
        status = sc;
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        //写入响应体（byte）
        return bodyOutputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        //写入响应体（text）
        return bodyPrintWriter;
    }

    @Override
    public void setContentType(String type) {
        if (type.startsWith("text/")) {
            type = type + ";charset=utf-8";
        }
        setHeader("Content-Type",type);
    }
}
