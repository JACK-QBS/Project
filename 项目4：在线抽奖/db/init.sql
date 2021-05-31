drop database if exists lucky_draw;
create database lucky_draw character set utf8mb4;

use lucky_draw;

drop table if exists user;
create table user(
    id int primary key auto_increment,
    username varchar(20) not null unique comment '用户账号',
    password varchar(20) not null comment '密码',
    nickname varchar(20) comment '用户昵称',
    email varchar(50) comment '邮箱',
    age int comment '年龄',
    head varchar(255) comment '头像url',
    create_time timestamp default NOW() comment '创建时间'
) comment '用户表';

drop table if exists setting;
create table setting(
    id int primary key auto_increment,
    user_id int not null comment '用户id',
    batch_number int not null comment '每次抽奖人数',
    create_time timestamp default NOW() comment '创建时间',
    foreign key (user_id) references user(id)
) comment '抽奖设置';

drop table if exists award;
create table award(
    id int primary key auto_increment,
    name varchar(20) not null comment '奖项名称',
    count int not null comment '奖项人数',
    award varchar(20) not null comment '奖品',
    setting_id int not null comment '抽奖设置id',
    create_time timestamp default NOW() comment '创建时间',
    foreign key (setting_id) references setting(id)
) comment '奖项';

drop table if exists member;
create table member(
    id int primary key auto_increment,
    name varchar(20) not null comment '姓名',
    no varchar(20) not null comment '工号',
    setting_id int not null comment '抽奖设置id',
    create_time timestamp default NOW() comment '创建时间',
    foreign key (setting_id) references setting(id)
) comment '抽奖人员';

drop table if exists record;
create table record(
    id int primary key auto_increment,
    member_id int not null comment '中奖人员id',
    award_id int not null comment '中奖奖项id',
    create_time timestamp default NOW() comment '创建时间',
    foreign key (member_id) references member(id),
    foreign key (award_id) references award(id)
) comment '中奖记录';



insert into user(id, username, password, nickname, email, age, head) values (1, 'bit', '123', '小比特', '1111@163.com', 18, 'img/test-head.jpg');

## 数据字典：学生毕业年份
insert into setting(id, user_id, batch_number) values (1, 1, 8);

insert into award(name, count, award, setting_id) values ('特靠谱欢乐奖', 1, '深圳湾一号', 1);
insert into award(name, count, award, setting_id) values ('特靠谱娱乐奖', 5, 'BMW X5', 1);
insert into award(name, count, award, setting_id) values ('特靠谱励志奖', 20, '办公室一日游', 1);



## 数据字典：学生专业
insert into member(name, no, setting_id) values ('李寻欢', '水果刀', 1);
insert into member(name, no, setting_id) values ('郭靖', '降猪十八掌', 1);
insert into member(name, no, setting_id) values ('韦小宝', '抓?龙爪手', 1);
insert into member(name, no, setting_id) values ('风清扬', '孤独九贱', 1);
insert into member(name, no, setting_id) values ('哪吒', '喷气式电单车', 1);
insert into member(name, no, setting_id) values ('渠昊空', 'no2', 1);
insert into member(name, no, setting_id) values ('闵觅珍', 'no2', 1);
insert into member(name, no, setting_id) values ('慈新之', 'no3', 1);
insert into member(name, no, setting_id) values ('户柔绚', 'no4', 1);
insert into member(name, no, setting_id) values ('柯雅容', 'no5', 1);
insert into member(name, no, setting_id) values ('邰虹彩', 'no6', 1);
insert into member(name, no, setting_id) values ('延易蓉', 'no7', 1);
insert into member(name, no, setting_id) values ('吉娇然', 'no8', 1);
insert into member(name, no, setting_id) values ('百里惜蕊', 'no9', 1);
insert into member(name, no, setting_id) values ('云寻双', 'no10', 1);
insert into member(name, no, setting_id) values ('衅嘉颖', 'no11', 1);
insert into member(name, no, setting_id) values ('银以晴', 'no12', 1);
insert into member(name, no, setting_id) values ('保颐和', 'no13', 1);
insert into member(name, no, setting_id) values ('饶燕婉', 'no14', 1);
insert into member(name, no, setting_id) values ('单阳平', 'no15', 1);
insert into member(name, no, setting_id) values ('墨碧春', 'no16', 1);
insert into member(name, no, setting_id) values ('侨诗柳', 'no17', 1);
insert into member(name, no, setting_id) values ('羿灵珊', 'no18', 1);
insert into member(name, no, setting_id) values ('甘凌波', 'no19', 1);
insert into member(name, no, setting_id) values ('希忻然', 'no20', 1);
insert into member(name, no, setting_id) values ('虎晴画', 'no21', 1);
insert into member(name, no, setting_id) values ('闪雅洁', 'no22', 1);
insert into member(name, no, setting_id) values ('风易云', 'no23', 1);
insert into member(name, no, setting_id) values ('泷运盛', 'no24', 1);
insert into member(name, no, setting_id) values ('沐长菁', 'no25', 1);
insert into member(name, no, setting_id) values ('栗芃芃', 'no26', 1);
insert into member(name, no, setting_id) values ('义涵蕾', 'no27', 1);
insert into member(name, no, setting_id) values ('泥清妙', 'no28', 1);
insert into member(name, no, setting_id) values ('亓官清宁', 'no29', 1);
insert into member(name, no, setting_id) values ('侯曜曦', 'no30', 1);
insert into member(name, no, setting_id) values ('齐淑雅', 'no31', 1);
insert into member(name, no, setting_id) values ('邸平松', 'no32', 1);
insert into member(name, no, setting_id) values ('泉千易', 'no33', 1);
insert into member(name, no, setting_id) values ('段彩静', 'no34', 1);
insert into member(name, no, setting_id) values ('伦晓凡', 'no35', 1);
insert into member(name, no, setting_id) values ('余莎莎', 'no36', 1);
insert into member(name, no, setting_id) values ('贵念梦', 'no37', 1);
insert into member(name, no, setting_id) values ('接骊文', 'no38', 1);
insert into member(name, no, setting_id) values ('龚芷蝶', 'no39', 1);
insert into member(name, no, setting_id) values ('丙冷霜', 'no40', 1);
insert into member(name, no, setting_id) values ('卫诗蕊', 'no41', 1);
insert into member(name, no, setting_id) values ('濯雅懿', 'no42', 1);
insert into member(name, no, setting_id) values ('蓝亦竹', 'no43', 1);
insert into member(name, no, setting_id) values ('雷书君', 'no44', 1);
insert into member(name, no, setting_id) values ('刚孤风', 'no45', 1);
insert into member(name, no, setting_id) values ('帛晨蓓', 'no46', 1);
insert into member(name, no, setting_id) values ('雀凝梦', 'no47', 1);
insert into member(name, no, setting_id) values ('於良工', 'no48', 1);
insert into member(name, no, setting_id) values ('从翠阳', 'no49', 1);
insert into member(name, no, setting_id) values ('宫咸英', 'no50', 1);
insert into member(name, no, setting_id) values ('项英光', 'no51', 1);
insert into member(name, no, setting_id) values ('胥友菱', 'no52', 1);
insert into member(name, no, setting_id) values ('慎初翠', 'no53', 1);
insert into member(name, no, setting_id) values ('锺映寒', 'no54', 1);
insert into member(name, no, setting_id) values ('貊飞翔', 'no55', 1);
insert into member(name, no, setting_id) values ('葛秀妮', 'no56', 1);
insert into member(name, no, setting_id) values ('劳令梅', 'no57', 1);
insert into member(name, no, setting_id) values ('昝欣怿', 'no58', 1);
insert into member(name, no, setting_id) values ('党忆柏', 'no59', 1);
insert into member(name, no, setting_id) values ('福月华', 'no60', 1);
insert into member(name, no, setting_id) values ('睢巧春', 'no61', 1);
insert into member(name, no, setting_id) values ('修听枫', 'no62', 1);
insert into member(name, no, setting_id) values ('孔梦竹', 'no63', 1);
insert into member(name, no, setting_id) values ('子车悦欣', 'no64', 1);
insert into member(name, no, setting_id) values ('赵飞宇', 'no65', 1);
insert into member(name, no, setting_id) values ('宁天睿', 'no66', 1);
insert into member(name, no, setting_id) values ('申文心', 'no67', 1);
insert into member(name, no, setting_id) values ('冀轩昂', 'no68', 1);
insert into member(name, no, setting_id) values ('邬代灵', 'no69', 1);
insert into member(name, no, setting_id) values ('佟嘉德', 'no70', 1);
insert into member(name, no, setting_id) values ('溥绿兰', 'no71', 1);
insert into member(name, no, setting_id) values ('改昊昊', 'no72', 1);
insert into member(name, no, setting_id) values ('捷梦影', 'no73', 1);
insert into member(name, no, setting_id) values ('孛书语', 'no74', 1);
insert into member(name, no, setting_id) values ('粟芮优', 'no75', 1);
insert into member(name, no, setting_id) values ('东门虹英', 'no76', 1);
insert into member(name, no, setting_id) values ('漆梓玥', 'no77', 1);
insert into member(name, no, setting_id) values ('尔幻玉', 'no78', 1);
insert into member(name, no, setting_id) values ('丁秋玉', 'no79', 1);
insert into member(name, no, setting_id) values ('平晨旭', 'no80', 1);
insert into member(name, no, setting_id) values ('遇沙羽', 'no81', 1);
insert into member(name, no, setting_id) values ('国琳溪', 'no82', 1);
insert into member(name, no, setting_id) values ('仪谷枫', 'no83', 1);
insert into member(name, no, setting_id) values ('钭尔琴', 'no84', 1);
insert into member(name, no, setting_id) values ('澄慧丽', 'no85', 1);
insert into member(name, no, setting_id) values ('佼清秋', 'no86', 1);
insert into member(name, no, setting_id) values ('缪荌荌', 'no87', 1);
insert into member(name, no, setting_id) values ('闻人幼丝', 'no88', 1);
insert into member(name, no, setting_id) values ('绍美曼', 'no89', 1);
insert into member(name, no, setting_id) values ('回访波', 'no90', 1);

-- 插入抽奖记录
-- insert into record(member_id, award_id) values(56, 1);
insert into record(member_id, award_id) values(1, 2);
insert into record(member_id, award_id) values(33, 2);
insert into record(member_id, award_id) values(13, 3);
insert into record(member_id, award_id) values(41, 3);
insert into record(member_id, award_id) values(54, 3);
insert into record(member_id, award_id) values(85, 3);
