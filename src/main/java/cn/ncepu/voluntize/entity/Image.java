package cn.ncepu.voluntize.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * <h2>图片实体类</h2>
 * 储存各种图像的url，包括学生头像，部门头像，活动图像，评论图片等。<br>
 * 多对一关联Student, Activity, Department, Comment类
 *
 * @author Ge Hanchen
 * @since 0.0.1
 */
@Data
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
    @Column(name = "name")
    private String name;

    private Student student;
    private Activity activity;
    private Department department;
    private Comment comment;
}
