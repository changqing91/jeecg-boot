package org.jeecg.modules.vision.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: vision_task
 * @Author: jeecg-boot
 * @Date:   2023-04-24
 * @Version: V1.0
 */
@Data
@TableName("vision_video_job")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="vision_video_job对象", description="vision_video_job")
public class VideoJob implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    @ApiModelProperty(value = "videoTemplateId")
    private java.lang.String videoTemplateId;
    @ApiModelProperty(value = "动态卡槽配置json")
    private java.lang.String clipsJson;
    @ApiModelProperty(value = "author")
    private java.lang.String author;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "ctime")
    private java.util.Date ctime;
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "mtime")
    private java.util.Date mtime;
    @ApiModelProperty(value = "Created：已创建，未合成；Processing：正在合成；Done: 完成")
    private java.lang.String status;
}
