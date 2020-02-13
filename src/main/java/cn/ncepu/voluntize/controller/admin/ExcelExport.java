package cn.ncepu.voluntize.controller.admin;

import cn.ncepu.voluntize.service.LoginService;
import cn.ncepu.voluntize.util.ExcelUtils;
import cn.ncepu.voluntize.vo.responseVo.StudentVo;
import cn.ncepu.voluntize.vo.responseVo.UserInfoVoAdmin;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @RequestMapping("/students")
    public void exportStudents(HttpServletResponse response) throws IOException {
        String fileName = "students_all";
        List<StudentVo> students = loginService.findAllStu();
        ExcelUtils<StudentVo> utils = new ExcelUtils<StudentVo>(){};
        XSSFWorkbook workbook = utils.exportExcel(students, fileName);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition",
                "attachment;fileName=" + URLEncoder.encode(fileName + ".xlsx", "UTF-8"));
        utils.writeToStream(workbook, response.getOutputStream());
    }

    @RequestMapping("/search")
    @ResponseBody
    public UserInfoVoAdmin searchUser(String id) {
        return loginService.findUser(id);
    }
}
