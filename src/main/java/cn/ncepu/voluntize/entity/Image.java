package cn.ncepu.voluntize.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * <h2>图片实体类</h2>
 * 储存各种图像的url，包括学生头像，部门头像，活动图像，评论图片等。<br>
 * 多对一关联Student, Activity, Department, Comment类
 *
 * @author Ge Hanchen
 * @since 0.0.1
 */
@Data
@Entity
public class Image {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id")
    private String id;

    @Basic
    @Column(name = "url")
    private String url;

    @Basic
    @Column(name = "name", columnDefinition = "varchar(255) comment '图片描述，即html image标签的alt属性'")
    private String name;

    @ManyToOne(targetEntity = Student.class)
    @JoinColumn(name = "student", referencedColumnName = "id")
    private Student student;

    @ManyToOne(targetEntity = Activity.class)
    @JoinColumn(name = "activity", referencedColumnName = "id")
    private Activity activity;

    @ManyToOne(targetEntity = Department.class)
    @JoinColumn(name = "department", referencedColumnName = "id")
    private Department department;

    @ManyToOne(targetEntity = Comment.class)
    @JoinColumn(name = "comment", referencedColumnName = "id")
    private Comment comment;
}
