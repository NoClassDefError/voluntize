package cn.ncepu.voluntize.controller.student;

import cn.ncepu.voluntize.service.UpdateUserService;
import cn.ncepu.voluntize.vo.requestVo.StudentUpdateVo;
import cn.ncepu.voluntize.vo.responseVo.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/student")
public class StudentUpdate {
    @Autowired
    UpdateUserService updateUserService;

    /**
     * @param studentUpdateVo update
     * @return json 修改是否成功
     */
    @RequestMapping(value = "/updateStu", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult updateStudent(StudentUpdateVo studentUpdateVo) {
        if (updateUserService.updateStudent(studentUpdateVo)) return new HttpResult("updateResult:success");
        else return new HttpResult("updateResult:error");
    }


}
