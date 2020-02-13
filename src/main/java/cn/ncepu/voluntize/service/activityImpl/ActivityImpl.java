package cn.ncepu.voluntize.service.activityImpl;

import cn.ncepu.voluntize.entity.*;
import cn.ncepu.voluntize.repository.*;
import cn.ncepu.voluntize.service.ActivityService;
import cn.ncepu.voluntize.vo.ActivityPeriodVo;
import cn.ncepu.voluntize.vo.ActivityStationVo;
import cn.ncepu.voluntize.vo.ActivityVo;
import cn.ncepu.voluntize.vo.ImageVo;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class ActivityImpl implements ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ActivityStationRepository activityStationRepository;

    @Autowired
    private ActivityPeriodRepository activityPeriodRepository;

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private ServletContext context;

    @Autowired
    private ScheduledExecutorService scheduledExecutorService;

    @Override
    public String createOrUpdate(ActivityVo activity) {
        if (activity.getId() == null) {
            //新增
            if (context.getAttribute("autoSendActivity").equals(true))
                activity.setStatus(1);
            else activity.setStatus(0);
            //添加定时器
            Runnable task = () -> {
                activity.setStatus(2);
                activityRepository.save(convertActivityVo(new Activity(), activity));
                LoggerFactory.getLogger(this.getClass()).info("Task executed, starting activity. Id=" + activity.getId());
            };
            scheduledExecutorService.schedule(task, 3, TimeUnit.DAYS);
            return activityRepository.save(convertActivityVo(new Activity(), activity)).getId();
        }
        Optional<Activity> optional = activityRepository.findById(activity.getId());
        if (optional.isPresent())
            //修改
            return activityRepository.save(convertActivityVo(optional.get(), activity)).getId();
        return "not found";
    }

    @Override
    public String updateStation(ActivityStationVo activityStationVo) {
        if (activityStationVo.getId() == null)
            return activityStationRepository.save(
                    convertActivityStationVo(new ActivityStation(), activityStationVo)).getId();
        return activityStationRepository.save(convertActivityStationVo(
                activityStationRepository.findById(activityStationVo.getId())
                        .orElse(new ActivityStation()), activityStationVo)).getId();
    }

    @Override
    public String updatePeriod(ActivityPeriodVo activityPeriodVo) {
        if (activityPeriodVo.getId() == null)
            return activityPeriodRepository.save(
                    convertActivityPeriodVo(new ActivityPeriod(), activityPeriodVo)).getId();
        return activityPeriodRepository.save(convertActivityPeriodVo(
                activityPeriodRepository.findById(activityPeriodVo.getId())
                        .orElse(new ActivityPeriod()), activityPeriodVo)).getId();
    }

    private Activity convertActivityVo(Activity origin, ActivityVo activity) {
        origin.setName(activity.getName());
        origin.setSemester(activity.getSemester());
        origin.setDescription(activity.getDescription());
        origin.setStatusId(activity.getStatus());
        if (activity.getDepartmentId() == null) origin.setDepartment(null);
        if (departmentRepository.findById(activity.getDepartmentId()).isPresent())
            origin.setDepartment(departmentRepository.findById(activity.getDepartmentId()).get());
        ArrayList<Image> images = new ArrayList<>();
        for (ImageVo imageVo : activity.getImages()) images.add(imageVo.toImage());
        origin.setImages(images);
//        ArrayList<ActivityStation> activityStations = new ArrayList<>();
//        activity.getStations().forEach(stationVo -> activityStations.add(convertActivityStationVo(new ActivityStation(), stationVo)));
//        origin.setStations(activityStations);
        return origin;
    }

    private ActivityStation convertActivityStationVo(ActivityStation station, ActivityStationVo stationVo) {
        station.setDescription(stationVo.getDescription());
        station.setLinkman(stationVo.getLinkman());
        station.setName(stationVo.getName());
        station.setPhoneNum(stationVo.getPhoneNum());

        station.setParentActivity(activityRepository.findById(stationVo.getParentId()).orElse(null));
//        ArrayList<ActivityPeriod> activityPeriods = new ArrayList<>();
//        stationVo.getPeriods().forEach(activityPeriodVo -> activityPeriods.add(convertActivityPeriodVo(new ActivityPeriod(), activityPeriodVo)));
//        station.setPeriods(activityPeriods);
        return station;
    }

    private ActivityPeriod convertActivityPeriodVo(ActivityPeriod period, ActivityPeriodVo periodVo) {
        period.setAmountRequired(periodVo.getAmountRequired());
        System.out.println(periodVo.getEndDate() + periodVo.getStartDate() + periodVo);
        period.setEndDate(Timestamp.valueOf(periodVo.getEndDate()));
        period.setStartDate(Timestamp.valueOf(periodVo.getStartDate()));
        period.setEquDuration(periodVo.getEquDuration());
        period.setParent(activityStationRepository.findById(periodVo.getParentStationId()).orElse(null));

        period.setRequirements(periodVo.getRequirements());
        period.setTimePeriod(periodVo.getTimePeriod());
        // TODO 这里不setRecords，更新activity时，Hibernate会删除关联吗？猜测是会，因为period主键id已经改变
        return period;
    }

    @Override
    public void deleteActivity(String id) {
//        System.out.println(id);
        activityRepository.deleteById(id);
    }

    @Override
    public void deleteActivityPeriod(String id) {
        activityPeriodRepository.deleteById(id);
    }

    @Override
    public void deleteActivityStation(String id) {
        activityStationRepository.deleteById(id);
    }

    @Override
    public List<Activity> findAll() {
        return activityRepository.findAll();
    }

    @Override
    public List<Activity> findStatus(Activity.ActivityStatus status) {
//      放弃使用QBE的方法
//        Activity activity = new Activity();
//        activity.setStatus(status[0]);
//        Example<Activity> example = Example.of(activity);
        return activityRepository.findByStatus(status.ordinal());
    }

    @Override
    public Page<Activity> notToFindStatus(Activity.ActivityStatus status, int page, int size) {
        return activityRepository.notToFindByStatus(status.ordinal(),
                PageRequest.of(page, size));
    }

    @Override
    public String startActivity(String activityId) {
        Activity activity = activityRepository.findById(activityId).orElse(null);
        if (activity != null) {
            activity.setStatus(Activity.ActivityStatus.STARTED);
            for (ActivityStation station : activity.getStations())
                for (ActivityPeriod period : station.getPeriods())
                    for (Record record : period.getRecords())
                        if (record.getStatusId() == 0) {
                            record.setPassed(false);
                            recordRepository.save(record);
                        }
            activityRepository.save(activity);
            return "success";
        } else return "not found";
    }

    @Override
    public String changeStatus(String activityId, Activity.ActivityStatus status) {
        Activity activity = activityRepository.findById(activityId).orElse(null);
        if (activity != null) {
            activity.setStatus(status);
            activityRepository.save(activity);
            return "success";
        } else return "not found";
    }

    @Override
    public List<Activity> findDepartment(String departmentId, Integer status) {
        if (status == null) return activityRepository.findByDepartmentId(departmentId);
        else return activityRepository.findByDepartmentId(departmentId, status);
    }

    @Override
    public Activity findById(String activityId) {
        return activityRepository.findById(activityId).orElse(null);
    }

    @Override
    public Activity findByPeriod(String periodId) {
        Optional<ActivityPeriod> period = activityPeriodRepository.findById(periodId);
        return period.map(activityPeriod -> activityPeriod.getParent().getParentActivity()).orElse(null);
    }

    @Override
    public Activity findByStation(String stationId) {
        return activityStationRepository.findById(stationId).map(ActivityStation::getParentActivity).orElse(null);
    }
}
