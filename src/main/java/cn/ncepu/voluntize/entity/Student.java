package cn.ncepu.voluntize.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * <h2>学生实体类</h2>
 * 用于数据库关联查询，学生的部分数据从学校已有数据库导入
 *
 * @author Ge Hanchen
 * @since 0.0.1
 */
@Entity
@Data
@Table(name = "student")
public class Student implements Cloneable {

    /**
     * 学号，唯一非空，外部导入，学生表主键
     */
    @Id
    @Column(name = "id", nullable = false,columnDefinition = "varchar(25) comment '学号，主键'")
    private String studentNum;

    /**
     * 身份证号
     */
    @Basic
    @Column(name = "id_num", length = 19,columnDefinition = "varchar(19) null comment '身份证号'")
    private String idNum;

    /**
     * 密码，初始密码为身份证后六位
     */
    @Basic
    @Column(name = "password", length = 30, nullable = false,columnDefinition = " varchar(30) default '123456' comment '初始密码，默认是123456'")
    private String password;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "major")
    private String major;

    @Basic
    @Column(name = "grade", columnDefinition = "varchar(45) comment '年级'")
    private String grade;

    @Basic
    @Column(name = "class",columnDefinition = "varchar(45) comment '班级\n" +
            "可从学籍数据库调用'")
    private String classs;

    @Basic
    @Column(name = "phone_num", length = 11)
    private String phoneNum;

    @Basic
    @Column(name = "email", columnDefinition = "varchar(255) comment '联系邮箱用于密码找回'")
    private String email;

    @Basic
    @Column(name = "school", columnDefinition = "varchar(255) comment '学院\n" +
            "可从学籍数据库调用'")
    private String school;

    /**
     * 志愿总时长
     */
    @Basic
    @Column(name = "total_duration", columnDefinition = "int default 0")
    private int totalDuration;

    /**
     * 志愿记录，一对多关联ActivityPeriod。<br>
     * 报名则计入志愿记录，但未必报名成功。
     */
    @OneToMany(mappedBy = "volunteer", targetEntity = Record.class)
    private List<Record> participated;

    @OneToMany(mappedBy = "student", targetEntity = Comment.class)
    private List<Comment> comments;

    /**
     * 头像
     */
    @OneToMany(mappedBy = "student", targetEntity = Image.class)
    private List<Image> profiles;

    @Override
    public Object clone() {
        Student stu = null;
        try {
            stu = (Student) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return stu;
    }
}
