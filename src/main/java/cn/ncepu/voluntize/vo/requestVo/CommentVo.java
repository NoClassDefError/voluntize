package cn.ncepu.voluntize.vo.requestVo;

import cn.ncepu.voluntize.vo.ImageVo;
import lombok.Data;

import java.util.ArrayList;

@Data
public class CommentVo {
    private String id;
    private String content;
    private ArrayList<ImageVo> imageVos;
    private String parentCommentId;
    private String activityId;
    //toComment要在service层里写
}
