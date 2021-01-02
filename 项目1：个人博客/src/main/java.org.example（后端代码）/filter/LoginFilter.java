package org.example.filter;

import org.example.model.JSONResponse;
import org.example.util.JSONUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 配置统一会话管理的过滤器：匹配所有请求路径
 * 服务端资源：/login不用校验Session，其他都要校验，如果不通过，返回401，响应内容随便
 * 前端资源：/jsp/校验Session，不通过重定向到登录页面
 *         /js/, /static/,/view/ 全部不校验
 */
@WebFilter("/*")
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    /**
     * 每次http请求匹配到过滤器路径时，会执行该过滤器的doFilter
     * 如果往下执行，时调用filterChain.doFilter(request,response)
     * 否则自行处理响应
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        String servletPath = req.getServletPath();//获取当前请求的服务路径
        //不需要登录允许访问：往下执行继续调用
        if(servletPath.startsWith("/js/") || servletPath.startsWith("/static/") ||
                servletPath.startsWith("/view/") || servletPath.equals("/login")) {
            filterChain.doFilter(servletRequest,servletResponse);
        } else {
            //获取Session对象，没有就返回null
            HttpSession session = req.getSession(false);
            //验证用户是否登录，如果没有登录，还需要根据前端或后端做不同的处理
            if (session == null || session.getAttribute("user") == null) {
                if (servletPath.startsWith("/jsp/")) {
                    //前端重定向到登录页面
                    resp.sendRedirect(basePath(req)+"/view/login.html");
                } else {
                    //后端返回401状态码
                    resp.setStatus(401);
                    resp.setCharacterEncoding("UTF-8");
                    resp.setContentType("application/json");
                    //返回统一的json数据格式
                    JSONResponse json = new JSONResponse();
                    json.setCode("LOG000");
                    json.setMessage("用户没有登录，不允许访问");
                    PrintWriter pw = resp.getWriter();
                    pw.println(JSONUtil.serialize(json));
                    pw.flush();
                    pw.close();
                }
            } else {
                //敏感资源，但已登录，允许继续执行
                filterChain.doFilter(servletRequest,servletResponse);
            }
        }
    }

    /**
     * 根据http请求，动态的获取访问路径（服务路径之前的部分）
     */
    public static String basePath(HttpServletRequest req) {
        String schema = req.getScheme(); // 获取http
        String host = req.getServerName(); //主机ip或域名
        int port = req.getServerPort(); //服务器端口号
        String contextPath = req.getContextPath(); //应用上下文路径
        return schema + "://" + host + ":" + port + contextPath;
    }

    @Override
    public void destroy() {

    }
}
