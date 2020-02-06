package cn.ncepu.voluntize.controller.admin;

import cn.ncepu.voluntize.service.UpdateUserService;
import cn.ncepu.voluntize.vo.requestVo.UserUpdateVoAdmin;
import cn.ncepu.voluntize.vo.responseVo.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/user")
public class UserManage {

    @Autowired
    private UpdateUserService updateUserService;

    @RequestMapping("/save")
    @ResponseBody
    public HttpResult saveUser(@RequestBody UserUpdateVoAdmin vo) {
        if (new Integer(1).equals(vo.getCategory()))
            return new HttpResult("saveUser:" + updateUserService.updateStudent(vo),
                    "category:student");
        else if (new Integer(2).equals(vo.getCategory()))
            return new HttpResult("saveUser:" + updateUserService.updateDepartment(vo),
                    "category:department");
        return new HttpResult("createUser:No such category.");
    }

    @RequestMapping("/delete")
    @ResponseBody
    public HttpResult deleteUser(String id) {
        return new HttpResult("deleteUser:" + updateUserService.deleteUser(id));
    }


}
