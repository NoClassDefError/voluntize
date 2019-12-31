package cn.ncepu.voluntize.controller;

import cn.ncepu.voluntize.entity.Record;
import cn.ncepu.voluntize.requestVo.ParticipateVo;
import cn.ncepu.voluntize.responseVo.HttpResult;
import cn.ncepu.voluntize.service.ParticipateService;
import com.alibaba.fastjson.JSON;
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

    @RequestMapping(value = "/getRecord", method = RequestMethod.POST)
    @ResponseBody
    public String getRecord(String periodId, String status) {
        return JSON.toJSONString(participateService.getRecord(periodId, Record.RecordStatus.valueOf(status)));
    }


}
