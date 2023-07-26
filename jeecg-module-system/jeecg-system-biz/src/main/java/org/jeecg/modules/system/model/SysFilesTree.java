package org.jeecg.modules.system.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.modules.system.entity.SysFiles;

import java.util.ArrayList;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SysFilesTree {
    private String id;
    private String fileName;
    private String url;
    private String createBy;
    private Date createTime;
    private String updateBy;
    private Date updateTime;
    private String fileType;
    private String storeType;
    private String parentId;
    private String tenantId;
    private Double fileSize;
    private String izFolder;
    private String izRootFolder;
    private String izStar;
    private Integer downCount;
    private Integer readCount;
    private String shareUrl;
    private String sharePerms;
    private String enableDown;
    private String enableUpdat;
    private String delFlag;
    private String userData;
    private String realname;
    private String zipName;
    private ArrayList<SysFiles> children;

    // constructor
    public SysFilesTree(SysFiles node) {
        this.id = node.getId();
        this.fileName = node.getFileName();
        this.url = node.getUrl();
        this.createBy = node.getCreateBy();
        this.createTime = node.getCreateTime();
        this.updateBy = node.getUpdateBy();
        this.updateTime = node.getUpdateTime();
        this.fileType = node.getFileType();
        this.storeType = node.getStoreType();
        this.parentId = node.getParentId();
        this.tenantId = node.getTenantId();
        this.fileSize = node.getFileSize();
        this.izFolder = node.getIzFolder();
        this.izRootFolder = node.getIzRootFolder();
        this.izStar = node.getIzStar();
        this.downCount = node.getDownCount();
        this.readCount = node.getReadCount();
        this.shareUrl = node.getShareUrl();
        this.sharePerms = node.getSharePerms();
        this.enableDown = node.getEnableDown();
        this.enableUpdat = node.getEnableUpdat();
        this.delFlag = node.getDelFlag();
        this.userData = node.getUserData();
        this.realname = node.getRealname();
        this.zipName = node.getZipName();
    }

}
