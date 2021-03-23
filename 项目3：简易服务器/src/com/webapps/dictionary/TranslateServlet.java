package com.webapps.dictionary;

import com.standard.ServletException;
import com.standard.http.HttpServlet;
import com.standard.http.HttpServletRequest;
import com.standard.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class TranslateServlet extends HttpServlet {

    public TranslateServlet() {
        System.out.println("我是 TranslateServlet 的构造方法");
    }

    @Override
    public void init() throws ServletException {
        System.out.println("我是 TranslateServlet 的 init 方法啊");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String english = req.getParameter("english");
        String chinese = translate(english);

        resp.setContentType("text/html");
        PrintWriter writer = resp.getWriter();
        writer.printf("<h1>翻译服务</h1>\r\n");
        writer.printf("<p>%s 的意思是 %s</p>\r\n",english,chinese);
    }

    private String translate(String english) {
        String chinese = english;
        return chinese;
    }
}
