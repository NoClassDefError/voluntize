
create table department
(
    id        varchar(255)                 not null comment '部门账号'
        primary key,
    email     varchar(255)                 null comment '该部门管理者的邮箱
用于密码找回',
    manager   varchar(255)                 null comment '管理者姓名',
    name      varchar(255)                 null comment '部门名',
    password  varchar(30) default '123456' not null comment '初始密码，默认是123456',
    phone_num varchar(11)                  null comment '该部门负责管理公益劳动相关事宜的老师的手机号
也可以是部门电话号'
);

create table activity
(
    id          varchar(255)  not null
        primary key,
    description text          null,
    name        varchar(255)  null,
    semester    varchar(255)  null comment '学期，例如：“2019-2020学年第一学期”',
    status_id   int default 0 null comment '0 部门已发送等待审核
1 审核并修改
2 报名
3 录用并开始活动
4 结束并评价',
    department  varchar(255)  null comment '部门账号',
    constraint FKsf1w5vswlmu7hq5cx8wmxlub3
        foreign key (department) references department (id)
);

create table activity_station
(
    id              varchar(255) not null
        primary key,
    description     text         null,
    linkman         varchar(255) null comment '联系人，每个地点的联系人不一定一样',
    station_name    varchar(255) null,
    phone_num       varchar(255) null comment '联系人电话',
    parent_activity varchar(255) null,
    constraint FK46dcjwcjpjkq62lnjbony635i
        foreign key (parent_activity) references activity (id)
);

create table activity_period
(
    id              varchar(255)                        not null
        primary key,
    amount_required int                                 null comment '所需人数',
    end_date        timestamp default CURRENT_TIMESTAMP null,
    equ_duration    int                                 null comment '等效公益劳动时长
不是简单的末初时间的差，需要单独给定',
    requirements    text                                null,
    start_date      timestamp default CURRENT_TIMESTAMP null comment '本项公益劳动开始的日期',
    `period`        text                                null comment '每天活动时间安排，例如上午8至11，下午2至5',
    parent          varchar(255)                        null,
    constraint FKptg4vppo98idc4h5gawmudg37
        foreign key (parent) references activity_station (id)
);

create table student
(
    id             varchar(25)                  not null comment '学号，主键'
        primary key,
    class          varchar(45)                  null comment '班级
可从学籍数据库调用',
    email          varchar(255)                 null comment '联系邮箱用于密码找回',
    grade          varchar(45)                  null comment '年级',
    id_num         varchar(19)                  null comment '身份证号',
    major          varchar(255)                 null,
    name           varchar(255)                 null,
    password       varchar(30) default '123456' not null comment '初始密码，默认是123456',
    phone_num      varchar(11)                  null,
    school         varchar(255)                 null comment '学院
可从学籍数据库调用',
    total_duration int         default 0        null,
    gender         varchar(10)                  null
);

create table comment
(
    id             varchar(255)                        not null
        primary key,
    description    text                                null,
    time           timestamp default CURRENT_TIMESTAMP null,
    activity       varchar(255)                        not null,
    department     varchar(255)                        null comment '部门账号',
    parent_comment varchar(255)                        null,
    student        varchar(25)                         null comment '学号，主键',
    constraint FKb8bhfe2og2c5v52l5w7lb1y50
        foreign key (department) references department (id),
    constraint FKk5dgrgaxq2cnqqo788r2gysxo
        foreign key (parent_comment) references comment (id),
    constraint FKqeaqu3dhs9wmfy0y0j148k9eg
        foreign key (student) references student (id),
    constraint FKraqdqfcg5j5daco4ma3iv2v8l
        foreign key (activity) references activity (id)
);

create table image
(
    id         varchar(255) not null
        primary key,
    name       varchar(255) null comment '图片描述，即html image标签的alt属性',
    url        text         null,
    activity   varchar(255) null,
    comment    varchar(255) null,
    department varchar(255) null comment '部门账号',
    student    varchar(25)  null comment '学号，主键',
    constraint FK4rbv3fafist78pxjoly1b9e5j
        foreign key (comment) references comment (id),
    constraint FKcq928mk18houvk5hsy7ske3ca
        foreign key (activity) references activity (id),
    constraint FKhiuk4p68oep9m3yef6e1ish5x
        foreign key (department) references department (id),
    constraint FKklkraduqlebpw7otuyx8sepnj
        foreign key (student) references student (id)
);

create table record
(
    id          varchar(255)  not null
        primary key,
    audit_level int           null comment '公益劳动成绩 ：
0  不通过，若不通过，给评级部门必须填写不通过的理由，见evaluation；
1  通过；
2  良好；
3  优秀。',
    comments    text          null comment '公益劳动结束后，
参加该公益劳动的学生对所参加的活动的文字评价,
初始值置空',
    evaluation  varchar(255)  null comment '0  不通过，见audit_level；
若不通过，给评级部门必须填写不通过的理由（不超过255字）；',
    info        varchar(255)  null,
    is_passed   tinyint(1)    null comment '非0：已被录取（true）；
0：非录取状态（false）',
    stars       int default 0 not null comment '公益劳动结束后，
学生反馈评星
（0，1，2，3，4，5）
默认未评星（0）',
    status_id   int default 0 not null comment '标志任一公益劳动项目所处状态
',
    the_period  varchar(255)  null,
    volunteer   varchar(25)   null comment '学号，主键',
    constraint FK7kj4cl6ibmwih6l0wt7qaj1js
        foreign key (volunteer) references student (id),
    constraint FKee83lyereiq1c9l5ao65aedkc
        foreign key (the_period) references activity_period (id)
);

