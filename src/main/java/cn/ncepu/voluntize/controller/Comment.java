package cn.ncepu.voluntize.controller;

import cn.ncepu.voluntize.service.CommentService;
import cn.ncepu.voluntize.vo.requestVo.CommentVo;
import cn.ncepu.voluntize.vo.responseVo.HttpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/comment")
public class Comment {

    @Autowired
    private CommentService commentService;

    @RequestMapping("/save")
    @ResponseBody
    public HttpResult comment(@RequestBody CommentVo commentVo) {
        return new HttpResult("save:" + commentService.saveOrUpdate(commentVo));
    }

    @RequestMapping("/delete")
    public void delete(String commentId) {
        commentService.delete(commentId);
    }

    @RequestMapping("/getComments")
    @ResponseBody
    public ArrayList<cn.ncepu.voluntize.vo.responseVo.CommentVo> getComments(String activity, int page) {
        return commentService.getComments(activity, page);
    }

    @RequestMapping("/getIndexComments")
    @ResponseBody
    public ArrayList<cn.ncepu.voluntize.vo.responseVo.CommentVo> getComments() {
        return commentService.getIndexComments();
    }
}
