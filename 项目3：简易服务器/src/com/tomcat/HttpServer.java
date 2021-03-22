package com.tomcat;

import com.standard.Servlet;
import com.standard.ServletException;
import com.tomcat.ConfigReader;
import com.tomcat.Context;
import com.tomcat.RequestResponseTask;
import com.tomcat.servlets.DefaultServlet;
import com.tomcat.servlets.NotFoundServlet;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//项目的入口
public class HttpServer {
    public static DefaultServlet defaultServlet = new DefaultServlet();
    public static NotFoundServlet notFoundServlet = new NotFoundServlet();

    public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, ServletException {

        //1、找到所有的 Servlet 对象，进行初始化
        initServer();

        //2、处理服务器逻辑
        startServer();

        //3、找到所有的 Servlet 对象，进行销毁
        destroyServer();
    }

    private static void initServer() throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, ServletException {
        //1、扫描所有的 Context （执行目录扫描）
        scanContexts();
        //2、针对每个 Context，需要知道有哪些 Servlet 类
        parseContextConf();
        //3、类的加载
        loadServletClasses();
        //4、执行实例化过程
        instantiateServletObjects();
        //5、初始化
        initializeServletObject();
    }

    //基础目录：
    public static final String WEBAPPS_BASE = "D:\\Code\\Project\\Server\\webapps";
    public static final List<Context> contextList = new ArrayList<>();
    private static final ConfigReader configReader = new ConfigReader();
    public static final DefaultContext defaultContext = new DefaultContext(configReader);
    //1、扫描所有的 Context （执行目录扫描）
    private static void scanContexts() {
        System.out.println("第一步：扫描出所有的 Contexts");
        File webappsRoot = new File(WEBAPPS_BASE);
        //遍历它下面的所有子目录
        File[] files = webappsRoot.listFiles();
        if (files == null) {
            throw new RuntimeException();
        }
        //处理每个文件
        for (File file : files) {
            if (!file.isDirectory()) {
                //不是目录，就不是web应用
                continue;
            }
            String contextName = file.getName();
            System.out.println(contextName);
            //每一个 servlet 当作 context 对象处理
            Context context = new Context(configReader,contextName);

            contextList.add(context);
        }
    }

    //2、针对每个 Context，需要知道有哪些 Servlet 类
    private static void parseContextConf() throws IOException {
        System.out.println("第二步：解析每个 Context 下的配置文件");
        for (Context context : contextList) {
            //针对每一行读取它的配置文件
            context.readConfigFile();
        }
    }

    //3、类的加载
    private static void loadServletClasses() throws ClassNotFoundException {
        System.out.println("第三步：加载每个 servlet 类");
        for (Context context : contextList) {
            context.loadServletClasses();
        }
    }

    //4、执行实例化过程
    private static void instantiateServletObjects() throws InstantiationException, IllegalAccessException {
        System.out.println("第四步：实例化每个 servlet 对象");
        for (Context context : contextList) {
            context.instantiateServletObjects();
        }
    }

    //5、初始化
    private static void initializeServletObject() throws ServletException {
        System.out.println("第五步：执行每个 servlet 对象的初始化");
        for (Context context : contextList) {
            context.initServletObjects();
        }
        defaultServlet.init();
        notFoundServlet.init();
    }

    //二、服务器处理逻辑
    private static void startServer() throws IOException {
        //线程池的方式
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        ServerSocket serverSocket = new ServerSocket(8080);
        //每次循环，处理一个请求
        while (true) {
            Socket socket = serverSocket.accept();
            Runnable task = new RequestResponseTask(socket);
            threadPool.execute(task);
        }
    }

    //三、销毁
    private static void destroyServer() {
        defaultServlet.destroy();
        notFoundServlet.destroy();
        for (Context context : contextList) {
            context.destroyServlets();
        }
    }
}
