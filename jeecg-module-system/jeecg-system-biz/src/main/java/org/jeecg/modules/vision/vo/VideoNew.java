package org.jeecg.modules.vision.vo;

import lombok.Data;
import net.sf.json.JSONArray;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.util.List;

@Data
public class VideoNew {

    @Excel(name = "模板ID", width = 20)
    private String templateId;

    @Excel(name = "动态卡槽配置json", width = 20)
    private JSONArray clipsJson;
}
