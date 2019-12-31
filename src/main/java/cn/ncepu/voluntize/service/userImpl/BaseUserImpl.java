package cn.ncepu.voluntize.service.userImpl;

import cn.ncepu.voluntize.repository.DepartmentRepository;
import cn.ncepu.voluntize.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public abstract class BaseUserImpl {
    @Autowired
    protected StudentRepository studentRepository;
    @Autowired
    protected DepartmentRepository departmentRepository;
    @Autowired
    protected HttpSession session;
}
