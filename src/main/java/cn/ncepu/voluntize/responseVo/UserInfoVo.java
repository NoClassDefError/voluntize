package cn.ncepu.voluntize.responseVo;

import cn.ncepu.voluntize.entity.Department;
import cn.ncepu.voluntize.entity.Student;
import lombok.Data;

/**
 * 用于登录时返回用户信息
 */
@Data
public class UserInfoVo {
    /**
     * -1-登录失败 0-管理员 1-学生 2-部门
     */
    public int userCategory = -1;
    public Student student = null;
    public Department department = null;

    public UserInfoVo(int userCategory, Student student, Department department) {
        this.department = department;
        this.userCategory = userCategory;
        this.student = student;
    }
}
