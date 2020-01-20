package cn.ncepu.voluntize.vo;

import cn.ncepu.voluntize.entity.Image;
import lombok.Data;

/**
 * 本类通用于request和response
 */
@Data
public class ImageVo {
    private String name;
    private String url;

    public Image toImage() {
        Image image = new Image();
        image.setName(name);
        image.setUrl(url);
        return image;
    }

    public ImageVo() { }

    public ImageVo(Image image) {
        this.name = image.getName();
        this.url = image.getUrl();
    }
}
