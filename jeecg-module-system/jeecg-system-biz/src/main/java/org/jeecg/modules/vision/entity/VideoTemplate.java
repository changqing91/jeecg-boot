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

    /**
     * 自增Id
     */
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "自增Id")
    private java.lang.Integer id;
    /**
     * title
     */
    @Excel(name = "title", width = 15)
    @ApiModelProperty(value = "title")
    private java.lang.String title;
    /**
     * code
     */
    @Excel(name = "code", width = 15)
    @ApiModelProperty(value = "title")
    private java.lang.String code;
    /**
     * videoUrl
     */
    @Excel(name = "videoUrl", width = 15)
    @ApiModelProperty(value = "videoUrl")
    private java.lang.String videoUrl;
    /**
     * coverImage
     */
    @Excel(name = "coverImage", width = 15)
    @ApiModelProperty(value = "coverImage")
    private java.lang.String coverImage;
    /**
     * description
     */
    @Excel(name = "description", width = 15)
    @ApiModelProperty(value = "description")
    private java.lang.String description;
    /**
     * shareConfig
     */
    @Excel(name = "shareConfig", width = 15)
    @ApiModelProperty(value = "shareConfig")
    private java.lang.String shareConfig;
    /**
     * pages
     */
    @Excel(name = "pages", width = 15)
    @ApiModelProperty(value = "pages")
    private java.lang.String pages;
    /**
     * script
     */
    @Excel(name = "script", width = 15)
    @ApiModelProperty(value = "script")
    private java.lang.String script;
    /**
     * author
     */
    @Excel(name = "author", width = 15)
    @ApiModelProperty(value = "author")
    private java.lang.String author;
    /**
     * ratio
     */
    @Excel(name = "ratio", width = 15)
    @ApiModelProperty(value = "ratio")
    private java.lang.String ratio;
    /**
     * width
     */
    @Excel(name = "width", width = 15)
    @ApiModelProperty(value = "width")
    private java.lang.Integer width;
    /**
     * height
     */
    @Excel(name = "height", width = 15)
    @ApiModelProperty(value = "height")
    private java.lang.Integer height;
    /**
     * pageMode
     */
    @Excel(name = "pageMode", width = 15)
    @ApiModelProperty(value = "pageMode")
    private java.lang.String pageMode;
    /**
     * flipType
     */
    @Excel(name = "flipType", width = 15)
    @ApiModelProperty(value = "flipType")
    private java.lang.Integer flipType;
    /**
     * slideNumber
     */
    @Excel(name = "slideNumber", width = 15)
    @ApiModelProperty(value = "slideNumber")
    private java.lang.Integer slideNumber;
    /**
     * state
     */
    @Excel(name = "state", width = 15)
    @ApiModelProperty(value = "state")
    private java.lang.Integer state;
    /**
     * isPublish
     */
    @Excel(name = "isPublish", width = 15)
    @ApiModelProperty(value = "isPublish")
    private java.lang.Integer isPublish;
    /**
     * isTemplate
     */
    @Excel(name = "isTemplate", width = 15)
    @ApiModelProperty(value = "isTemplate")
    private java.lang.Integer isTemplate;
    /**
     * members
     */
    @Excel(name = "members", width = 15)
    @ApiModelProperty(value = "members")
    private java.lang.String members;
    /**
     * version
     */
    @Excel(name = "version", width = 15)
    @ApiModelProperty(value = "version")
    private java.lang.String version;
    /**
     * 软删除
     */
    @Excel(name = "软删除", width = 15)
    @ApiModelProperty(value = "软删除")
    private java.lang.Integer isdelete;
    /**
     * 商品数量
     */
    @Excel(name = "商品数量", width = 15)
    @ApiModelProperty(value = "商品数量")
    private java.lang.Integer goodsCount;
    /**
     * 提交步骤
     */
    @Excel(name = "提交步骤", width = 15)
    @ApiModelProperty(value = "提交步骤")
    private java.lang.Integer step;
    /**
     * 素材Id
     */
    @Excel(name = "素材Id", width = 15)
    @ApiModelProperty(value = "素材Id")
    private String filesId;
    /**
     * ctime
     */
    @Excel(name = "ctime", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "ctime")
    private java.util.Date ctime;
    /**
     * mtime
     */
    @Excel(name = "mtime", width = 15, format = "yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "mtime")
    private java.util.Date mtime;
}
