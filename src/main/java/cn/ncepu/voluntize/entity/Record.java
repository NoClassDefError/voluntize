package cn.ncepu.voluntize.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * <h2>志愿记录实体类</h2>
 * 是Student与ActivityPeriod之间的多对多关联表<br>
 * 生命周期
 * <ul>
 *     <li>已报名</li>
 *     <li>已审核</li>
 *     <li>已授分</li>
 *     <li>已评价</li>
 * </ul>
 *
 * @author Ge Hanchen
 * @since 0.0.1
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "record")
@ToString(exclude = {"status", "period", "volunteer"})
@JsonIgnoreProperties({"status", "period", "volunteer"})
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
     * 学生报名时的备注信息
     */
    @Basic
    @Column(name = "info", length = 5000)
    private String info;
    /**
     * 用于数据库存储，总是与status匹配，请不要直接操作此值，而是操作status；
     */
    @Basic
    @Column(name = "status_id", nullable = false, columnDefinition = "integer default 0")
    private int statusId = 0;

    @Basic
    @Column(name = "create_time")
    @CreatedDate
    private Long createTime;

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
    private boolean isPassed = true;

    /**
     * 志愿成绩 0 不通过 1 通过 2 良好 3 优秀
     */
    @Basic
    @Column(name = "audit_level")
    private Integer auditLevel;

    /**
     * 如果不通过，必填理由
     */
    @Basic
    @Column(name = "evaluation")
    private String evaluation;

    /**
     * 学生反馈评星 （0，1，2，3，4）
     */
    @Basic
    @Column(name = "stars", columnDefinition = "integer default 0")
    private Integer stars = 0;

    @Basic
    @Column(name = "comments", length = 5000)
    private String comment;

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
            case 3:
                this.status = RecordStatus.COMMENTED;
        }
        this.statusId = statusId;
    }

    public enum RecordStatus {
        APPLIED, PASSED, EVALUATED, COMMENTED
    }
}
