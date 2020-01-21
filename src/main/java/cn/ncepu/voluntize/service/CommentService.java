package cn.ncepu.voluntize.service;

import cn.ncepu.voluntize.vo.requestVo.CommentVo;

import java.util.ArrayList;

public interface CommentService {
    String saveOrUpdate(CommentVo commentVo);

    void delete(String commentId);

    ArrayList<cn.ncepu.voluntize.vo.responseVo.CommentVo> getComments(String activityId, Integer size, Integer page);

}
