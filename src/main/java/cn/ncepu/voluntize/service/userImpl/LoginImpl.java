package cn.ncepu.voluntize.service.userImpl;

import cn.ncepu.voluntize.entity.Department;
import cn.ncepu.voluntize.entity.Student;
import cn.ncepu.voluntize.requestVo.LoginVo;
import cn.ncepu.voluntize.responseVo.UserInfoVo;
import cn.ncepu.voluntize.service.LoginService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginImpl extends BaseUserImpl implements LoginService {

    public UserInfoVo login(LoginVo user) {
        //判断是否是管理员
        if (user.getId().equals("admin") && user.getPassword().equals("adIl1Fun*&23hu)283"))
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

}
