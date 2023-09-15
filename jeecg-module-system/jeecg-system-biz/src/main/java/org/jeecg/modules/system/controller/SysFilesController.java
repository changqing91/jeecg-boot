package org.jeecg.modules.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.modules.system.entity.SysFiles;
import org.jeecg.modules.system.model.SysFilesTree;
import org.jeecg.modules.system.service.ISysFilesService;
import org.jeecg.modules.vision.entity.TemplateFilesSlot;
import org.jeecg.modules.vision.entity.VideoTemplate;
import org.jeecg.modules.vision.service.IVideoTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 素材库-素材管理
 * @Author: jeecg-boot
 * @Date: 2022-07-21
 * @Version: V1.0
 */
@Slf4j
@Api(tags = "素材库-素材管理")
@RestController
@RequestMapping("/sys/files")
public class SysFilesController extends JeecgController<SysFiles, ISysFilesService> {
    @Autowired
    private ISysFilesService sysFilesService;

    @Autowired
    private IVideoTemplateService videoTemplateService;

    /**
     * 分页列表查询
     *
     * @param sysFiles
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "素材管理-分页列表查询")
    @ApiOperation(value = "素材管理-分页列表查询", notes = "素材管理-分页列表查询")
    @GetMapping(value = "/list")
    public Result<?> queryPageList(SysFiles sysFiles,
                                   @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                   @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                   HttpServletRequest req) {
        QueryWrapper<SysFiles> queryWrapper = QueryGenerator.initQueryWrapper(sysFiles, req.getParameterMap());
        Page<SysFiles> page = new Page<SysFiles>(pageNo, pageSize);
        IPage<SysFiles> pageList = sysFilesService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param sysFiles
     * @return
     */
    @AutoLog(value = "素材管理-添加")
    @ApiOperation(value = "素材管理-添加", notes = "素材管理-添加")
    @PostMapping(value = "/add")
    public Result<?> add(@RequestBody SysFiles sysFiles) {
        sysFilesService.save(sysFiles);
        return Result.OK("添加成功！");
    }

    /**
     * 上传
     *
     * @param multipartFile
     * @param request
     * @return
     */
    @AutoLog(value = "素材管理-上传")
    @ApiOperation(value = "素材管理-上传", notes = "素材管理-上传")
    @PostMapping(value = "/upload")
    public Result upload(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request) {
        Result result = new Result();
        String username = JwtUtil.getUserNameByToken(request);
        try {
            String url = sysFilesService.upload(multipartFile, username);
            result.setResult(url);
            result.setCode(CommonConstant.SC_OK_200);
            result.OK("上传成功");
        } catch (Exception ex) {
            log.info(ex.getMessage(), ex);
            result.error500("上传失败");
        }
        return result;
    }

    /**
     * 编辑
     *
     * @param sysFiles
     * @return
     */
    @AutoLog(value = "素材管理-编辑")
    @ApiOperation(value = "素材管理-编辑", notes = "素材管理-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<?> edit(@RequestBody SysFiles sysFiles) {
        sysFilesService.updateById(sysFiles);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "素材管理-通过id删除")
    @ApiOperation(value = "素材管理-通过id删除", notes = "素材管理-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<?> delete(@RequestParam(name = "id", required = true) String id) {
        sysFilesService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "素材管理-批量删除")
    @ApiOperation(value = "素材管理-批量删除", notes = "素材管理-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<?> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.sysFilesService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功！");
    }

    /**
     * 通过id查询单个素材
     *
     * @param id
     * @return
     */
    @AutoLog(value = "素材管理-通过id查询单个素材")
    @ApiOperation(value = "素材管理-通过id查询单个素材", notes = "素材管理-通过id查询单个素材")
    @GetMapping(value = "/queryById")
    public Result<?> queryById(@RequestParam(name = "id", required = true) String id) {
        SysFiles sysFiles = sysFilesService.getById(id);
        return Result.OK(sysFiles);
    }

    /**
     * 通过模板id查询
     *
     * @param templateId
     * @return SysFilesTree
     */
    @AutoLog(value = "素材管理-通过模板id查询素材包")
    @ApiOperation(value = "素材管理-通过模板id查询素材包", notes = "素材管理-通过模板id查询素材包")
    @GetMapping(value = "/queryPackageByTemplateId")
    public Result<?> queryByTemplateId(@RequestParam(name = "templateId", required = true) Integer templateId, HttpServletRequest req) {
//        VideoTemplate videoTemplate = videoTemplateService.getById(templateId);
//        String filesId = videoTemplate.getFilesId();
//        SysFilesTree sysFilesTree = sysFilesService.getSysFilesTree(filesId);
//        return Result.OK(sysFilesTree);
        return null;
    }

    /**
     * 通过id查询素材包
     *
     * @param filesId
     * @return SysFilesTree
     */
    @AutoLog(value = "素材管理-通过id查询素材包")
    @ApiOperation(value = "素材管理-通过id查询素材包", notes = "素材管理-通过id查询素材包")
    @GetMapping(value = "/queryPackageById")
    public Result<?> queryPackageById(@RequestParam(name = "filesId", required = true) String filesId, HttpServletRequest req) {
        SysFilesTree sysFilesTree = sysFilesService.getSysFilesTree(filesId);
        return Result.OK(sysFilesTree);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param sysFiles
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SysFiles sysFiles) {
        return super.exportXls(request, sysFiles, SysFiles.class, "素材管理");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, SysFiles.class);
    }

}
