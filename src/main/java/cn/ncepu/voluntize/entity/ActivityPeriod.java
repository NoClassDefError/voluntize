package cn.ncepu.voluntize.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

/**
 * <h2>志愿活动时间段</h2>
 * 一个志愿分多个时间段进行报名与审核。
 *
 * @author Ge Hanchen
 * @since 0.0.1
 */
@Data
@Entity
@Table(name = "activity_period")//此处表名自动生成时与类名不同，idea语法检查时会找不到表
@ToString(exclude = {"records", "parent"})
@JsonIgnoreProperties({"records", "parent"})
public class ActivityPeriod {
    /**
     * 唯一标识id，类型String，主键生成策略：uuid2
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id")
    private String id;

    /**
     * 所属志愿岗位
     */
    @ManyToOne(targetEntity = ActivityStation.class,cascade = CascadeType.REMOVE)
    @JoinColumn(name = "parent", referencedColumnName = "id")
    private ActivityStation parent;

    /**
     * 对时间和时间段。。。。！
     */
    @Basic
    @Column(name = "start_date", columnDefinition = "timestamp default now() comment '本项公益劳动开始的日期'")
    private Timestamp startDate;

    @Basic
    @Column(name = "end_date", columnDefinition = "timestamp default now()")
    private Timestamp endDate;

    @Basic
    @Column(name = "period", columnDefinition = "text comment '每天活动时间安排，例如上午8至11，下午2至5'")
    private String timePeriod;

    /**
     * 使用一个字符串储存多个要求，中间使用';'隔开
     */
    @Basic
    @Column(name = "requirements", columnDefinition = "text")
    private String requirements;

    /**
     * 等效志愿时长
     */
    @Basic
    @Column(name = "equ_duration", columnDefinition = "int comment '等效公益劳动时长\n不是简单的末初时间的差，需要单独给定' ")
    private Integer equDuration;

    /**
     * 所需人数，活动开始前有效
     */
    @Basic
    @Column(name = "amount_required", columnDefinition = "int comment '所需人数'")
    private Integer amountRequired;

    /**
     * 志愿记录 如有学生报名则增加一条志愿记录
     */
    @JSONField(serialize = false)
    @OneToMany(targetEntity = Record.class, mappedBy = "period", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Record> records;
}
