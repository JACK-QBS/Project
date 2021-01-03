package org.example.servlet;

import org.example.dao.ArticleDAO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//文章的删除
@WebServlet("/articleDelete")
public class ArticleDeleteServlet extends AbstractBaseServlet{

    @Override
    protected Object process(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String ids = req.getParameter("ids");
        int num = ArticleDAO.delete(ids.split(","));
        return null;
    }
}
