package cn.ncepu.voluntize.service.userImpl;

import cn.ncepu.voluntize.entity.Department;
import cn.ncepu.voluntize.entity.Image;
import cn.ncepu.voluntize.entity.Student;
import cn.ncepu.voluntize.repository.DepartmentRepository;
import cn.ncepu.voluntize.repository.StudentRepository;
import cn.ncepu.voluntize.service.UpdateUserService;
import cn.ncepu.voluntize.requestVo.DepartmentUpdateVo;
import cn.ncepu.voluntize.requestVo.ImageVo;
import cn.ncepu.voluntize.requestVo.StudentUpdateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class UpdateUserImpl implements UpdateUserService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private HttpSession session;

    /**
     * 其中不仅要修改学生进本信息，还要修改图片
     *
     * @param student StudentUpdateVo
     * @return 修改是否成功
     */
    @Override
    public boolean updateStudent(StudentUpdateVo student) {
        //从session中获取学生学号信息
        String studentId = (String) session.getAttribute("UserId");
        Optional<Student> optional = studentRepository.findById(studentId);
        if (!optional.isPresent()) return false;
        //先对vo中的信息进行检验
        Student origin = optional.get();
        Student newStu = (Student) origin.clone();

        //设定基本信息
        newStu.setStudentNum(studentId);
        newStu.setEmail(student.getEmail());
        newStu.setMajor(student.getName());
        newStu.setGrade(student.getPhoneNum());

        //构造并设定image对象
        ArrayList<Image> images = new ArrayList<>();
        for (ImageVo image : student.getProfiles()) {
            Image image1 = image.toImage();
            image1.setStudent(origin);
            images.add(image1);
        }
        newStu.setProfiles(images);
        //其他信息保持不变

        return true;
    }

    /**
     * 修改部门信息，与上个方法基本相同
     * @param department DepartmentUpdateVo
     * @return 修改是否成功
     */
    @Override
    public boolean updateDepartment(DepartmentUpdateVo department) {
        String depId = (String)session.getAttribute("UserId");
        Optional<Department> optional = departmentRepository.findById(depId);
        if (!optional.isPresent()) return false;
        Department origin = optional.get();
        Department newDep = (Department) origin.clone();
        newDep.setId(depId);
        newDep.setManager(department.getManager());
        newDep.setPhoneNum(department.getPhoneNum());
        ArrayList<Image> images = new ArrayList<>();
        for (ImageVo image : department.getImages()) {
            Image image1 = image.toImage();
            image1.setDepartment(origin);
            images.add(image1);
        }
        newDep.setImages(images);
        return true;
    }
}
