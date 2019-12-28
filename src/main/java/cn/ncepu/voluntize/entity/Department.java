package cn.ncepu.voluntize.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * <h2>部门实体类</h2>
 *
 * @author Ge Hanchen
 * @since 0.0.1
 */
@Data
@Entity
public class Department {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "password", length = 30)
    private String password;

    @Basic
    @Column(name = "phone_num", length = 11)
    private String phoneNum;

    @Basic
    @Column(name = "email", length = 50)
    private String email;

    @Basic
    @Column(name = "manager")
    private String manager;

    /**
     * 部门图册
     */
    @OneToMany(mappedBy = "department", targetEntity = Image.class)
    private List<Image> images;

    /**
     * 该部门发布的所有志愿活动信息
     */
    @OneToMany(mappedBy = "department", targetEntity = Activity.class)
    private List<Activity> activities;

    @OneToMany(mappedBy = "department", targetEntity = Comment.class)
    private List<Comment> comments;

    @Override
    public Object clone() {
        Department stu = null;
        try {
            stu = (Department) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return stu;
    }
}
