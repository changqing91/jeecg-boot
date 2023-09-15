package org.jeecg.modules.vision.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class VideoTemplateEditVo implements VideoTemplateVo {
    @NotNull(message = "id不能为空")
    private String id;
    @NotNull(message = "name不能为空")
    private String name;
    @NotNull(message = "description不能为空")
    private String description;
    @NotNull(message = "aeProjectUrl不能为空")
    private String aeProjectUrl;
    @NotNull(message = "clipsJson不能为空")
    private String clipsJson;
    private String coverImageUrl;
    private String mainRenderName;
    @NotNull(message = "previewVideoUrl不能为空")
    private String previewVideoUrl;
    @NotNull(message = "width不能为空")
    private Integer width;
    @NotNull(message = "height不能为空")
    private Integer height;
    private String version;
    @NotNull(message = "author不能为空")
    private String author;
    @NotNull(message = "fps不能为空")
    private Integer fps;
    @NotNull(message = "bitRate不能为空")
    private String bitRate;

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
