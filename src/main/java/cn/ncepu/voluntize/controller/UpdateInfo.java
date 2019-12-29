package cn.ncepu.voluntize.controller;

import cn.ncepu.voluntize.service.UpdateUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UpdateInfo extends BaseController {
    @Autowired
    UpdateUserService updateUserService;

    
}
