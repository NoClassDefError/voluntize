package cn.ncepu.voluntize.vo.responseVo;

import cn.ncepu.voluntize.entity.Record;
import cn.ncepu.voluntize.vo.ActivityPeriodVo;
import lombok.Data;

/**
 * 本类用于显示志愿记录
 */
@Data
public class RecordVo {
    private String id;
    private StudentVo volunteer;
    private ActivityPeriodVo period;
    private String info;
    private Record.RecordStatus status;
    private boolean isPassed;
    private int auditLevel;
    private String evaluation;
    private int stars;
    private String comment;

    public RecordVo(Record record){
        this.id = record.getId();
        this.auditLevel = record.getAuditLevel();
        this.comment = record.getComment();
        this.evaluation = record.getEvaluation();
        this.info = record.getInfo();
        this.isPassed = record.isPassed();
        this.status = record.getStatus();
        this.stars = record.getStars();
        this.volunteer = new StudentVo(record.getVolunteer());
        this.period = new ActivityPeriodVo(record.getPeriod());
    }
}
