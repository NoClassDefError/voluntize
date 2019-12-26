package cn.ncepu.voluntize.entities;

import lombok.Data;

import java.sql.Time;
import java.util.ArrayList;

/**
 * <h2>志愿活动时间段</h2>
 * 一个志愿分多个时间段进行报名与审核。
 */
@Data
public class ActivityPeriod {
    /**
     * 唯一标识id，类型String，主键生成策略：uuid2
     */
    private String id;

    /**
     * 所属志愿岗位
     */
    private ActivityStation parent;

    private Time startTime;

    private Time endTime;

    /**
     * 等效志愿时长
     */
    private int equDuration;

    /**
     * 所需人数，活动开始前有效
     */
    private int amountRequired;

    /**
     * 报名者
     */
    private ArrayList<Student> appliers;

    /**
     * 参与者，报名审核通过则为参与者
     */
    private ArrayList<Student> participants;


    private ArrayList<Student> winners;
}
