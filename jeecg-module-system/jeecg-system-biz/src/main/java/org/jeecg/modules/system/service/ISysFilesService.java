package org.jeecg.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.system.entity.SysFiles;
import org.jeecg.modules.system.model.SysFilesTree;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Description: 知识库-文档管理
 * @Author: jeecg-boot
 * @Date: 2022-07-21
 * @Version: V1.0
 */
public interface ISysFilesService extends IService<SysFiles> {

    String upload(MultipartFile multipartFile, String username) throws Exception;

    SysFilesTree getSysFilesTree(String sysFilesId);
}
