package cn.ncepu.voluntize.controller;

import cn.ncepu.voluntize.entity.Record;
import cn.ncepu.voluntize.vo.requestVo.ParticipateVo;
import cn.ncepu.voluntize.vo.responseVo.HttpResult;
import cn.ncepu.voluntize.service.ParticipateService;
import cn.ncepu.voluntize.vo.responseVo.RecordVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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

    /**
     * 学生在自己主页上获取自己参加的公益来动记录，后期可以增加分页功能
     */
    @RequestMapping(value = "/getStuRecord", method = RequestMethod.POST)
    @ResponseBody
    public ArrayList<RecordVo> getStuRecord(String periodId, String status) {
        ArrayList<RecordVo> recordVos = new ArrayList<>();
        for (Record record : participateService.getRecord(periodId, Record.RecordStatus.valueOf(status)))
            recordVos.add(new RecordVo(record));
        return recordVos;
    }

}
