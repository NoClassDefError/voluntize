package cn.ncepu.voluntize.service.userImpl;

import cn.ncepu.voluntize.entity.Department;
import cn.ncepu.voluntize.entity.Student;
import cn.ncepu.voluntize.vo.requestVo.LoginVo;
import cn.ncepu.voluntize.vo.responseVo.StudentVo;
import cn.ncepu.voluntize.vo.responseVo.UserInfoVo;
import cn.ncepu.voluntize.service.LoginService;
import cn.ncepu.voluntize.vo.responseVo.UserInfoVoAdmin;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LoginImpl extends BaseUserImpl implements LoginService {

    public UserInfoVo login(LoginVo user) {
        //判断是否是管理员
        if ("admin".equals(user.getId()) && "admin".equals(user.getPassword()))
            return new UserInfoVo(0, null, null);
        //判断用户身份
        Optional<Student> optional1 = studentRepository.findById(user.getId());
        Optional<Department> optional2 = departmentRepository.findById(user.getId());
        if (optional1.isPresent()) if (user.getPassword().equals(optional1.get().getPassword()))
            return new UserInfoVo(1, optional1.get(), null);
        if (optional2.isPresent()) if (user.getPassword().equals(optional2.get().getPassword()))
            return new UserInfoVo(2, null, optional2.get());
        return new UserInfoVo(-1, null, null);
    }

    @Override
    public UserInfoVo login(String userId) {
        //判断是否是管理员
        if ("admin".equals(userId))
            return new UserInfoVo(0, null, null);
        //判断用户身份
        Optional<Student> optional1 = studentRepository.findById(userId);
        Optional<Department> optional2 = departmentRepository.findById(userId);
        return optional1.map(student -> new UserInfoVo(1, student, null))
                .orElseGet(() -> optional2.map(department -> new UserInfoVo(2, null, department))
                        .orElseGet(() -> new UserInfoVo(-1, null, null)));
    }

    @Override
    public UserInfoVoAdmin findUser(String userId){
        Optional<Student> optional1 = studentRepository.findById(userId);
        Optional<Department> optional2 = departmentRepository.findById(userId);
        return optional1.map(student -> new UserInfoVoAdmin(1, student, null))
                .orElseGet(() -> optional2.map(department -> new UserInfoVoAdmin(2, null, department))
                        .orElseGet(() -> new UserInfoVoAdmin(-1, null, null)));
    }

    @Override
    public List<StudentVo> findAllStu(){
        List<StudentVo> studentVos = new ArrayList<>();
        for(Student student : studentRepository.findAll()) studentVos.add(new StudentVo(student));
        return studentVos;
    }
}
