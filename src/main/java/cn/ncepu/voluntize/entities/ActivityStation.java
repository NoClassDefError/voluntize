package cn.ncepu.voluntize.entities;

import lombok.Data;

import java.util.ArrayList;

/**
 * <h2>志愿岗位实体类</h2>
 * 认为岗位的地点是固定的
 */
@Data
public class ActivityStation {
    /**
     * 唯一标识id，类型String，主键生成策略：uuid2
     */
    private String id;

    private String name;

    private String description;

    private String location;

    /**
     * 使用一个字符串储存多个要求，中间使用';'隔开
     */
    private String requirements;

    /**
     * 所属志愿活动
     */
    private Activity parentActivity;

    /**
     * 划分为多个时间段
     */
    private ArrayList<ActivityPeriod> periods;
}
