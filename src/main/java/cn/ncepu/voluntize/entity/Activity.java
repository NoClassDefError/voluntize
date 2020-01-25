package cn.ncepu.voluntize.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * <h2>志愿活动的实体类</h2>
 * <p>
 * 一场志愿活动分为多个志愿岗位，ActivityStation
 * 一个志愿岗位分为多个时间段，ActivityPeriod
 * 最终按时间段进行报名。
 *
 * @author Ge Hanchen
 * @since 0.0.1
 */
@Data
@Entity
@JsonIgnoreProperties({"images", "comments", "stations", "department", "status"})
@ToString(exclude = {"images", "comments", "stations", "department", "status"})
public class Activity {

    /**
     * 唯一标识id，类型String，主键生成策略：uuid2
     */
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "id")
    private String id;

    /**
     * 使用枚举标志活动所处阶段，但不被直接存在数据库中
     */
    @Transient
    private ActivityStatus status;

    /**
     * 用于数据库存储，总是与status匹配，请不要直接操作此值，而是操作status；
     */
    @Basic
    @Column(name = "status_id", columnDefinition = "int default 0 comment '0 部门已发送等待审核\n" +
            "1 审核并修改\n" +
            "2 报名\n" +
            "3 录用并开始活动\n" +
            "4 结束并评价'")
    private int statusId;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "semester", columnDefinition = "varchar(255) comment '学期，例如：“2019-2020学年第一学期”'")
    private String semester;

    @Basic
    @Column(name = "description", columnDefinition = "text")
    private String description;

    @ManyToOne(targetEntity = Department.class)
    @JoinColumn(name = "department", referencedColumnName = "id")
    private Department department;

    /**
     * 志愿活动图册
     * TODO 级联保存与删除问题
     */
    @OneToMany(targetEntity = Image.class, mappedBy = "activity", cascade = CascadeType.ALL)
    private List<Image> images;

    /**
     * 志愿活动的评论区，只有在报名阶段以后才允许评论
     */
    @OneToMany(targetEntity = Comment.class, mappedBy = "activity")
    private List<Comment> comments;

    /**
     * 划分为多个岗位
     */
    @OneToMany(targetEntity = ActivityStation.class, mappedBy = "parentActivity")
    private List<ActivityStation> stations;

    public void setStatus(ActivityStatus status) {
        this.statusId = status.ordinal();
        this.status = status;
    }

    public void setStatusId(int statusId) {
        switch (statusId) {
            case 0:
                this.status = ActivityStatus.CONFIRMING;
                break;
            case 1:
                this.status = ActivityStatus.SEND;
                break;
            case 2:
                this.status = ActivityStatus.APPLY;
                break;
            case 3:
                this.status = ActivityStatus.STARTED;
                break;
            case 4:
                this.status = ActivityStatus.FINISHED;
                break;
        }
        this.statusId = statusId;
    }

    /**
     * <h2>志愿活动生命周期：</h2>
     * <ul>
     *     <li>CONFIRMING 部门已发送等待审核</li>
     *     <li>SEND 审核并修改</li>
     *     <li>APPLY 报名</li>
     *     <li>STARTED 录用并开始活动</li>
     *     <li>FINISHED 结束并评价</li>
     * </ul>
     * 共5个阶段
     */
    public enum ActivityStatus {
        CONFIRMING, SEND, APPLY, STARTED, FINISHED
    }
}
