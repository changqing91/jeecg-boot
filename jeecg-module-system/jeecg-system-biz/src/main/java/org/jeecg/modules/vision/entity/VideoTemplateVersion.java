package org.jeecg.modules.vision.entity;


import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: video_template_version
 * @Author: jeecg-boot
 * @Date:   2023-08-28
 * @Version: V1.0
 */
@Data
@TableName("vision_video_template_version")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="vision_video_template_version对象", description="video_template_version")
public class VideoTemplateVersion implements Serializable {
    private static final long serialVersionUID = 1L;

    /**id*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
    /**id*/
    @ApiModelProperty(value = "videoTemplateId")
    private java.lang.String videoTemplateId;
    @Excel(name = "aeProjectUrl", width = 15)
    @ApiModelProperty(value = "aeProjectUrl")
    private java.lang.String aeProjectUrl;
    /**动态卡槽配置json*/
    @Excel(name = "动态卡槽配置json", width = 15)
    @ApiModelProperty(value = "动态卡槽配置json")
    private java.lang.String clipsJson;
    /**version*/
    @Excel(name = "version", width = 15)
    @ApiModelProperty(value = "version")
    private java.lang.String version;
    /**author*/
    @Excel(name = "author", width = 15)
    @ApiModelProperty(value = "author")
    private java.lang.String author;
    /**Available：可用，有版本状态可生产视频
     Created：已创建，未产生版本
     Uploading：正在上传
     Processing：正在执行AE工程解析
     UploadFailed：上传失败
     ProcessFailed：解析失败*/
    @Excel(name = "Available：可用，有版本状态可生产视频；Created：已创建，未产生版本；Uploading：正在上传；Processing：正在执行AE工程解析；UploadFailed：上传失败；ProcessFailed：解析失败", width = 15)
    @ApiModelProperty(value = "Available：可用，有版本状态可生产视频；Created：已创建，未产生版本；Uploading：正在上传；Processing：正在执行AE工程解析；UploadFailed：上传失败；ProcessFailed：解析失败")
    private java.lang.String status;
    /**是否正在使用*/
    @Excel(name = "是否正在使用", width = 15)
    @ApiModelProperty(value = "是否正在使用")
    private java.lang.Integer isCurrent;
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
