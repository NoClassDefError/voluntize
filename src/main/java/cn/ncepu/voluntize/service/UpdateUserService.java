package cn.ncepu.voluntize.service;

import cn.ncepu.voluntize.requestVo.DepartmentUpdateVo;
import cn.ncepu.voluntize.requestVo.StudentUpdateVo;

public interface UpdateUserService extends UserService {
    /**
     * 修改学生账户信息（不包括密码）
     * @param student StudentUpdateVo
     * @return 修改是否成功
     */
    boolean updateStudent(StudentUpdateVo student);

    /**
     * 修改部门账户信息（不包括密码）
     * @param department DepartmentUpdateVo
     * @return 修改是否成功
     */
    boolean updateDepartment(DepartmentUpdateVo department);
}
