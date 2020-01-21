package cn.ncepu.voluntize.service.activityImpl;

import cn.ncepu.voluntize.entity.ActivityPeriod;
import cn.ncepu.voluntize.entity.Record;
import cn.ncepu.voluntize.entity.Student;
import cn.ncepu.voluntize.repository.ActivityPeriodRepository;
import cn.ncepu.voluntize.repository.RecordRepository;
import cn.ncepu.voluntize.repository.StudentRepository;
import cn.ncepu.voluntize.service.ParticipateService;
import cn.ncepu.voluntize.vo.requestVo.AppraiseVo;
import cn.ncepu.voluntize.vo.requestVo.EvaluateVo;
import cn.ncepu.voluntize.vo.requestVo.ParticipateVo;
import cn.ncepu.voluntize.vo.responseVo.StudentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
            Record record = new Record();
            record.setVolunteer(student.get());
            record.setPeriod(activityPeriod.get());
            record.setInfo(participateVo.getInfo());
            if (recordRepository.findOne(Example.of(record)).isPresent())
                return "duplicated";//检验不能重复报名
            record.setStatus(Record.RecordStatus.APPLIED);
            recordRepository.save(record);
            return "success";
        }
        return "error";
    }

    @Override
    public String cancel(String recordId) {
        Optional<Record> record = recordRepository.findById(recordId);
        if (record.isPresent()) {
            Record record1 = record.get();
            record1.setPassed(false);
            record1.setStatus(Record.RecordStatus.APPLIED);
            recordRepository.save(record1);
            return "success";
        }
        return "error";
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
    public void accept(List<String> records) {
        for (String id : records) {
            Optional<Record> record = recordRepository.findById(id);
            if (record.isPresent()) {
                record.get().setStatus(Record.RecordStatus.PASSED);
                record.get().setPassed(true);
                recordRepository.save(record.get());
            }
        }
    }

    @Override
    public void evaluate(List<EvaluateVo> records) {
        for (EvaluateVo evaluateVo : records) {
            Optional<Record> record = recordRepository.findById(evaluateVo.getRecordId());
            if (record.isPresent()) {
                Record record1 = record.get();
                record1.setStatus(Record.RecordStatus.EVALUATED);
                record1.setAuditLevel(evaluateVo.getAuditLevel());
                record1.setEvaluation(evaluateVo.getEvaluate());
                recordRepository.save(record.get());
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
    public Student studentInfo(String id){
        Optional<Student> optional = studentRepository.findById(id);
        return optional.orElse(null);
    }
}
