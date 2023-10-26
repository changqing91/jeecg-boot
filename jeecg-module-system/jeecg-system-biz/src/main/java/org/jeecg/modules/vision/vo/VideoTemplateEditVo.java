package org.jeecg.modules.vision.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class VideoTemplateEditVo implements VideoTemplateVo {
    @NotBlank(message = "id is required")
    @ApiModelProperty(value = "模板ID", required = true)
    private String id;
    @NotBlank(message = "name is required")
    @ApiModelProperty(value = "模板名称", required = true)
    private String name;
    @NotBlank(message = "description is required")
    @ApiModelProperty(value = "描述")
    private String description;
    @NotBlank(message = "aeProjectUrl is required")
    @ApiModelProperty(value = "AE工程文件URL", required = true)
    private String aeProjectUrl;
    @ApiModelProperty(value = "模板配置JSON")
    private String clipsJson;
    @ApiModelProperty(value = "封面图URL")
    private String coverImageUrl;
    @ApiModelProperty(value = "主合成名称", required = true)
    private String mainRenderName;
    @NotBlank(message = "previewVideoUrl is required")
    @ApiModelProperty(value = "预览视频URL", required = true)
    private String previewVideoUrl;
    @ApiModelProperty(value = "宽度")
    private Integer width;
    @ApiModelProperty(value = "高度")
    private Integer height;
    @NotBlank(message = "resolution is required")
    @ApiModelProperty(value = "分辨率")
    private String resolution;
    @NotBlank(message = "ratio is required")
    @ApiModelProperty(value = "比例")
    private String ratio;
    @ApiModelProperty(value = "版本号")
    private String version;
    @NotBlank(message = "author is required")
    @ApiModelProperty(value = "作者", required = true)
    private String author;
    @NotNull(message = "fps is required")
    @ApiModelProperty(value = "帧率")
    private Integer fps;
    @ApiModelProperty(value = "比特率")
    private String bitRate;
    @ApiModelProperty(value = "状态")
    private String status;
    @ApiModelProperty(value = "是否删除")
    private Integer isDelete;
    @ApiModelProperty(value = "是否当前版本")
    private Integer isCurrent;


    @Override
    public String getCurrentVersionId() {
        return null;
    }
}
