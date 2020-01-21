package cn.ncepu.voluntize.vo.requestVo;

import lombok.Data;

@Data
public class EvaluateVo {
    private String recordId;
    private String evaluate;
    private int auditLevel;
}
