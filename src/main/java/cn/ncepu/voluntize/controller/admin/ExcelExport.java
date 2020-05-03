package cn.ncepu.voluntize.controller.admin;

import cn.ncepu.voluntize.service.LoginService;
import cn.ncepu.voluntize.util.ExcelUtils;
import cn.ncepu.voluntize.vo.responseVo.DepartmentExcelVo;
import cn.ncepu.voluntize.vo.responseVo.StudentExcelVo;
import cn.ncepu.voluntize.vo.responseVo.UserInfoVoAdmin;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/admin/excel")
public class ExcelExport {

    @Autowired
    private LoginService loginService;

    @RequestMapping("/stu")
    public void exportStudents(@RequestParam Integer grade, HttpServletResponse response) throws IOException {
        String fileName;
        List<StudentExcelVo> students = loginService.findStuExcel(grade);
        if (grade == null) fileName = "students_all";
        else fileName = "student_" + grade;

        ExcelUtils<StudentExcelVo> utils = new ExcelUtils<StudentExcelVo>() {
        };//这个花括号不能少，否则获取不到泛型
        XSSFWorkbook workbook = utils.exportExcel(students, fileName);
//        File file = new File("C:\\Users\\DELL\\Desktop\\" + fileName + ".xlsx");
//        if (file.createNewFile()) {
//            FileOutputStream fileOutputStream = new FileOutputStream(file);
//            utils.writeToStream(workbook, fileOutputStream);
//        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition",
                "attachment;fileName=" + URLEncoder.encode(fileName + ".xlsx", "UTF-8"));
        utils.writeToStream(workbook, response.getOutputStream());
    }

    @RequestMapping("/dep")
    @ResponseBody
    public List<DepartmentExcelVo> exportDepartments() {
//        String fileName = "departments_all";
        return loginService.findDepExcel();
//        return null;
//        ExcelUtils<DepartmentVo> utils = new ExcelUtils<DepartmentVo>() {
//        };//这个花括号不能少，否则获取不到泛型
//        XSSFWorkbook workbook = utils.exportExcel(students, fileName);
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("multipart/form-data");
//        response.setHeader("Content-Disposition",
//                "attachment;fileName=" + URLEncoder.encode(fileName + ".xlsx", "UTF-8"));
//        utils.writeToStream(workbook, response.getOutputStream());
    }

    @RequestMapping("/search")
    @ResponseBody
    public UserInfoVoAdmin searchUser(String id) {
        return loginService.findUser(id);
    }
}
