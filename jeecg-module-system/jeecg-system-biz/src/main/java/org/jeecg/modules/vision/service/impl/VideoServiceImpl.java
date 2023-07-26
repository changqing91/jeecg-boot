package org.jeecg.modules.vision.service.impl;

import org.jeecg.modules.vision.entity.Video;
import org.jeecg.modules.vision.mapper.VideoMapper;
import org.jeecg.modules.vision.service.IVideoService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: vision_video
 * @Author: jeecg-boot
 * @Date:   2023-04-24
 * @Version: V1.0
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements IVideoService {

}
