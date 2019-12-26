package cn.ncepu.voluntize.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.ArrayList;

/**
 * <h2>志愿活动的实体类</h2>
 *
 * 一场志愿活动分为多个志愿岗位，ActivityStation
 * 一个志愿岗位分为多个时间段，ActivityPeriod
 * 最终按时间段进行报名。
 *
 * @author Ge Hanchen
 * @since 0.0.1
 */
@Data
public class Activity {

    /**
     * 唯一标识id，类型String，主键生成策略：uuid2
     */
    @Id
    private String id;

    /**
     * 使用枚举标志活动所处阶段，但不被直接存在数据库中
     */
    private ActivityStatus status;

    /**
     * 用于数据库存储，总是与status匹配，请不要直接操作此值，而是操作status；
     */
    @Column(name = "status_id")
    private int statusId;

    private String name;

    private Department department;

    private String description;

    /**
     * 划分为多个岗位
     */
    private ArrayList<ActivityStation> stations;

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
    enum ActivityStatus {
        CONFIRMING, SEND, APPLY, STARTED, FINISHED
    }
}
