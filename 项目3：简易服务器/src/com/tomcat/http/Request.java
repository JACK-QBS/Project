package com.tomcat.http;

import com.standard.http.Cookie;
import com.standard.http.HttpServletRequest;
import com.standard.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Request implements HttpServletRequest {
    public final String method;
    public final String requestURL;
    public final String contextPath;
    public final String servletPath;
    public final Map<String,String> parameters;
    public final Map<String,String> headers;
    public final List<Cookie> cookieList;
    public HttpSessionImpl session = null;

    public Request(String method, String requestURI, String contextPath, String servletPath, Map<String, String> parameters, Map<String, String> headers, List<Cookie> cookieList) throws IOException, ClassNotFoundException {
        this.method = method;
        this.requestURL = requestURI;
        this.contextPath = contextPath;
        this.servletPath = servletPath;
        this.parameters = parameters;
        this.headers = headers;
        this.cookieList = cookieList;
        for (Cookie cookie : cookieList) {
            if (cookie.getName().equals("session-id")) {
                String sessionId = cookie.getValue();
                session = new HttpSessionImpl(sessionId);
                break;
            }
        }
    }

    @Override
    public String toString() {
        return String.format("Request{%s %s %s %s %s}",method,requestURL,parameters,headers,session);
    }

    @Override
    public Cookie[] getCookies() {
        return cookieList.toArray(new Cookie[0]);
    }

    @Override
    public String getHeader(String name) {
        return headers.get(name);
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getRequestURI() {
        return requestURL;
    }

    @Override
    public String getContextPath() {
        return contextPath;
    }

    @Override
    public String getServletPath() {
        return servletPath;
    }

    @Override
    public HttpSession getSession() {
        if (session != null) {
            return session;
        }
        return new HttpSessionImpl();
    }

    @Override
    public String getParameter(String name) {
        return parameters.get(name);
    }
}
