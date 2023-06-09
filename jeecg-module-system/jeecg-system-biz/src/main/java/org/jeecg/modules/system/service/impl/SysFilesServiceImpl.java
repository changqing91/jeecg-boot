package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.CommonUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.common.util.oss.OssBootUtil;
import org.jeecg.modules.system.entity.SysFiles;
import org.jeecg.modules.system.mapper.SysFilesMapper;
import org.jeecg.modules.system.service.ISysFilesService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;


/**
 * @Description: 知识库-文档管理
 * @Author: jeecg-boot
 * @Date: 2022-07-21
 * @Version: V1.0
 */
@Service
public class SysFilesServiceImpl extends ServiceImpl<SysFilesMapper, SysFiles> implements ISysFilesService {

    @Override
    public void upload(MultipartFile multipartFile, String username) throws Exception {
        String fileName = multipartFile.getOriginalFilename();
        fileName = CommonUtils.getFileName(fileName);
        SysFiles sysFiles = new SysFiles();
        sysFiles.setFileName(fileName);
        String url = OssBootUtil.upload(multipartFile,"upload/audios");
        if(oConvertUtils.isEmpty(url)){
            throw new JeecgBootException("上传文件失败! ");
        }
        //update-begin--Author:scott  Date:20201227 for：JT-361【文件预览】阿里云原生域名可以文件预览，自己映射域名kkfileview提示文件下载失败-------------------
        // 返回阿里云原生域名前缀URL
//        sysFiles.setUrl(OssBootUtil.getOriginalUrl(url));
        sysFiles.setUrl(url);
        sysFiles.setCreateBy(username);
        sysFiles.setCreateTime(new Date(System.currentTimeMillis()));
        sysFiles.setFileSize(Double.valueOf(multipartFile.getSize()));
        sysFiles.setFileType(multipartFile.getContentType());
        sysFiles.setEnableDown("1");
        //update-end--Author:scott  Date:20201227 for：JT-361【文件预览】阿里云原生域名可以文件预览，自己映射域名kkfileview提示文件下载失败-------------------
        this.save(sysFiles);
    }
}
