package cn.ncepu.voluntize.service;

import cn.ncepu.voluntize.entity.Record;
import cn.ncepu.voluntize.entity.Student;
import cn.ncepu.voluntize.vo.requestVo.AppraiseVo;
import cn.ncepu.voluntize.vo.requestVo.EvaluateVo;
import cn.ncepu.voluntize.vo.requestVo.ParticipateVo;

import java.util.ArrayList;
import java.util.List;

public interface ParticipateService {
    String participate(ParticipateVo participateVo);

    /**
     * 取消报名或退出活动
     */
    String cancel(String recordId);

    /**
     * 为了防止数据过大。禁止Record夹在Activity转成的json里
     * 专门设置查询方法
     */
    ArrayList<Record> getRecord(String periodId, Record.RecordStatus status);

    ArrayList<Record> getRecord(String periodId);

    List<Record> getRecordByStu(Integer status);

    /**
     * 注意，accept与evaluate方法在前端均是批量操作的
     * 一次性评论完ActivityPeriod下所有record
     *
     */
    String accept(List<String> records);

    String deny(String records);

    /**
     * 老师给予学生成绩
     * Integer为成绩等级，0 不通过 1 通过 2 良好 3 优秀
     */
    void evaluate(List<EvaluateVo> records);


    /**
     * 学生给予活动评价
     */
    String appraise(AppraiseVo evaluateVo);

    Student studentInfo(String id);
}
