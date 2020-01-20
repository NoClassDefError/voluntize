package cn.ncepu.voluntize.service;

import cn.ncepu.voluntize.entity.Record;
import cn.ncepu.voluntize.vo.requestVo.ParticipateVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface ParticipateService {
    Record participate(ParticipateVo participateVo);

    /**
     * 为了防止数据过大。禁止Record夹在Activity转成的json里
     * 专门设置查询方法
     */
    ArrayList<Record> getRecord(String periodId, Record.RecordStatus status);

    /**
     * 注意，accept与evaluate方法在前端均是批量操作的
     * 一次性评论完ActivityPeriod下所有record
     */
    void accept(List<String> records);

    /**
     * Integer为成绩等级，0 不通过 1 通过 2 良好 3 优秀
     * @param records Map<String,Integer> String为record的id
     */
    void evaluate(Map<String,Integer> records);
}
