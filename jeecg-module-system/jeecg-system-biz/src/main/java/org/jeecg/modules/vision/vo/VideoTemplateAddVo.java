package org.jeecg.modules.vision.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class VideoTemplateAddVo implements VideoTemplateVo {
    private String name;
    private String description;
    private String aeProjectUrl;
    private String coverImageUrl;
    private String mainRenderName;
    private String previewVideoUrl;
    private Integer width;
    private Integer height;
    private String author;
    private Integer fps;
    private String bitRate;

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getClipsJson() {
        return null;
    }

    @Override
    public String getVersion() {
        return null;
    }

    @Override
    public String getStatus() {
        return null;
    }

    @Override
    public Integer getIsDelete() {
        return null;
    }

    @Override
    public Integer getIsCurrent() {
        return null;
    }

    @Override
    public String getCurrentVersionId() {
        return null;
    }
}
