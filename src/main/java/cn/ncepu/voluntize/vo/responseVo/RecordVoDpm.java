package cn.ncepu.voluntize.vo.responseVo;

import cn.ncepu.voluntize.entity.Record;
import lombok.Data;

/**
 * 本类用于显示志愿记录
 */
@Data
public class RecordVoDpm {
    private String id;
    private StudentVo volunteer;
    private String periodId;
    private String info;
    private int status;
    private boolean isPassed;
    private Integer auditLevel;
    private String evaluation;
    private Integer stars;
    private String comment;

    public RecordVoDpm() {
    }

    public RecordVoDpm(Record record) {
        this.id = record.getId();
        this.auditLevel = record.getAuditLevel();
        this.comment = record.getComment();
        this.evaluation = record.getEvaluation();
        this.info = record.getInfo();
        this.isPassed = record.isPassed();
        this.status = record.getStatusId();
        this.stars = record.getStars();
        this.volunteer = new StudentVo(record.getVolunteer());
        this.periodId = record.getPeriod().getId();
    }
}
