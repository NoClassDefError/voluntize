package cn.ncepu.voluntize.controller;

import cn.ncepu.voluntize.service.UpdateUserService;
import cn.ncepu.voluntize.responseVo.HttpResult;
import cn.ncepu.voluntize.requestVo.DepartmentUpdateVo;
import cn.ncepu.voluntize.requestVo.StudentUpdateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdateInfo extends BaseController {
    @Autowired
    UpdateUserService updateUserService;

    /**
     * @param studentUpdateVo update
     * @return json 修改是否成功
     */
    @RequestMapping(value = "/updateStudent", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult updateStudent(StudentUpdateVo studentUpdateVo) {
        if (updateUserService.updateStudent(studentUpdateVo)) return new HttpResult("updateResult:success");
        else return new HttpResult("updateResult:error");
    }

    @RequestMapping(value="/updateDepartment",method = RequestMethod.POST)
    @ResponseBody
    public HttpResult updateDepartment(DepartmentUpdateVo departmentUpdateVo){
        if(updateUserService.updateDepartment(departmentUpdateVo)) return new HttpResult("updateResult:success");
        else return new HttpResult("updateResult:error");
    }
}
