package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysFiles;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description: 知识库-文档管理
 * @Author: jeecg-boot
 * @Date: 2022-07-21
 * @Version: V1.0
 */
public interface ISysFilesService extends IService<SysFiles> {

    void upload(MultipartFile multipartFile, String username) throws Exception;
}
