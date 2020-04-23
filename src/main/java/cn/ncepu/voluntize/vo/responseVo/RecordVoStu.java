package cn.ncepu.voluntize.vo.responseVo;

import cn.ncepu.voluntize.entity.Record;
import cn.ncepu.voluntize.vo.ActivityVo;
import lombok.Data;

@Data
public class RecordVoStu {
    private String id;
    private ActivityVo activityVo;
    private String info;
    private int status;
    private boolean isPassed;
    private Integer auditLevel;
    private String evaluation;
    private Integer stars;
    private String comment;

    public RecordVoStu() {
    }

    public RecordVoStu(Record record) {
        activityVo = new ActivityVo(record.getPeriod().getParent().getParentActivity());
        this.id = record.getId();
        this.auditLevel = record.getAuditLevel();
        this.comment = record.getComment();
        this.info = record.getInfo();
        this.isPassed = record.isPassed();
        this.evaluation = record.getEvaluation();
        this.stars = record.getStars();
        this.comment = record.getComment();
        this.status = record.getStatusId();
    }
}
