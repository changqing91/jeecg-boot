package org.jeecg.modules.vision.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: vision_video_template
 * @Author: jeecg-boot
 * @Date: 2023-04-24
 * @Version: V1.0
 */
@Data
@TableName("vision_video_template")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "vision_video_template对象", description = "vision_video_template")
public class VideoTemplate implements Serializable {
    private static final long serialVersionUID = 1L;

    /**id*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**模板名称*/
    @Excel(name = "模板名称", width = 15)
    @ApiModelProperty(value = "模板名称")
    private java.lang.String name;
    /**描述*/
    @Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
    private java.lang.String description;
    /**封面图片地址*/
    @Excel(name = "封面图片地址", width = 15)
    @ApiModelProperty(value = "封面图片地址")
    private java.lang.String coverImageUrl;
    /**主合成命名*/
    @Excel(name = "主合成命名", width = 15)
    @ApiModelProperty(value = "主合成命名")
    private java.lang.String mainRenderName;
    /**width*/
    @Excel(name = "width", width = 15)
    @ApiModelProperty(value = "width")
    private java.lang.Integer width;
    /**height*/
    @Excel(name = "height", width = 15)
    @ApiModelProperty(value = "height")
    private java.lang.Integer height;
    /**分辨率*/
    @Excel(name = "resolution", width = 15)
    @ApiModelProperty(value = "resolution")
    private java.lang.String resolution;
    /**比例：16:9*/
    @Excel(name = "ratio", width = 15)
    @ApiModelProperty(value = "ratio")
    private java.lang.String ratio;
    /**version*/
    @Excel(name = "version", width = 15)
    @ApiModelProperty(value = "version")
    private java.lang.String version;
    /**author*/
    @Excel(name = "author", width = 15)
    @ApiModelProperty(value = "author")
    private java.lang.String author;
    /**帧率*/
    @Excel(name = "帧率", width = 15)
    @ApiModelProperty(value = "帧率")
    private java.lang.Integer fps;
    /**bitRate*/
    @Excel(name = "bitRate", width = 15)
    @ApiModelProperty(value = "bitRate")
    private java.lang.String bitRate;
    /**Available：可用，有版本状态可生产视频
     Created：已创建，未产生版本
     Uploading：正在上传
     Processing：正在执行AE工程解析
     UploadFailed：上传失败
     ProcessFailed：解析失败*/
    @Excel(name = "Available：可用，有版本状态可生产视频；Created：已创建，未产生版本；Uploading：正在上传；Processing：正在执行AE工程解析；UploadFailed：上传失败；ProcessFailed：解析失败", width = 15)
    @ApiModelProperty(value = "Available：可用，有版本状态可生产视频；Created：已创建，未产生版本；Uploading：正在上传；Processing：正在执行AE工程解析；UploadFailed：上传失败；ProcessFailed：解析失败")
    private java.lang.String status;
    /**预览视频地址*/
    @Excel(name = "预览视频地址", width = 15)
    @ApiModelProperty(value = "预览视频地址")
    private java.lang.String previewVideoUrl;
    /**isPublish*/
    @Excel(name = "isPublish", width = 15)
    @ApiModelProperty(value = "isPublish")
    private java.lang.Integer isPublish;
    /**软删除*/
    @Excel(name = "软删除", width = 15)
    @ApiModelProperty(value = "软删除")
    private java.lang.Integer isDelete;
    /**ctime*/
    @Excel(name = "ctime", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "ctime")
    private java.util.Date ctime;
    /**mtime*/
    @Excel(name = "mtime", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "mtime")
    private java.util.Date mtime;
}
