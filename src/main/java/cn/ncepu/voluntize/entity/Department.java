package cn.ncepu.voluntize.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

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
@ToString(exclude = {"images", "activities", "comments"})
@JsonIgnoreProperties({"images", "activities", "comments"})
public class Department {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "password", length = 30, nullable = false, columnDefinition = " varchar(30) default '123456'")
    private String password = "123456";

    /**
     * 该部门负责管理公益劳动相关事宜的老师的手机号
     */
    @Basic
    @Column(name = "phone_num", length = 11)
    private String phoneNum;

    /**
     * 该部门管理者的邮箱用于密码找回
     */
    @Basic
    @Column(name = "email")
    private String email;

    /**
     * 管理者姓名
     */
    @Basic
    @Column(name = "manager")
    private String manager;

    @Basic
    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    /**
     * 部门图册
     */
    @OneToMany(mappedBy = "department", targetEntity = Image.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Image> images;

    /**
     * 该部门发布的所有志愿活动信息
     */
    @OneToMany(mappedBy = "department", targetEntity = Activity.class, cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Activity> activities;

    @OneToMany(mappedBy = "department", targetEntity = Comment.class, cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
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
