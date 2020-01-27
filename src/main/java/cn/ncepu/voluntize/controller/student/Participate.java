package cn.ncepu.voluntize.controller.student;

import cn.ncepu.voluntize.controller.BaseController;
import cn.ncepu.voluntize.service.ParticipateService;
import cn.ncepu.voluntize.vo.requestVo.AppraiseVo;
import cn.ncepu.voluntize.vo.requestVo.ParticipateVo;
import cn.ncepu.voluntize.vo.responseVo.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/student/service")
public class Participate extends BaseController {

    @Autowired
    private ParticipateService participateService;

    @RequestMapping(value = "/participate", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult participate(@RequestBody ParticipateVo participateVo) {
        String result = participateService.participate(participateVo);
        return new HttpResult("participate:" + result);
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult cancel(String recordId) {
        return new HttpResult("cancel", participateService.cancel(recordId));
    }

    @RequestMapping(value = "/appraise", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult appraise(@RequestBody AppraiseVo appraiseVo) {
        return new HttpResult("appraise:" + participateService.appraise(appraiseVo));
    }
}
