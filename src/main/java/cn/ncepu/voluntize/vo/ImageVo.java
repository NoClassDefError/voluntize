package cn.ncepu.voluntize.vo;

import cn.ncepu.voluntize.entity.Image;
import lombok.Data;

@Data
public class ImageVo {
    private String name;
    private String url;

    public Image toImage(){
        Image image = new Image();
        image.setName(name);
        image.setUrl(url);
        return image;
    }
}
