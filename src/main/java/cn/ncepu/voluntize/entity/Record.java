package cn.ncepu.voluntize.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * <h2>志愿记录实体类</h2>
 * 是Student与ActivityPeriod之间的多对多关联表<br>
 * 生命周期
 * <ul>
 *     <li>已报名</li>
 *     <li>已审核</li>
 *     <li>已评价</li>
 * </ul>
 *
 * @author Ge Hanchen
 * @since 0.0.1
 */
@Data
@Entity
//@Table(name="record")
public class Record {
    /**
     * 唯一标识id，类型String，主键生成策略：uuid2
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id")
    private String id;

    /**
     * 外键 多对一关联 student 表
     */
    @ManyToOne(targetEntity = Student.class)
    @JoinColumn(name = "volunteer", referencedColumnName = "id")
    private Student volunteer;

    @ManyToOne(targetEntity = ActivityPeriod.class)
    @JoinColumn(name = "the_period", referencedColumnName = "id")
    private ActivityPeriod period;

    /**
     * 用于数据库存储，总是与status匹配，请不要直接操作此值，而是操作status；
     */
    @Basic
    @Column(name = "status_id")
    private int statusId;

    /**
     * 使用枚举标志志愿所处阶段，但不被直接存在数据库中
     */
    @Transient
    private RecordStatus status;

    /**
     * 被允许加入活动，初始值为空
     */
    @Basic
    @Column(name = "is_passed")
    private boolean isPassed;

    /**
     * 志愿成绩 0 不通过 1 通过 2 良好 3 优秀
     */
    @Basic
    @Column(name = "audit_level")
    private int auditLevel;

    public void setStatus(RecordStatus status) {
        this.statusId = status.ordinal();
        this.status = status;
    }

    public void setStatusId(int statusId) {
        switch (statusId) {
            case 0:
                this.status = RecordStatus.APPLIED;
                break;
            case 1:
                this.status = RecordStatus.PASSED;
                break;
            case 2:
                this.status = RecordStatus.EVALUATED;
                break;
        }
        this.statusId = statusId;
    }

    enum RecordStatus {
        APPLIED, PASSED, EVALUATED
    }
}
