package cn.ncepu.voluntize.vo.responseVo;

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
    public StudentVo student = null;
    public DepartmentVo department = null;

    public UserInfoVo(int userCategory, Student student, Department department) {
        this.student = student == null ? null : new StudentVo(student);
        this.department = department == null ? null : new DepartmentVo(department);
        this.userCategory = userCategory;
    }
}
