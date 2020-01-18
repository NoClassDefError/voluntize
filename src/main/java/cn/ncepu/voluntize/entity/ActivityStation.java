package cn.ncepu.voluntize.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * <h2>志愿岗位实体类</h2>
 * 认为岗位的地点是固定的
 *
 * @author Ge Hanchen
 * @since 0.0.1
 */
@Data
@Entity
@Table(name = "activity_station")
public class ActivityStation {
    /**
     * 唯一标识id，类型String，主键生成策略：uuid2
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id")
    private String id;

    @Basic
    @Column(name = "station_name")
    private String name;

    @Basic
    @Column(name = "description", columnDefinition = "text")
    private String description;

    /**
     * 使用一个字符串储存多个要求，中间使用';'隔开
     */
    @Basic
    @Column(name = "requirements", columnDefinition = "text")
    private String requirements;

    /**
     * 所属志愿活动
     */
    @ManyToOne(targetEntity = Activity.class)
    @JoinColumn(name = "parent_activity", referencedColumnName = "id")
    private Activity parentActivity;

    /**
     * 划分为多个时间段
     */
    @OneToMany(targetEntity = ActivityPeriod.class, mappedBy = "parent")
    private List<ActivityPeriod> periods;
}
