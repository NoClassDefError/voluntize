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
public class Student implements Cloneable {

    /**
     * 学号，唯一非空，外部导入，学生表主键
     */
    @Id
    @Column(name = "id", nullable = false)
    private String studentNum;

    /**
     * 身份证号
     */
    @Basic
    @Column(name = "id_num", length = 19)
    private String idNum;

    /**
     * 密码，初始密码为身份证后六位
     */
    @Basic
    @Column(name = "password", length = 30)
    private String password;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "major")
    private String major;

    @Basic
    @Column(name = "grade")
    private String grade;

    @Basic
    @Column(name = "phone_num", length = 11)
    private String phoneNum;

    @Basic
    @Column(name = "email", length = 50)
    private String email;

    /**
     * 志愿总时长
     */
    @Basic
    @Column(name = "total_duration")
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
