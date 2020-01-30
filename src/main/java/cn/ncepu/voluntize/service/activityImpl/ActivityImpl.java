package cn.ncepu.voluntize.service.activityImpl;

import cn.ncepu.voluntize.entity.*;
import cn.ncepu.voluntize.repository.ActivityPeriodRepository;
import cn.ncepu.voluntize.repository.ActivityRepository;
import cn.ncepu.voluntize.repository.ActivityStationRepository;
import cn.ncepu.voluntize.repository.DepartmentRepository;
import cn.ncepu.voluntize.service.ActivityService;
import cn.ncepu.voluntize.vo.ActivityPeriodVo;
import cn.ncepu.voluntize.vo.ActivityStationVo;
import cn.ncepu.voluntize.vo.ActivityVo;
import cn.ncepu.voluntize.vo.ImageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    /**
     * 需特别注意，hibernate可以关联查询，但不能关联保存
     */
    @Override
    public String createOrUpdate(ActivityVo activity) {
        if (activity.getId() == null)
            return activityRepository.save(convertActivityVo(new Activity(), activity)).getId();
        Optional<Activity> optional = activityRepository.findById(activity.getId());
        if (optional.isPresent())
            return activityRepository.save(convertActivityVo(optional.get(), activity)).getId();
        else return activityRepository.save(convertActivityVo(new Activity(), activity)).getId();
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
        Activity activity = new Activity();
        activity.setStatus(status);
        Example<Activity> example = Example.of(activity);
        return activityRepository.findAll(example);
    }

    @Deprecated
    public void confirm(String id) {
        if (activityRepository.findById(id).isPresent()) {
            Activity activity = activityRepository.findById(id).get();
            if (activity.getStatus().equals(Activity.ActivityStatus.CONFIRMING))
                activity.setStatus(Activity.ActivityStatus.SEND);
            activityRepository.save(activity);
        }
    }

    @Override
    public List<Activity> findDepartment(String departmentId) {
        Optional<Department> department = departmentRepository.findById(departmentId);
        if (department.isPresent()) {
            Department department1 = department.get();
            return department1.getActivities();
        }
        return null;
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
    public Activity findByStation(String stationId){
        return activityStationRepository.findById(stationId).map(ActivityStation::getParentActivity).orElse(null);
    }
}
