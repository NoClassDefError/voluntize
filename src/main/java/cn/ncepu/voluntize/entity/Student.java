package cn.ncepu.voluntize.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cache;
import org.springframework.cache.annotation.CacheConfig;

import javax.persistence.*;
import java.util.List;

/**
 * <h2>学生实体类</h2>
 * 用于数据库关联查询，学生的部分数据从学校已有数据库导入
 *
 * @author Ge Hanchen
 * @since 2019.12
 */
@Entity
@Data
@Table(name = "student")
@ToString(exclude = {"profiles", "comments", "participated"})
@JsonIgnoreProperties({"profiles", "comments", "participated"})
public class Student implements Cloneable {

    /**
     * 学号，唯一非空，外部导入，学生表主键
     */
    @Id
    @Column(name = "id", nullable = false, length = 25)
    private String studentNum;

    /**
     * 身份证号
     */
//    @Basic
//    @Column(name = "id_num", length = 19, columnDefinition = "varchar(19) null")
//    private String idNum;

    @Basic
    @Column(name = "gender", length = 10)
    private String gender;

    /**
     * 密码，初始密码为"123456"
     */
    @Basic
    @Column(name = "password", length = 30, nullable = false, columnDefinition = " varchar(30) default '123456'")
    private String password = "123456";

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "major")
    private String major;

    @Basic
    @Column(name = "grade")
    private Integer grade;

    @Basic
    @Column(name = "class", length = 45)
    private String classs;

    @Basic
    @Column(name = "phone_num", length = 11)
    private String phoneNum;

    @Basic
    @Column(name = "email")
    private String email;

    @Basic
    @Column(name = "school")
    private String school;

    /**
     * 志愿总时长
     */
    @Basic
    @Column(name = "total_duration", columnDefinition = "integer default 0")
    private int totalDuration = 0;

    /**
     * 志愿记录，一对多关联ActivityPeriod。<br>
     * 报名则计入志愿记录，但未必报名成功。
     */
    @OneToMany(mappedBy = "volunteer", targetEntity = Record.class, cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<Record> participated;

    @OneToMany(mappedBy = "student", targetEntity = Comment.class, cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Comment> comments;

    /**
     * 头像
     */
    @OneToMany(mappedBy = "student", targetEntity = Image.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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
