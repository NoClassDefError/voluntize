#新表结构：
#csv格式或txt格式导入这个表
create table student_import
(
    id     varchar(25) primary key,
    class  varchar(45),
    school varchar(255),
    grade  varchar(45),
    name   varchar(255)
);

# 手动同步表数据的步骤：
# 删除新表不存在的学生，尽量不要执行此句！
# 因为由于旧表部分学生与其它表关联，无法删除！
delete
from student
where !exists(select * from student_import where id = student.id);

# 增加新表独有的学生
insert into voluntize.student (id, class, name, school, grade)
select id, class, name, school, grade
from student_import a
where !exists(select * from student where id = a.id);

# 新表旧表的学生id相同了，更新数据
update voluntize.student
    right join student_import on (student.id = student_import.id)
set student.class=student_import.class,
    student.school=student_import.school,
    student.grade=student_import.grade,
    student.name=student_import.name;

# 不建议自动同步表数据：

# create trigger updateStu
#     after insert
#     on student_import
#     for each row
# begin
#     insert into voluntize.student (id, class, name, school, grade)
#     select id, class, name, school, grade
#     from student_import a
#     where !exists(select * from student where id = a.id);
#     update voluntize.student
#         inner join student_import on id = student_import.id
#     set student.class=student_import.class;
# end;
