package com.tomcat.http;

import com.standard.http.Cookie;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

//http请求解析工作
//单例对象 -- 负责从 socket.inputStream 解析出 HTTP 对象
public class HttpRequestParser {
    public Request parse(InputStream socketInputStream) throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(socketInputStream,"UTF-8");
        //1、解析方法（转大写）
        String method = scanner.next().toUpperCase();
        //2、解析路径
        String path = scanner.next();
        String requestURI = path;
        Map<String,String> parameters = new HashMap<>();
        int i = path.indexOf("?");
        if (i != -1) {
            requestURI = path.substring(0,i);
            String queryString = path.substring(i+1);
            for (String kv : queryString.split("&")) {
                String[] kvParts = kv.split("=");
                String name = URLDecoder.decode(kvParts[0].trim(),"UTF-8");
                String value = URLDecoder.decode(kvParts[1].trim(),"UTF-8");
                parameters.put(name,value);
            }
        }
        int j = requestURI.indexOf('/',1);
        String contextPath = "/";
        String servletPath = requestURI;
        if (j != -1) {
            contextPath = requestURI.substring(1,j);
            servletPath = requestURI.substring(j);
        }

        //3、得到版本信息
        String version = scanner.nextLine();

        //读取请求头
        String headerLine;
        Map<String,String> headers = new HashMap<>();
        List<Cookie> cookieList = new ArrayList<>();
        //请求行不为空
        while (scanner.hasNextLine() && !(headerLine = scanner.nextLine().trim()).isEmpty()) {
            //解析请求头信息
            String[] parts = headerLine.split(":");
            String name = parts[0].trim().toLowerCase();
            String value = parts[1].trim();
            headers.put(name,value);

            if (name.equals("cookie")) {
                String[] kvParts = value.split(";");
                for (String kvPart : kvParts) {
                    if (kvPart.trim().isEmpty()) {
                        continue;
                    }
                    String[] split = kvPart.split("=");
                    String cookieName = split[0].trim();
                    String cookieValue = split[1].trim();
                    Cookie cookie = new Cookie(cookieName,cookieValue);
                    cookieList.add(cookie);
                }
            }
        }
        //工厂模式的方法
        return new Request(method,requestURI,contextPath,servletPath,parameters,headers,cookieList);
    }
}
