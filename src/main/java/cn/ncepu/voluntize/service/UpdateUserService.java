package cn.ncepu.voluntize.service;

import cn.ncepu.voluntize.vo.requestVo.DepartmentUpdateVo;
import cn.ncepu.voluntize.vo.requestVo.StudentUpdateVo;
import cn.ncepu.voluntize.vo.requestVo.UserUpdateVoAdmin;

public interface UpdateUserService {
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

    //下面三个由管理员接口调用

    String updateStudent(UserUpdateVoAdmin voAdmin);

    String updateDepartment(UserUpdateVoAdmin voAdmin);

    String deleteUser(String id);
}
