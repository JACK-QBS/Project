package org.example.config.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.base.JSONResponse;
import org.example.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;

/**
 * 统一会话管理的拦截器
 */
public class LoginInterceptor implements HandlerInterceptor {

    private ObjectMapper objectMapper;

    public LoginInterceptor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if(session != null){//获取登录时设置的用户信息
            User user = (User) session.getAttribute("user");
            if(user != null){//登录了，允许访问
                return true;
            }
        }
        //登录失败，不允许访问的业务：区分前后端
        //TODO：前端跳转登录页面，后端返回json
//        new ObjectMapper().writeValueAsString(object);//序列化对象为json字符串
        //请求的服务路径
        String servletPath = request.getServletPath();//   /apiXXX.html
        if(servletPath.startsWith("/api/")){//后端逻辑：返回json
            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            JSONResponse json = new JSONResponse();
            json.setCode("USR000");
            json.setMessage("用户没有登录，不允许访问");
            String s = objectMapper.writeValueAsString(json);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            PrintWriter pw = response.getWriter();
            pw.println(s);
            pw.flush();
        }else{//前端逻辑：跳转到登录页面 /views/index.html
            //相对路径的写法，一定是请求路径作为相对位置的参照点
            //使用绝对路径来重定向，不建议使用相对路径和转发
            String schema = request.getScheme();//http
            String host = request.getServerName();//ip
            int port = request.getServerPort();//port
            String contextPath = request.getContextPath();//application Context path应用上下文路径
            String basePath = schema+"://"+host+":"+port+contextPath;
            //重定向到登录页面
            response.sendRedirect(basePath+"/index.html");
        }
        return false;
    }
}
