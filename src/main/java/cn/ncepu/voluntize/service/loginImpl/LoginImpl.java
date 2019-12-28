package cn.ncepu.voluntize.service.loginImpl;

import cn.ncepu.voluntize.entity.Student;
import cn.ncepu.voluntize.repository.DepartmentRepository;
import cn.ncepu.voluntize.repository.StudentRepository;
import cn.ncepu.voluntize.vo.LoginVo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginImpl {
    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;

    public LoginImpl(StudentRepository studentRepository, DepartmentRepository departmentRepository) {
        this.studentRepository = studentRepository;
        this.departmentRepository = departmentRepository;
    }

    public boolean login(LoginVo user) {
        Optional<Student> optional1 = studentRepository.findById(user.getId());
        if (optional1.isPresent()) if (user.getPassword().equals(optional1.get().getPassword())) return true;
        return departmentRepository.findById(user.getId()).filter(department -> user.getPassword().equals(department.getPassword())).isPresent();
    }
}
