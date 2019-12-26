package cn.ncepu.voluntize.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;

/**
 * <h2>学生实体类</h2>
 * 用于数据库关联查询，学生的部分数据从学校已有数据库导入
 *
 * @author Ge Hanchen
 * @since 0.0.1
 */
@Entity
@Data
public class Student {

    /**
     * 学号，唯一非空，外部导入，学生表主键
     */
    @Id
    private String studentNum;

    /**
     * 身份证号
     */
    private String idNum;

    /**
     * 密码，初始密码为身份证后六位
     */
    private String password;

    /**
     * 姓名
     */
    private String name;

    /**
     * 专业
     */
    private String major;

    /**
     * 年级
     */
    private String grade;

    /**
     * 该学生已报名
     */
    private ArrayList<Activity> activities;



}
