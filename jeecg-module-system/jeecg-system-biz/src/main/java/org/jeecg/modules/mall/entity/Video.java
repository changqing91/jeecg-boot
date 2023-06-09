package org.jeecg.modules.mall.entity;

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
 * @Description: mall_video
 * @Author: jeecg-boot
 * @Date:   2023-04-24
 * @Version: V1.0
 */
@Data
@TableName("mall_video")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="mall_video对象", description="mall_video")
public class Video implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
	/**商品ID*/
	@Excel(name = "商品ID", width = 15)
    @ApiModelProperty(value = "商品ID")
    private java.lang.Integer spuId;
	/**ctime*/
	@Excel(name = "ctime", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "ctime")
    private java.util.Date ctime;
	/**mtime*/
	@Excel(name = "mtime", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "mtime")
    private java.util.Date mtime;
	/**商品名称*/
	@Excel(name = "商品名称", width = 15)
    @ApiModelProperty(value = "商品名称")
    private java.lang.String spuName;
	/**视频地址*/
	@Excel(name = "视频地址", width = 15)
    @ApiModelProperty(value = "视频地址")
    private java.lang.String videoUrl;
	/**视频模板ID*/
	@Excel(name = "视频模板ID", width = 15)
    @ApiModelProperty(value = "视频模板ID")
    private java.lang.Integer templateId;
	/**视频模板名称*/
	@Excel(name = "视频模板名称", width = 15)
    @ApiModelProperty(value = "视频模板名称")
    private java.lang.String templateName;
	/**视频渠道*/
	@Excel(name = "视频渠道", width = 15)
    @ApiModelProperty(value = "视频渠道")
    private java.lang.String videoChannel;
}
