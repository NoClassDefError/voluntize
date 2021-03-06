package cn.ncepu.voluntize.controller.department;

import cn.ncepu.voluntize.controller.BaseController;
import cn.ncepu.voluntize.service.UpdateUserService;
import cn.ncepu.voluntize.vo.requestVo.DepartmentUpdateVo;
import cn.ncepu.voluntize.vo.responseVo.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/department")
public class DepartmentUpdate extends BaseController {
    @Autowired
    UpdateUserService updateUserService;

    @RequestMapping(value="/updateDepartment",method = RequestMethod.POST)
    @ResponseBody
    public HttpResult updateDepartment(@RequestBody DepartmentUpdateVo departmentUpdateVo){
        if(updateUserService.updateDepartment(departmentUpdateVo)) return new HttpResult("updateResult:success");
        else return new HttpResult("updateResult:error");
    }
}
