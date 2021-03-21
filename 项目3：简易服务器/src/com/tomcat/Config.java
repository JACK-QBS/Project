package com.tomcat;

import java.util.LinkedHashMap;
import java.util.Map;

// 负责解析 web.conf 并得到最终的对象表示
public class Config {
    public Map<String,String> servletNameToServletClassName;
    // url 对应 servlet 名称,Linked保证顺序（插入顺序）
    public LinkedHashMap<String,String> urlToServletNameMap;

    public Config(Map<String, String> servletNameToServletClassName, LinkedHashMap<String, String> urlToServletNameMap) {
        this.servletNameToServletClassName = servletNameToServletClassName;
        this.urlToServletNameMap = urlToServletNameMap;
    }
}
