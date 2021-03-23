package com.webapps.dictionary;

import com.standard.ServletException;
import com.standard.http.HttpServlet;
import com.standard.http.HttpServletRequest;
import com.standard.http.HttpServletResponse;
import com.standard.http.HttpSession;

import java.io.IOException;

public class ProfileActionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            //未登录，跳转登录页面
            resp.sendRedirect("login.html");
        } else {
            resp.setContentType("text/plain");
            resp.getWriter().println(user.toString());
        }
    }
}
