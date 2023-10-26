package org.jeecg.modules.vision.vo;

import lombok.Data;
import org.jeecg.modules.vision.entity.VideoTemplateVersion;

@Data
public class VideoJobVo {
    private String id;
    private String status;
    private VideoTemplateVersion videoTemplateVersion;
    private String clipsJson;
}
