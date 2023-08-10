package org.jeecg.modules.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.common.exception.JeecgBootException;
import org.jeecg.common.util.CommonUtils;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.common.util.oss.OssBootUtil;
import org.jeecg.modules.system.entity.SysFiles;
import org.jeecg.modules.system.mapper.SysFilesMapper;
import org.jeecg.modules.system.model.SysFilesTree;
import org.jeecg.modules.system.service.ISysFilesService;
import org.jeecg.modules.system.vo.SysFilesVo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static cn.hutool.core.bean.BeanUtil.isNotEmpty;


/**
 * @Description: 知识库-文档管理
 * @Author: jeecg-boot
 * @Date: 2022-07-21
 * @Version: V1.0
 */
@Service
public class SysFilesServiceImpl extends ServiceImpl<SysFilesMapper, SysFiles> implements ISysFilesService {

    @Override
    public String upload(MultipartFile multipartFile, String username) throws Exception {
        String fileName = multipartFile.getOriginalFilename();
        fileName = CommonUtils.getFileName(fileName);
        SysFiles sysFiles = new SysFiles();
        sysFiles.setFileName(fileName);
        String url = OssBootUtil.upload(multipartFile, "upload/audios");
        if (oConvertUtils.isEmpty(url)) {
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
        return url;
    }

    private List<SysFilesTree> getSysFiles(SysFiles sysFiles) {
        List<SysFilesTree> children = new ArrayList<>();
        QueryWrapper<SysFiles> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", sysFiles.getId());
        List<SysFiles> sysFilesList = this.list(queryWrapper);
        for (SysFiles sf : sysFilesList) {
            if (null != sf) {
                if (sf.getIzFolder() == "1") {
                    // 递归
                    List<SysFilesTree> sysFilesTreeList = getSysFiles(sf);
                    SysFilesTree sft = new SysFilesTree(sf);
                    sft.setChildren(sysFilesTreeList);
                    children.add(sft);
                } else {
                    SysFilesTree sft = new SysFilesTree(sf);
                    children.add(sft);
                }
            }
        }
        return children;
    }

    @Override
    // 根据sysFilesId获取文件树
    public SysFilesTree getSysFilesTree(String sysFilesId) {
        SysFiles sysFiles = this.getOne(new QueryWrapper<SysFiles>().eq("id", sysFilesId));
        SysFilesTree sysFilesTree = new SysFilesTree(sysFiles);
        sysFilesTree.setChildren(this.getSysFiles(sysFiles));
        return sysFilesTree;
    }
}
