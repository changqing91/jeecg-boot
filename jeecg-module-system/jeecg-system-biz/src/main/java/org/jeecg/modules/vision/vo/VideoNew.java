package org.jeecg.modules.vision.vo;

import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.List;

@Data
public class VideoNew {

    @Excel(name = "模板ID", width = 20)
    private Integer templateId;

    @Excel(name = "商品ids", width = 20)
    private List<String> spuIds;
}
