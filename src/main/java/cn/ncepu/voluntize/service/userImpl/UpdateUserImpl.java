package cn.ncepu.voluntize.service.userImpl;

import cn.ncepu.voluntize.entity.Department;
import cn.ncepu.voluntize.entity.Image;
import cn.ncepu.voluntize.entity.Student;
import cn.ncepu.voluntize.service.UpdateUserService;
import cn.ncepu.voluntize.vo.requestVo.DepartmentUpdateVo;
import cn.ncepu.voluntize.vo.ImageVo;
import cn.ncepu.voluntize.vo.requestVo.StudentUpdateVo;
import cn.ncepu.voluntize.vo.requestVo.UserUpdateVoAdmin;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UpdateUserImpl extends BaseUserImpl implements UpdateUserService {

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
        newStu.setName(student.getName());
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

        studentRepository.save(newStu);
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
    @Override
    public String updateStudent(UserUpdateVoAdmin voAdmin) {
        //department表中的id不能与student的重复
        if (departmentRepository.findById(voAdmin.getId()).isPresent())
            return "A student's id cannot be the same as a department's.";
        Student student = new Student();
        student.setStudentNum(voAdmin.getId());
        student.setName(voAdmin.getName());
        student.setGrade(voAdmin.getGrade());
        student.setGender(voAdmin.getGender());
        student.setClasss(voAdmin.getClasss());
        student.setIdNum(voAdmin.getIdNum());
        student.setMajor(voAdmin.getMajor());
        student.setSchool(voAdmin.getSchool());
        if (voAdmin.getPassword() == null) student.setPassword("123456");
        else student.setPassword(voAdmin.getPassword());
        studentRepository.save(student);
        return "success";
    }

    @Override
    public String updateDepartment(UserUpdateVoAdmin voAdmin) {
        if (studentRepository.findById(voAdmin.getId()).isPresent())
            return "A department's id cannot be the same as a student's.";
        Department department = new Department();
        department.setId(voAdmin.getId());
        department.setManager(voAdmin.getManager());
        department.setName(voAdmin.getName());
        if (voAdmin.getPassword() == null) department.setPassword("123456");
        else department.setPassword(voAdmin.getPassword());
        departmentRepository.save(department);
        return "success";
    }

    @Override
    public String deleteUser(String id) {
        if (studentRepository.findById(id).isPresent()) {
            studentRepository.deleteById(id);
            return "A student account deleted.";
        }
        if (departmentRepository.findById(id).isPresent()) {
            departmentRepository.deleteById(id);
            return "A department account deleted.";
        }
        return "Id not found.";
    }
}
