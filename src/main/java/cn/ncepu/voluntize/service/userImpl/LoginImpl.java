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

    public int login(LoginVo user) {
        //判断是否是管理员
        if (user.getId().equals("admin") && user.getPassword().equals("adIl1Fun*&23hu)283")) return 0;
        //判断是否是学生
        Optional<Student> optional1 = studentRepository.findById(user.getId());
        if (optional1.isPresent()) if (user.getPassword().equals(optional1.get().getPassword())) return 1;
        return departmentRepository.findById(user.getId()).filter(department -> user.getPassword().equals(department.getPassword())).isPresent() ? 2 : 0;
    }
}
