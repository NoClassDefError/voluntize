alter table activity
    drop constraint FKsf1w5vswlmu7hq5cx8wmxlub3
alter table activity_period
    drop constraint FKptg4vppo98idc4h5gawmudg37
alter table activity_station
    drop constraint FK46dcjwcjpjkq62lnjbony635i
alter table activity_station_periods
    drop constraint FKqy8cwihr30s9b4aj4iiura6ki
alter table activity_station_periods
    drop constraint FKiqafoe55qeay4e1l1bvinilg3
alter table comment
    drop constraint FKraqdqfcg5j5daco4ma3iv2v8l
alter table comment
    drop constraint FKb8bhfe2og2c5v52l5w7lb1y50
alter table comment
    drop constraint FKk5dgrgaxq2cnqqo788r2gysxo
alter table comment
    drop constraint FKqeaqu3dhs9wmfy0y0j148k9eg
alter table comment_son_comment
    drop constraint FK1632mbx4f81ej3vyergnefqhe
alter table comment_son_comment
    drop constraint FKmpbqnkc1ph6nchx6l4y4eud6y
alter table image
    drop constraint FKcq928mk18houvk5hsy7ske3ca
alter table image
    drop constraint FK4rbv3fafist78pxjoly1b9e5j
alter table image
    drop constraint FKhiuk4p68oep9m3yef6e1ish5x
alter table image
    drop constraint FKklkraduqlebpw7otuyx8sepnj
alter table record
    drop constraint FKee83lyereiq1c9l5ao65aedkc
alter table record
    drop constraint FK7kj4cl6ibmwih6l0wt7qaj1js
drop table activity
drop table activity_period
drop table activity_station
drop table activity_station_periods
drop table comment
drop table comment_son_comment
drop table department
drop table image
drop table record
drop table student
create table activity
(
    id          varchar(255) not null,
    description varchar(MAX),
    name        varchar(255),
    status_id   int,
    department  varchar(255),
    primary key (id)
)
create table activity_period
(
    id              varchar(255) not null,
    amount_required int,
    end_time        time,
    equ_duration    int,
    start_time      time,
    parent          varchar(255),
    primary key (id)
)
create table activity_station
(
    id              varchar(255) not null,
    description     varchar(255),
    name            varchar(255),
    station_name    varchar(255),
    requirements    varchar(255),
    parent_activity varchar(255),
    primary key (id)
)
create table activity_station_periods
(
    activity_station_id varchar(255) not null,
    periods_id          varchar(255) not null
)
create table comment
(
    id             varchar(255) not null,
    description    varchar(MAX),
    time           time,
    activity       varchar(255),
    department     varchar(255),
    parent_comment varchar(255),
    student        varchar(255),
    primary key (id)
)
create table comment_son_comment
(
    comment_id     varchar(255) not null,
    son_comment_id varchar(255) not null
)
create table department
(
    id        varchar(255) not null,
    manager   varchar(255),
    name      varchar(255),
    password  varchar(30),
    phone_num varchar(11),
    primary key (id)
)
create table image
(
    id         varchar(255) not null,
    name       varchar(255),
    url        varchar(255),
    activity   varchar(255),
    comment    varchar(255),
    department varchar(255),
    student    varchar(255),
    primary key (id)
)
create table record
(
    id          varchar(255) not null,
    audit_level int,
    is_passed   bit,
    status_id   int,
    the_period  varchar(255),
    volunteer   varchar(255),
    primary key (id)
)
create table student
(
    id             varchar(255) not null,
    email          varchar(50),
    grade          varchar(255),
    id_num         varchar(19),
    major          varchar(255),
    name           varchar(255),
    password       varchar(30),
    phone_num      varchar(11),
    total_duration int,
    primary key (id)
)
alter table activity_station_periods
    add constraint UK_1jb0okc3y6k4ktl2d1fs34u9c unique (periods_id)
alter table comment_son_comment
    add constraint UK_tr0t146nebkn67r133r8g2xwu unique (son_comment_id)
alter table activity
    add constraint FKsf1w5vswlmu7hq5cx8wmxlub3 foreign key (department) references department
alter table activity_period
    add constraint FKptg4vppo98idc4h5gawmudg37 foreign key (parent) references activity_station
alter table activity_station
    add constraint FK46dcjwcjpjkq62lnjbony635i foreign key (parent_activity) references activity
alter table activity_station_periods
    add constraint FKqy8cwihr30s9b4aj4iiura6ki foreign key (periods_id) references activity_period
alter table activity_station_periods
    add constraint FKiqafoe55qeay4e1l1bvinilg3 foreign key (activity_station_id) references activity_station
alter table comment
    add constraint FKraqdqfcg5j5daco4ma3iv2v8l foreign key (activity) references activity
alter table comment
    add constraint FKb8bhfe2og2c5v52l5w7lb1y50 foreign key (department) references department
alter table comment
    add constraint FKk5dgrgaxq2cnqqo788r2gysxo foreign key (parent_comment) references comment
alter table comment
    add constraint FKqeaqu3dhs9wmfy0y0j148k9eg foreign key (student) references student
alter table comment_son_comment
    add constraint FK1632mbx4f81ej3vyergnefqhe foreign key (son_comment_id) references comment
alter table comment_son_comment
    add constraint FKmpbqnkc1ph6nchx6l4y4eud6y foreign key (comment_id) references comment
alter table image
    add constraint FKcq928mk18houvk5hsy7ske3ca foreign key (activity) references activity
alter table image
    add constraint FK4rbv3fafist78pxjoly1b9e5j foreign key (comment) references comment
alter table image
    add constraint FKhiuk4p68oep9m3yef6e1ish5x foreign key (department) references department
alter table image
    add constraint FKklkraduqlebpw7otuyx8sepnj foreign key (student) references student
alter table record
    add constraint FKee83lyereiq1c9l5ao65aedkc foreign key (the_period) references activity_period
alter table record
    add constraint FK7kj4cl6ibmwih6l0wt7qaj1js foreign key (volunteer) references student