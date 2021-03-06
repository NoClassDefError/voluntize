package cn.ncepu.voluntize.controller.department;

import cn.ncepu.voluntize.controller.BaseController;
import cn.ncepu.voluntize.service.ActivityService;
import cn.ncepu.voluntize.service.ParticipateService;
import cn.ncepu.voluntize.vo.responseVo.ActivityResponseVo;
import cn.ncepu.voluntize.vo.responseVo.RecordVoDpm;
import cn.ncepu.voluntize.vo.responseVo.StudentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/department/query")
public class DepartmentQuery extends BaseController {
    @Autowired
    private ActivityService activityService;

    @Autowired
    private ParticipateService participateService;

    @RequestMapping(value = "/released", method = RequestMethod.POST)
    public List<ActivityResponseVo> getActivity(Integer status, Integer page) {
        Pageable pageable = page == null ? Pageable.unpaged() : PageRequest.of(page, 10);
//        logger.info("" + session.getAttribute("UserId"));
//        logger.info("status:" + status + " page:" + page);
        if ("Department".equals(session.getAttribute("UserCategory")))
            return activityService.findDepartment((String) session.getAttribute("UserId"), status, pageable);
        else return null;
    }

    @RequestMapping(value = "/pages", method = RequestMethod.POST)
    public Integer getPages(Integer status) {
        return activityService.findDepartment((String) session.getAttribute("UserId"), status, Pageable.unpaged()).size() / 10 + 1;
    }

    @RequestMapping(value = "/records", method = RequestMethod.POST)
    public List<RecordVoDpm> getRecords(String periodId) {
//        System.out.println(periodId);
//        ArrayList<RecordVoDpm> recordVos = new ArrayList<>();
//        for (Record record : participateService.getRecord(periodId))
//            recordVos.add(new RecordVoDpm(record));
        return participateService.getRecord(periodId);
    }

    @RequestMapping(value = "/studentInfo", method = RequestMethod.POST)
    public StudentVo getStudent(String studentId) {
        return new StudentVo(participateService.studentInfo(studentId));
    }
}
