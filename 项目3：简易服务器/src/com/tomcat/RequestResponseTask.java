package com.tomcat;

import com.standard.Servlet;
import com.standard.http.Cookie;
import com.tomcat.http.HttpRequestParser;
import com.tomcat.http.Request;
import com.tomcat.http.Response;

import java.io.*;
import java.net.Socket;
import java.util.Map;

//请求 - 响应周期
public class RequestResponseTask implements Runnable{
    private final Socket socket;
    private static final HttpRequestParser parser = new HttpRequestParser();

    public RequestResponseTask(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            //1、解析得到请求对象
            Request request = parser.parse(socket.getInputStream());
            System.out.println(request);
            //2、实例化一个响应对象
            Response response = new Response();
            //3、根据 request.getContextPath() 找到哪个 Context 进行处理
            Context handleContext = HttpServer.defaultContext;
            for (Context context : HttpServer.contextList) {
                if (context.getName().equals(request.getContextPath())) {
                    handleContext = context;
                    break;
                }
            }
            //4、根据 req.getServletPath() 找到 Context 中的哪个 HttpServlet 进行处理
            Servlet servlet = handleContext.get(request.getServletPath());
            if (servlet == null) {
                servlet = HttpServer.defaultServlet;
            }
            //5、调用 service方法，交给业务处理
            servlet.service(request,response);
            System.out.println(response);
            //6、根据 response 对象中的数据，发送 HTTP 响应
            sendResponse(socket.getOutputStream(),request,response);

            socket.close();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    private void sendResponse(OutputStream outputStream, Request request, Response response) throws IOException {
        //保存 session
        //1、种 cookie
        //2、保存成文件
        if (request.session != null) {
            Cookie cookie = new Cookie("session-id",request.session.sessionId);
            response.addCookie(cookie);
            request.session.saveSessionData();
        }

        Writer writer = new OutputStreamWriter(outputStream,"UTF-8");
        PrintWriter printWriter = new PrintWriter(writer);
        //处理 cookie
        for (Cookie cookie : response.cookieList) {
            response.setHeader("Set-Cookie",String.format("%s=%s",cookie.getName(),cookie.getValue()));
        }

        //设置响应行
        printWriter.printf("HTTP/1.0 %d OK\r\n",response.status);
        //设置响应头
        for (Map.Entry<String,String> entry : response.headers.entrySet()) {
            String name = entry.getKey();
            String value = entry.getValue();
            printWriter.printf("%s: %s\r\n",name,value);
        }
        printWriter.printf("\r\n");
        //设置响应体
        response.bodyPrintWriter.flush();
        response.bodyOutputStream.flush();
        printWriter.flush();

        byte[] bytes = response.bodyOutputStream.toByteArray();
        outputStream.write(bytes);
        outputStream.flush();
    }
}
