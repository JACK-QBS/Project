package org.example.service;

import org.example.exception.AppException;
import org.example.mapper.SettingMapper;
import org.example.mapper.UserMapper;
import org.example.model.Setting;
import org.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * service 调用 mapper
 */
@Service//注册到容器
public class UserService {
    //注入 mapper 属性
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SettingMapper settingMapper;

    @Value("${user.head.local-path}")//引用一个值(头像图片路径)
    private String headLocalPath;
    @Value("${user.head.local-path}")


    private String headRemotePath;

    //通过一个账号在数据库中查找
    public User query(String username) {
        return userMapper.query(username);
    }

    //注册：
    @Transactional//绑定事务
    public void register(User user, MultipartFile headFile) {
        try {
            //用户注册：在数据库中插入数据（插入后，自增主键会绑定到 User 对象）
            userMapper.insertSelective(user);//用户表插入数据
            //用户注册并登录后，跳转到设置页面，页面初始化就查询设置表数据绑定的奖项，
            //所以注册时，在插入一条和用户绑定的设置数据
            Setting setting = new Setting();
            setting.setUserId(user.getId());
            setting.setBatchNumber(8);//每次抽奖人数
            settingMapper.insertSelective(setting);//设置表插入数据

            //如果有上传用户头像（保存时，需要考虑 localPath 后的相对路径是在并发情况下唯一）
            //1、需要保存在服务端本地，并且部署好，能通过 url 访问
            if (headFile != null) {
                String uuid = UUID.randomUUID().toString();//生成一个唯一的字符串
                //使用上传时的图片名称保存到本地
                String uri = "/" + uuid + "/" + headFile.getOriginalFilename();
                //本地就是保存在 headLocalPath +　uri，http 访问就是 headRemotePath + uri
                String localPath = headLocalPath + uri;
                //如果父文件夹没创建，不能直接保存
                File head = new File(localPath);
                File parent = head.getParentFile();
                if (!parent.exists()) parent.mkdirs();//没有就创建父文件夹
                headFile.transferTo(head);//保存到本地
                //2、用户头像的 url，设置到 user 对象的 head 属性
                user.setHead(headRemotePath + uri);
            }
        } catch (IOException e) {
            throw new AppException("USR003","用户头像保存出错",e);
        }

    }
}
