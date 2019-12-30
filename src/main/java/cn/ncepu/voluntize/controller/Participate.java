package cn.ncepu.voluntize.controller;

import cn.ncepu.voluntize.requestVo.ParticipateVo;
import cn.ncepu.voluntize.responseVo.HttpResult;
import cn.ncepu.voluntize.service.ParticipateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class Participate extends BaseController {

    @Autowired
    private ParticipateService participateService;

    @RequestMapping(value = "/participate", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult participate(@RequestBody ParticipateVo participateVo) {
        if (participateService.participate(participateVo) == null)
            return new HttpResult("participate:success");
        else return new HttpResult("participate:error");
    }
}
