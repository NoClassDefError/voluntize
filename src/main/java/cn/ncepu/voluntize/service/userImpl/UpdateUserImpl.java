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

        //设定基本信息
        origin.setStudentNum(studentId);
        origin.setEmail(student.getEmail());
        origin.setName(student.getName());
        origin.setPhoneNum(student.getPhoneNum());

        //构造并设定image对象
        if (student.getProfiles() != null) {
            ArrayList<Image> images = new ArrayList<>();
            for (ImageVo image : student.getProfiles()) {
                Image image1 = image.toImage();
                image1.setStudent(origin);
                images.add(image1);
            }
            origin.setProfiles(images);
        }
        //其他信息保持不变

        studentRepository.save(origin);
        return true;
    }

    /**
     * 部门本身的接口
     *
     * @param department DepartmentUpdateVo
     * @return 修改是否成功
     */
    @Override
    public boolean updateDepartment(DepartmentUpdateVo department) {
        String depId = (String) session.getAttribute("UserId");
        Optional<Department> optional = departmentRepository.findById(depId);
        if (!optional.isPresent()) return false;
        Department origin = optional.get();
        origin.setId(depId);
        origin.setManager(department.getManager());
        origin.setPhoneNum(department.getPhoneNum());
        origin.setEmail(department.getEmail());
        if (department.getImages() != null) {
            ArrayList<Image> images = new ArrayList<>();
            for (ImageVo image : department.getImages()) {
                Image image1 = image.toImage();
                image1.setDepartment(origin);
                images.add(image1);
            }
            origin.setImages(images);
        }
        departmentRepository.save(origin);
        return true;
    }

    /**
     * 管理员的修改接口
     */
    @Override
    public String updateStudent(UserUpdateVoAdmin voAdmin) {
        //department表中的id不能与student的重复
        if (departmentRepository.findById(voAdmin.getId()).isPresent())
            return "学生的id不能和部门的一样";
        Student student = new Student();
        student.setStudentNum(voAdmin.getId());
        student.setName(voAdmin.getName());
        student.setGrade(voAdmin.getGrade());
        student.setGender(voAdmin.getGender());
        student.setClasss(voAdmin.getClasss());
//        student.setIdNum(voAdmin.getIdNum());
        student.setMajor(voAdmin.getMajor());
        student.setSchool(voAdmin.getSchool());
        if (voAdmin.getPassword() == null) student.setPassword("123456");
        else student.setPassword(voAdmin.getPassword());
        return studentRepository.save(student).getStudentNum();
    }

    /**
     * 管理员的修改接口
     */
    @Override
    public String updateDepartment(UserUpdateVoAdmin voAdmin) {
        if (studentRepository.findById(voAdmin.getId()).isPresent())
            return "部门的id不能和学生的一样";
        Department department = new Department();
        department.setId(voAdmin.getId());
        department.setManager(voAdmin.getManager());
        department.setName(voAdmin.getName());
        if (voAdmin.getPassword() == null) department.setPassword("123456");
        else department.setPassword(voAdmin.getPassword());
        return departmentRepository.save(department).getId();
    }

    /**
     * 管理员的删除接口
     */
    @Override
    public String deleteUser(String id) {
        if (studentRepository.findById(id).isPresent()) {
            studentRepository.deleteById(id);
            return "学生账户" + id + "已删除";
        }
        if (departmentRepository.findById(id).isPresent()) {
            departmentRepository.deleteById(id);
            return "部门账户" + id + "已删除";
        }
        return "没有找到这个账户";
    }
}
