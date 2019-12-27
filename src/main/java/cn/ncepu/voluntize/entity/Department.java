package cn.ncepu.voluntize.entity;

import lombok.Data;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;

/**
 * <h2>部门实体类</h2>
 *
 * @author Ge Hanchen
 * @since 0.0.1
 */
@Data
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
    @Column(name = "phoneNum", length = 11)
    private String phoneNum;

    @Basic
    @Column(name = "manager")
    private String manager;

    /**
     * 部门图册
     */
    @OneToMany(mappedBy = "department", targetEntity = Image.class)
    private ArrayList<Image> images;

    /**
     * 该部门发布的所有志愿活动信息
     */
    @OneToMany(mappedBy = "department", targetEntity = Activity.class)
    private ArrayList<Activity> activities;

    @OneToMany(mappedBy = "department", targetEntity = Comment.class)
    private ArrayList<Comment> comments;
}
