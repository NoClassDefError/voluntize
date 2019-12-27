package cn.ncepu.voluntize.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Time;
import java.util.ArrayList;

/**
 * <h2>志愿活动时间段</h2>
 * 一个志愿分多个时间段进行报名与审核。
 *
 * @author Ge Hanchen
 * @since 0.0.1
 */
@Data
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
    @ManyToOne(targetEntity = ActivityStation.class)
    @JoinColumn(name = "parent", referencedColumnName = "id")
    private ActivityStation parent;

    @Basic
    @Column(name = "start_time")
    private Time startTime;

    @Basic
    @Column(name = "end_time")
    private Time endTime;

    /**
     * 等效志愿时长
     */
    @Basic
    @Column(name = "equ_duration")
    private int equDuration;

    /**
     * 所需人数，活动开始前有效
     */
    @Basic
    @Column(name = "amount_required")
    private int amountRequired;

    /**
     * 志愿记录 如有学生报名则增加一条志愿记录
     */
    @OneToMany(targetEntity = Record.class, mappedBy = "period")
    private ArrayList<Record> records;
}
