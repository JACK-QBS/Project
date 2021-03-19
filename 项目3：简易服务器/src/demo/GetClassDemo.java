package demo;

public class GetClassDemo {
    public static void main(String[] args) throws ClassNotFoundException {
        String className = "com.webapps.dictionary.TranslateServlet";

        //利用反射特性，通过 String 类名称，加载并得到 Class 类信息
        Class<?> clazz = Class.forName(className);
    }
}
