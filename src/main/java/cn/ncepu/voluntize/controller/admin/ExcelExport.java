package cn.ncepu.voluntize.controller.admin;

import cn.ncepu.voluntize.entity.Student;
import cn.ncepu.voluntize.repository.StudentRepository;
import cn.ncepu.voluntize.util.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/admin/excel")
public class ExcelExport {

    @Autowired
    private StudentRepository studentRepository;

    @RequestMapping("/students")
    public void exportStudents(HttpServletRequest request, HttpServletResponse response) {
        List<Student> students = studentRepository.findAll();
        ExcelUtils<Student> utils = new ExcelUtils<>();
        utils.exportExcel(new String[]{"", "", ""}, students, "所有学生", response);
    }


}
