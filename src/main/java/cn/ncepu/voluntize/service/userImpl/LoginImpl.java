package cn.ncepu.voluntize.service.userImpl;

import cn.ncepu.voluntize.entity.Department;
import cn.ncepu.voluntize.entity.Student;
import cn.ncepu.voluntize.repository.RecordRepository;
import cn.ncepu.voluntize.service.LoginService;
import cn.ncepu.voluntize.vo.requestVo.LoginVo;
import cn.ncepu.voluntize.vo.responseVo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RefreshScope
@Service
@CacheConfig(cacheNames = "loginService")
public class LoginImpl extends BaseUserImpl implements LoginService {

    @Value("${application.adminPassword}")
    private String adminPassword;

    public UserInfoVo login(LoginVo user) {
        //判断是否是管理员
        if ("admin".equals(user.getId()) && adminPassword.equals(user.getPassword()))
            return new UserInfoVo(0, null, null);
        //判断用户身份
        Optional<Student> optional1 = studentRepository.findById(user.getId());
        Optional<Department> optional2 = departmentRepository.findById(user.getId());
        if (optional1.isPresent()) if (user.getPassword().equals(optional1.get().getPassword())) {
            Student student = optional1.get();
            Integer a = recordRepository.getTheDuration(student.getStudentNum());
//            System.out.println(recordRepository.getTheDuration(student.getStudentNum()));
            if (a == null) a = 0;
            student.setTotalDuration(a);
            studentRepository.updateTotalDuration(user.getId(), a);
            return new UserInfoVo(1, student, null);
        }
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
        return optional1.map(student -> {
            Integer a = recordRepository.getTheDuration(student.getStudentNum());
            if (a == null) a = 0;
            student.setTotalDuration(a);
//            System.out.println(a);
            studentRepository.updateTotalDuration(userId, a);
            return new UserInfoVo(1, student, null);
        }).orElseGet(() -> optional2.map(department -> new UserInfoVo(2, null, department))
                .orElseGet(() -> new UserInfoVo(-1, null, null)));
    }

    @Autowired
    private RecordRepository recordRepository;

    @Override
    public UserInfoVoAdmin findUser(String userId) {
        Optional<Student> optional1 = studentRepository.findById(userId);
        Optional<Department> optional2 = departmentRepository.findById(userId);
        return optional1.map(student -> new UserInfoVoAdmin(1, student, null))
                .orElseGet(() -> optional2.map(department -> new UserInfoVoAdmin(2, null, department))
                        .orElseGet(() -> new UserInfoVoAdmin(-1, null, null)));
    }

    @Override
    public List<StudentExcelVo> findAllStu() {
        List<StudentExcelVo> studentVos = new ArrayList<>();
        for (Student student : studentRepository.findAll()) studentVos.add(new StudentExcelVo(student));
        return studentVos;
    }

    @Override
    public List<DepartmentExcelVo> findAllDep() {
        return departmentRepository.getDepartmentExcelVo();
    }
}
