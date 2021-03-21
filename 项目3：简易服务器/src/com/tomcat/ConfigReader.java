package com.tomcat;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

// 负责解析 web.conf 并得到最终的对象表示
public class ConfigReader {
    public Config read(String name) throws IOException {

        Map<String,String> servletNameToServletClassName = new HashMap<>();
        // url 对应 servlet 名称,Linked保证顺序（插入顺序）
        LinkedHashMap<String,String> urlToServletNameMap = new LinkedHashMap<>();

        //进行 web.conf 文件的读取 + 解析
        //web.conf 放哪里，必须符合规范，否则就会读不到
        String filename = String.format("%s/%s/WEB-INF/web.conf", HttpServer.WEBAPPS_BASE,name);
        String stage = "start";//"servlets"/"mappings"
        //进行文本文件内容的读取
        try (InputStream is = new FileInputStream(filename)) {
            Scanner scanner = new Scanner(is,"UTF-8");
            // 只要还有一行，我就读一行
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    //如果是空行、或是注释行，跳过
                    continue;
                }
                switch (stage) {
                    case "start":
                        if (line.equals("servlets:")) {
                            stage = "servlets";
                        }
                        break;
                    case "servlets":
                        if (line.equals("servlet-mappings:")) {
                            stage = "mappings";
                        } else {
                            //进行 ServletName => ServletClassName 的解析
                            //用 = 进行分割
                            String[] parts = line.split("=");
                            String servletName = parts[0].trim();
                            String servletClassName = parts[1].trim();
                            servletNameToServletClassName.put(servletName,servletClassName);
                        }
                        break;
                    case "mappings":
                        //进行 URL => ServletName 的解析
                        String[] parts = line.split("=");
                        String url = parts[0].trim();
                        String servletName = parts[1].trim();
                        urlToServletNameMap.put(url,servletName);
                        break;
                }
            }
        }
        return new Config(servletNameToServletClassName,urlToServletNameMap);
    }
}
