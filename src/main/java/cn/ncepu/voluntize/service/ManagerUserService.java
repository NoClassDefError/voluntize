package cn.ncepu.voluntize.service;

import cn.ncepu.voluntize.entity.Department;
import cn.ncepu.voluntize.entity.Student;

import java.util.ArrayList;

/**
 * 管理员专享权限
 */
public interface ManagerUserService {
    /**
     * id为新值则为添加，id为已有的值则为更新
     */
    void updateStudent(Student student);
    void updateDepartment(Department department);
    void deleteStudent(String id);
    void deleteDepartment(String id);
    ArrayList<Student> findAllStudent();
    ArrayList<Department> findAllDepartment();
}
