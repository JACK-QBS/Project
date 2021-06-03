package org.example.tool;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * MyBatis数据库生成工具的启动类（生成 Mybatis相关代码）
 * （根据数据库，生成 Mapper、实体类、xml文件）
 */
public class Generator {

    private static final boolean OVERWRITE = true;

    private static final String CONFIG_PATH = "generator/config.xml";

    public static void main(String[] args) throws Exception {

        System.out.println("--------------------start generator-------------------");
        List<String> warnings = new ArrayList<>();

        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream(CONFIG_PATH);
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(is);
        DefaultShellCallback callback = new DefaultShellCallback(OVERWRITE);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
        warnings.forEach(System.err::println);
        System.out.println("--------------------end generator-------------------");
    }
}