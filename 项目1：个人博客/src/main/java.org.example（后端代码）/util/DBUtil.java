package org.example.util;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import org.example.exception.AppException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

//数据库的连接获取和释放
public class DBUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/servlet_blog?user=root&password=123456&useUnicode=true&characterEncoding=UTF-8&useSSL=false";
    //定义连接池对象
    private static final DataSource DS = new MysqlDataSource();

    /**
     * 工具类提供数据库JDBC操作
     * 不足：1、static代码块出现错误，NOClassDefFoundError表示类可以找到，但是类加载失败，无法运行
     *         ClassNotFoundException：找不到类
     *      2、工具类设计上不是最优的，数据库框架ORM框架Mybatis，都是模式设计的
     */
    static {
        ((MysqlDataSource) DS).setUrl(URL);
    }
    public static Connection getConnection() {
        try {
            return DS.getConnection();
        } catch (SQLException e) {
            //抛自定义异常
            throw new AppException("DB001","获取数据库连接失败",e);
        }
    }


    public static void close(Connection c, Statement s) {
        close(c,s,null);
    }
    public static void close(Connection c, Statement s, ResultSet r) {
        try {
            if (r != null)
                r.close();
            if (s != null)
                s.close();
            if (c != null)
                c.close();
        } catch (SQLException e) {
            throw new AppException("DB002","数据库释放资源出错",e);
        }
    }
}
