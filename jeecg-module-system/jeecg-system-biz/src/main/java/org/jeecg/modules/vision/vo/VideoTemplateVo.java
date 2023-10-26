package org.jeecg.modules.vision.vo;

import java.util.Date;

public interface VideoTemplateVo {
    // add getters
    String getId();

    String getName();

    String getDescription();

    String getAeProjectUrl();

    String getClipsJson();

    String getCoverImageUrl();

    String getMainRenderName();

    String getPreviewVideoUrl();

    Integer getWidth();

    Integer getHeight();

    String getVersion();

    String getAuthor();

    Integer getFps();

    String getBitRate();

    String getStatus();

    Integer getIsDelete();

    Integer getIsCurrent();

    String getCurrentVersionId();

    String getRatio();

    String getResolution();
}
