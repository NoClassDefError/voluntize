package cn.ncepu.voluntize.service.userImpl;

import cn.ncepu.voluntize.entity.Student;
import cn.ncepu.voluntize.repository.DepartmentRepository;
import cn.ncepu.voluntize.repository.StudentRepository;
import cn.ncepu.voluntize.service.LoginService;
import cn.ncepu.voluntize.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginImpl implements LoginService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    public boolean login(LoginVo user) {
        Optional<Student> optional1 = studentRepository.findById(user.getId());
        if (optional1.isPresent()) if (user.getPassword().equals(optional1.get().getPassword())) return true;
        return departmentRepository.findById(user.getId()).filter(department -> user.getPassword().equals(department.getPassword())).isPresent();
    }
}
