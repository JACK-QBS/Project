package com.webapps.dictionary;

import com.standard.ServletException;
import com.standard.http.HttpServlet;
import com.standard.http.HttpServletRequest;
import com.standard.http.HttpServletResponse;
import com.standard.http.HttpSession;

import java.io.IOException;

public class LoginActionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        if (username.equals("qbs") && password.equals("123")) {
            User user = new User(username,password);
            HttpSession session = req.getSession();
            session.setAttribute("user",user);

            resp.sendRedirect("profile-action");
        } else {
            resp.sendRedirect("login.html");
        }
    }
}
