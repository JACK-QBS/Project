package org.example.util;

import org.example.model.DocInfo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 步骤一：
 * 从本地 api 目录，遍历静态 html 文件
 * 每一个 html 需要构建正排索引：本地某个文件
 * 正文索引信息 List<DocInfo>
 *     DocInfo(id,title,content,url)
 */
public class Parser {
    //api目录
    public static final String API_PATH = "D:\\Code\\Project\\docs\\api";
    //构建的本地文件正排索引
    public static final String RAW_DATA = "D:\\Code\\Project/raw_data.txt";
    //官方api文档的根路径(拼接本地api路径)
    public static final String API_BASE_PATH = "https://docs.oracle.com/javase/8/docs/api";

    public static void main(String[] args) throws IOException {
        //找到api本地路径下所有的html文件
        List<File> htmls = listHtml(new File(API_PATH));
        FileWriter fw = new FileWriter(RAW_DATA);
        PrintWriter pw = new PrintWriter(fw,true); //打印输出流，自动刷新缓冲区
        for (File html : htmls) {
            //一个html解析DocInfo有的属性（输入）
            DocInfo doc = parseHtml(html);
            //保存本地正排索引文件（输出）（行号代表id）
            //格式：一行为一个doc，title+\3 + url + content
            String uri = html.getAbsolutePath().substring(API_PATH.length());
            System.out.println("Parse: "+uri);
            if(doc.getTitle().contains("�")){
                System.out.println("title====================="+doc.getTitle());
            }
            if(doc.getContent().contains("�")){
                System.out.println("content====================="+doc.getContent());
            }
            pw.println(doc.getTitle()+"\3"+doc.getUrl()+"\3"+doc.getContent());
        }
    }

    private static DocInfo parseHtml(File html) {
        DocInfo doc = new DocInfo();
        //ArrayList.html长度-5
        doc.setTitle(html.getName().substring(0,html.getName().length()-".html".length()));
        //获取相对路径
        String uri = html.getAbsolutePath().substring(API_PATH.length());
        doc.setUrl(API_BASE_PATH + uri);
        doc.setContent(parseContent(html));
        //目前是从本地api目录的html文件解析为文档对象，这步不需要设置id
        return doc;
    }

    /**
     * 解析 html 内容
     * <标签>内容</标签>
     * 只取内容，有多个标签就拼接
     */
    private static String parseContent(File html) {
        StringBuilder sb = new StringBuilder();
        try {
            FileReader fr = new FileReader(html);
            int i;
            boolean isContent = false;//判断是标签还是内容
            //一个字符一个字符来读取
            while ((i = fr.read()) != -1) {
                char c = (char) i;
                if (isContent) {
                    if (c == '<') {
                        //当前标签的内容读取结束   <标签>内容<
                        isContent = false;
                        continue;
                    } else if (c == '\n' || c == '\r') { // 换行符 \r\n
                        sb.append(" ");
                    } else {
                        sb.append(c);//拼接标签内容
                    }
                } else if (c == '>'){
                    //当前不是正文，并且读取到>，之后就是正文   <标签
                    isContent = true;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    //递归遍历html文件(根据传入的目录)
    private static List<File> listHtml(File dir) {
        List<File> list = new ArrayList<>();
        //列出目录中的子文件和子文件夹
        File[] children = dir.listFiles();
        if (children != null) {
            for (File child : children) {
                if (child.isDirectory()) {
                    //若是子文件夹：递归调用获取子文件夹内的html文件
                    list.addAll(listHtml(child));
                } else if (child.getName().endsWith(".html")) {
                    list.add(child);
                }
            }
        }
        return list;
    }
}
