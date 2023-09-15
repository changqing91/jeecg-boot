package org.jeecg.modules.vision.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.oss.entity.OssFile;
import org.jeecg.modules.vision.entity.TemplateFilesSlot;
import org.jeecg.modules.vision.service.ITemplateFilesSlotService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;

/**
 * @Description: 素材库-模板素材坑位
 * @Author: jeecg-boot
 * @Date: 2023-07-19
 * @Version: V1.0
 */
//@Api(tags = "素材库-模板素材坑位")
@RestController
//@RequestMapping("/vision/templateFilesSlot")
@Slf4j
public class TemplateFilesSlotController extends JeecgController<TemplateFilesSlot, ITemplateFilesSlotService> {
    @Autowired
    private ITemplateFilesSlotService templateFilesSlotService;

    /**
     * 分页列表查询
     *
     * @param templateFilesSlot
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "素材库-模板素材坑位-分页列表查询")
//    @ApiOperation(value = "模板素材坑位-分页列表查询", notes = "模板素材坑位-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<TemplateFilesSlot>> queryPageList(TemplateFilesSlot templateFilesSlot,
                                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                          HttpServletRequest req) {
        QueryWrapper<TemplateFilesSlot> queryWrapper = QueryGenerator.initQueryWrapper(templateFilesSlot, req.getParameterMap());
        Page<TemplateFilesSlot> page = new Page<TemplateFilesSlot>(pageNo, pageSize);
        IPage<TemplateFilesSlot> pageList = templateFilesSlotService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

//    @ApiOperation(value = "模板素材坑位-根据模板Id查询", notes = "模板素材坑位-根据模板Id查询")
    @GetMapping(value = "/queryByTemplateId")
    public Result<List<TemplateFilesSlot>> queryByTemplateId(@RequestParam(name = "templateId") Integer templateId,
                                                             HttpServletRequest req) {
        TemplateFilesSlot slot = new TemplateFilesSlot();
        slot.setVisionVideoTemplateId(templateId);
        QueryWrapper<TemplateFilesSlot> queryWrapper = QueryGenerator.initQueryWrapper(slot, req.getParameterMap());
        slot.setVisionVideoTemplateId(templateId);
        List<TemplateFilesSlot> list = templateFilesSlotService.list(queryWrapper);
        return Result.OK(list);
    }

    /**
     * 添加
     *
     * @param templateFilesSlot
     * @return
     */
    @AutoLog(value = "模板素材坑位-添加")
//    @ApiOperation(value = "模板素材坑位-添加", notes = "模板素材坑位-添加")
    @RequiresPermissions("vision:vision_template_files_slot:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody TemplateFilesSlot templateFilesSlot) {
        templateFilesSlotService.save(templateFilesSlot);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param templateFilesSlot
     * @return
     */
    @AutoLog(value = "模板素材坑位-编辑")
//    @ApiOperation(value = "模板素材坑位-编辑", notes = "模板素材坑位-编辑")
    @RequiresPermissions("vision:vision_template_files_slot:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody TemplateFilesSlot templateFilesSlot) {
        templateFilesSlotService.updateById(templateFilesSlot);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "模板素材坑位-通过id删除")
//    @ApiOperation(value = "模板素材坑位-通过id删除", notes = "模板素材坑位-通过id删除")
    @RequiresPermissions("vision:vision_template_files_slot:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        templateFilesSlotService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "模板素材坑位-批量删除")
//    @ApiOperation(value = "模板素材坑位-批量删除", notes = "模板素材坑位-批量删除")
    @RequiresPermissions("vision:vision_template_files_slot:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.templateFilesSlotService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "模板素材坑位-通过id查询")
//    @ApiOperation(value = "模板素材坑位-通过id查询", notes = "模板素材坑位-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<TemplateFilesSlot> queryById(@RequestParam(name = "id", required = true) String id) {
        TemplateFilesSlot templateFilesSlot = templateFilesSlotService.getById(id);
        if (templateFilesSlot == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(templateFilesSlot);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param templateFilesSlot
     */
    @RequiresPermissions("vision:vision_template_files_slot:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TemplateFilesSlot templateFilesSlot) {
        return super.exportXls(request, templateFilesSlot, TemplateFilesSlot.class, "vision_template_files_slot");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("vision:vision_template_files_slot:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, TemplateFilesSlot.class);
    }

}
