package cn.ncepu.voluntize.service.activityImpl;

import cn.ncepu.voluntize.entity.Activity;
import cn.ncepu.voluntize.entity.ActivityPeriod;
import cn.ncepu.voluntize.entity.Record;
import cn.ncepu.voluntize.entity.Student;
import cn.ncepu.voluntize.repository.ActivityPeriodRepository;
import cn.ncepu.voluntize.repository.RecordRepository;
import cn.ncepu.voluntize.repository.StudentRepository;
import cn.ncepu.voluntize.service.ActivityService;
import cn.ncepu.voluntize.service.ParticipateService;
import cn.ncepu.voluntize.vo.requestVo.AppraiseVo;
import cn.ncepu.voluntize.vo.requestVo.EvaluateVo;
import cn.ncepu.voluntize.vo.requestVo.ParticipateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ParticipateImpl implements ParticipateService {

    @Autowired
    private HttpSession session;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private ActivityPeriodRepository activityPeriodRepository;

    /**
     * 添加多对多关联，不需要更新Student表与ActivityPeriod表
     */
    @Override
    public String participate(ParticipateVo participateVo) {
        Optional<ActivityPeriod> activityPeriod = activityPeriodRepository.findById(participateVo.getPeriodId());
        Optional<Student> student = studentRepository.findById((String) session.getAttribute("UserId"));
        if (student.isPresent() && activityPeriod.isPresent()) {
            int status = activityPeriod.get().getParent().getParentActivity().getStatusId();
            if (status == 1) {
                Record record = new Record();
                record.setVolunteer(student.get());
                record.setPeriod(activityPeriod.get());
                record.setInfo(participateVo.getInfo());
                if (recordRepository.findOne(Example.of(record)).isPresent())
                    return "You've already participated.";//检验不能重复报名
                record.setStatus(Record.RecordStatus.APPLIED);
                record.setStars(0);
                recordRepository.save(record);
                return "success";
            } else return "Cannot participate, the activity status is " + status;
        }
        return "error";
    }

    @Override
    public String cancel(String recordId) {
        recordRepository.deleteById(recordId);
//        Optional<Record> record = recordRepository.findById(recordId);
//        if (record.isPresent()) {
//            Record record1 = record.get();
//            record1.setPassed(false);
//            record1.setStatus(Record.RecordStatus.APPLIED);
//            recordRepository.save(record1);
//            return "success";
//        }
        return "success";
    }

    @Override
    public ArrayList<Record> getRecord(String periodId, Record.RecordStatus status) {
        Optional<ActivityPeriod> activityPeriod = activityPeriodRepository.findById(periodId);
        if (activityPeriod.isPresent()) {
            ArrayList<Record> result = new ArrayList<>();
            for (Record record : activityPeriod.get().getRecords())
                if (record.getStatus().equals(status)) result.add(record);
            return result;
        }
        return null;
    }

    @Override
    public ArrayList<Record> getRecord(String periodId) {
        Optional<ActivityPeriod> activityPeriod = activityPeriodRepository.findById(periodId);
        return activityPeriod.map(period -> new ArrayList<>(period.getRecords())).orElse(null);
    }

    @Override
    public List<Record> getRecordByStu(Integer status) {
        String studentId = (String) session.getAttribute("UserId");
        if (studentId == null) return null;
//        System.out.println(status);
        if (status == null) return recordRepository.findByStudent(studentId);
        else return recordRepository.findByStudent(studentId, status);
    }

    @Override
    public String accept(List<String> records) {
        boolean flag = true;
//        System.out.println(records);
        List<String> notfounds = new ArrayList<>();
        for (String id : records) {
            Record record = recordRepository.findById(id).orElse(null);
            if (record != null) {
                if (flag) {
                    flag = false;
                    if (record.getPeriod().getParent().getParentActivity().getStatusId() != 1)
                        return "录取失败，该活动当前不在报名期";
                }
                record.setStatus(Record.RecordStatus.PASSED);
                record.setPassed(true);
                recordRepository.save(record);
            } else notfounds.add(id);
        }
        return "除了这些学生的报名记录没找到外，其它学生都已经成功录取" + notfounds;
    }

    @Override
    public String deny(String records) {
        Record record = recordRepository.findById(records).orElse(null);
        if (record != null) {
            if (record.getPeriod().getParent().getParentActivity().getStatusId() != 2)
                return "Cannot accept! The activity is not in the status for participation.";
            record.setStatus(Record.RecordStatus.APPLIED);
            record.setPassed(false);
            recordRepository.save(record);
            return "success";
        } else
            return "not found";
    }

    @Override
    public void evaluate(List<EvaluateVo> records) {
//        boolean flag = true;
        for (EvaluateVo evaluateVo : records) {
            Record record1 = recordRepository.findById(evaluateVo.getRecordId()).orElse(null);
            if (record1 != null) {
                if(record1.getPeriod().getParent().getParentActivity().getStatus()==Activity.ActivityStatus.FINISHED){
                    record1.setStatus(Record.RecordStatus.EVALUATED);
                    record1.setAuditLevel(evaluateVo.getAuditLevel());
                    record1.setEvaluation(evaluateVo.getEvaluate());
                    recordRepository.save(record1);
                }
//                if (flag) {
//                    activityService.changeStatus(
//                            record1.getPeriod().getParent().getParentActivity().getId(),
//                            Activity.ActivityStatus.FINISHED);
//                    flag = false;
//                }
            }
        }
    }

    @Override
    public String appraise(AppraiseVo evaluateVo) {
        Optional<Record> record = recordRepository.findById(evaluateVo.getRecordId());
        if (record.isPresent()) {
            Record record1 = record.get();
            record1.setStatus(Record.RecordStatus.COMMENTED);
            record1.setComment(evaluateVo.getComment());
            record1.setStars(evaluateVo.getStars());
            recordRepository.save(record1);
            return "success";
        }
        return "error";
    }

    @Override
    public Student studentInfo(String id) {
        Optional<Student> optional = studentRepository.findById(id);
        return optional.orElse(null);
    }
}
