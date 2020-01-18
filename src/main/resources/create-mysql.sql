alter table activity
    drop foreign key FKsf1w5vswlmu7hq5cx8wmxlub3;
alter table activity_period
    drop foreign key FKptg4vppo98idc4h5gawmudg37;
alter table activity_station
    drop foreign key FK46dcjwcjpjkq62lnjbony635i;
alter table comment
    drop foreign key FKraqdqfcg5j5daco4ma3iv2v8l;
alter table comment
    drop foreign key FKb8bhfe2og2c5v52l5w7lb1y50;
alter table comment
    drop foreign key FKk5dgrgaxq2cnqqo788r2gysxo;
alter table comment
    drop foreign key FKqeaqu3dhs9wmfy0y0j148k9eg;
alter table image
    drop foreign key FKcq928mk18houvk5hsy7ske3ca;
alter table image
    drop foreign key FK4rbv3fafist78pxjoly1b9e5j;
alter table image
    drop foreign key FKhiuk4p68oep9m3yef6e1ish5x;
alter table image
    drop foreign key FKklkraduqlebpw7otuyx8sepnj;
alter table record
    drop foreign key FKee83lyereiq1c9l5ao65aedkc;
alter table record
    drop foreign key FK7kj4cl6ibmwih6l0wt7qaj1js;
drop table if exists activity;
drop table if exists activity_period;
drop table if exists activity_station;
drop table if exists comment;
drop table if exists department;
drop table if exists image;
drop table if exists record;
drop table if exists student;
create table activity
(
    id          varchar(255) not null,
    description text,
    name        varchar(255),
    status_id   int default 0,
    department  varchar(255) comment '部门账号',
    primary key (id)
) engine = InnoDB;
create table activity_period
(
    id              varchar(255) not null,
    amount_required int comment '所需人数',
    end_time        timestamp default current_timestamp,
    equ_duration    int comment '等效公益劳动时长
不是简单的末初时间的差，需要单独给定',
    start_time      timestamp default current_timestamp,
    parent          varchar(255),
    primary key (id)
) engine = InnoDB;
create table activity_station
(
    id              varchar(255) not null,
    description     text,
    station_name    varchar(255),
    requirements    text,
    parent_activity varchar(255),
    primary key (id)
) engine = InnoDB;
create table comment
(
    id             varchar(255) not null,
    description    text,
    time           timestamp default current_timestamp,
    activity       varchar(255),
    department     varchar(255) comment '部门账号',
    parent_comment varchar(255),
    student        varchar(25) comment '学号，主键',
    primary key (id)
) engine = InnoDB;
create table department
(
    id        varchar(255) comment '部门账号'                           not null,
    email     varchar(255) comment '该部门管理者的邮箱
用于密码找回',
    manager   varchar(255) comment '管理者姓名',
    name      varchar(255) comment '部门名',
    password  varchar(30) default '123456' comment '初始密码，默认是123456' not null,
    phone_num varchar(11) comment '该部门负责管理公益劳动相关事宜的老师的手机号
也可以是部门电话号',
    primary key (id)
) engine = InnoDB;
create table image
(
    id         varchar(255) not null,
    name       varchar(255) comment '图片描述，即html image标签的alt属性',
    url        varchar(255),
    activity   varchar(255),
    comment    varchar(255),
    department varchar(255) comment '部门账号',
    student    varchar(25) comment '学号，主键',
    primary key (id)
) engine = InnoDB;
create table record
(
    id          varchar(255)                    not null,
    audit_level int comment '公益劳动成绩 ：
0  不通过，若不通过，给评级部门必须填写不通过的理由，见evaluation；
1  通过； 
2  良好； 
3  优秀。',
    comments    text comment '公益劳动结束后，
参加该公益劳动的学生对所参加的活动的文字评价,
初始值置空',
    evaluation  varchar(255) comment '0  不通过，见audit_level；
若不通过，给评级部门必须填写不通过的理由（不超过255字）；',
    info        varchar(255),
    is_passed   bit,
    stars       int default 0 comment '公益劳动结束后，
学生反馈评星 
（0，1，2，3，4，5）
默认未评星（0）' not null,
    status_id   int default 0 comment '标志任一公益劳动项目所处状态
'         not null,
    the_period  varchar(255),
    volunteer   varchar(25) comment '学号，主键',
    primary key (id)
) engine = InnoDB;
create table student
(
    id             varchar(25) comment '学号，主键'                           not null,
    class          varchar(45) comment '班级
可从学籍数据库调用',
    email          varchar(255) comment '联系邮箱用于密码找回',
    grade          varchar(45) comment '年级',
    id_num         varchar(19) comment '身份证号'                            null,
    major          varchar(255),
    name           varchar(255),
    password       varchar(30) default '123456' comment '初始密码，默认是123456' not null,
    phone_num      varchar(11),
    school         varchar(255) comment '学院
可从学籍数据库调用',
    total_duration int         default 0,
    primary key (id)
) engine = InnoDB;
alter table activity
    add constraint FKsf1w5vswlmu7hq5cx8wmxlub3 foreign key (department) references department (id);
alter table activity_period
    add constraint FKptg4vppo98idc4h5gawmudg37 foreign key (parent) references activity_station (id);
alter table activity_station
    add constraint FK46dcjwcjpjkq62lnjbony635i foreign key (parent_activity) references activity (id);
alter table comment
    add constraint FKraqdqfcg5j5daco4ma3iv2v8l foreign key (activity) references activity (id);
alter table comment
    add constraint FKb8bhfe2og2c5v52l5w7lb1y50 foreign key (department) references department (id);
alter table comment
    add constraint FKk5dgrgaxq2cnqqo788r2gysxo foreign key (parent_comment) references comment (id);
alter table comment
    add constraint FKqeaqu3dhs9wmfy0y0j148k9eg foreign key (student) references student (id);
alter table image
    add constraint FKcq928mk18houvk5hsy7ske3ca foreign key (activity) references activity (id);
alter table image
    add constraint FK4rbv3fafist78pxjoly1b9e5j foreign key (comment) references comment (id);
alter table image
    add constraint FKhiuk4p68oep9m3yef6e1ish5x foreign key (department) references department (id);
alter table image
    add constraint FKklkraduqlebpw7otuyx8sepnj foreign key (student) references student (id);
alter table record
    add constraint FKee83lyereiq1c9l5ao65aedkc foreign key (the_period) references activity_period (id);
alter table record
    add constraint FK7kj4cl6ibmwih6l0wt7qaj1js foreign key (volunteer) references student (id);

INSERT INTO `voluntize`.`student` (`id`, `school`, `email`, `grade`, `class`, `major`, `name`, `total_duration`)
VALUES ('120181080701', '控计学院', '1201811080701@ncepu.edu.cn', '大二', '软件1802', '软件工程', '邵博深', 30);
INSERT INTO `voluntize`.`student` (`id`, `school`, `email`, `grade`, `class`, `id_num`, `major`, `name`)
VALUES ('120171020201', '电气学院', 'Macswelle@outlook.com', '大三', '电气1710', '341003199908170034', '电气工程及其自动化', '葛翰臣');
INSERT INTO `voluntize`.`student` (`id`, `school`, `grade`, `class`, `major`, `name`)
VALUES ('120171020203', '控计学院', '大二', '信安1802', '信息安全', '谢沅伯');
INSERT INTO `voluntize`.`student` (`id`, `school`, `grade`, `class`, `major`, `name`)
VALUES ('120171020204', '控计学院', '大二', '计算1802', '计算机科学与技术', '许奕晞');

INSERT INTO `voluntize`.`department` (`id`, `name`)
VALUES ('6177001', '图书馆（主）');
INSERT INTO `voluntize`.`department` (`id`, `name`)
VALUES ('6177002', '图书馆（C）');
INSERT INTO `voluntize`.`department` (`id`, `name`)
VALUES ('6177003', '教务处');
INSERT INTO `voluntize`.`department` (`id`, `name`)
VALUES ('6177004', '财务处');
