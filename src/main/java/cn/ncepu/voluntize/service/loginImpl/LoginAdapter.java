package cn.ncepu.voluntize.service.loginImpl;

import cn.ncepu.voluntize.repository.DepartmentRepository;
import cn.ncepu.voluntize.repository.StudentRepository;
import cn.ncepu.voluntize.service.UserService;
import cn.ncepu.voluntize.vo.StudentUpdateVo;

/**
 * 将UserService分开实现，使用类的适配器模式
 */
public class LoginAdapter extends LoginImpl implements UserService  {
    public LoginAdapter(StudentRepository studentRepository, DepartmentRepository departmentRepository) {
        super(studentRepository, departmentRepository);
    }

    @Override
    public boolean updateStudent(StudentUpdateVo student) {
        return false;
    }
}
