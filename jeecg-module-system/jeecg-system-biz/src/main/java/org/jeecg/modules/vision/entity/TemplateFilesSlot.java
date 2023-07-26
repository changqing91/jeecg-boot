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
 * @Description: vision_template_files_slot
 * @Author: jeecg-boot
 * @Date:   2023-07-19
 * @Version: V1.0
 */
@Data
@TableName("vision_template_files_slot")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="vision_template_files_slot对象", description="vision_template_files_slot")
public class TemplateFilesSlot implements Serializable {
    private static final long serialVersionUID = 1L;

    /**id*/
    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**sysFilesId*/
    @Excel(name = "sysFilesId", width = 15)
    @ApiModelProperty(value = "sysFilesId")
    private java.lang.String sysFilesId;
    /**visionVideoTemplateId*/
    @Excel(name = "visionVideoTemplateId", width = 15)
    @ApiModelProperty(value = "visionVideoTemplateId")
    private java.lang.Integer visionVideoTemplateId;
    /**name*/
    @Excel(name = "name", width = 15)
    @ApiModelProperty(value = "name")
    private java.lang.String name;
    /**remark*/
    @Excel(name = "remark", width = 15)
    @ApiModelProperty(value = "remark")
    private java.lang.String remark;
}
