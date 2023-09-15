package org.jeecg.modules.vision.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.vision.entity.VideoTemplateVersion;
import org.jeecg.modules.vision.service.IVideoTemplateVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @Description: video_template_version
 * @Author: jeecg-boot
 * @Date:   2023-08-28
 * @Version: V1.0
 */
//@Api(tags="video_template_version")
@RestController
@RequestMapping("/vision/videoTemplateVersion")
@Slf4j
public class VideoTemplateVersionController extends JeecgController<VideoTemplateVersion, IVideoTemplateVersionService> {
    @Autowired
    private IVideoTemplateVersionService videoTemplateVersionService;

    /**
     * 分页列表查询
     *
     * @param videoTemplateVersion
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    @AutoLog(value = "video_template_version-分页列表查询")
//    @ApiOperationApiOperation(value="video_template_version-分页列表查询", notes="video_template_version-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<VideoTemplateVersion>> queryPageList(VideoTemplateVersion videoTemplateVersion,
                                                             @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                             @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                             HttpServletRequest req) {
        QueryWrapper<VideoTemplateVersion> queryWrapper = QueryGenerator.initQueryWrapper(videoTemplateVersion, req.getParameterMap());
        Page<VideoTemplateVersion> page = new Page<VideoTemplateVersion>(pageNo, pageSize);
        IPage<VideoTemplateVersion> pageList = videoTemplateVersionService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 查询一个状态为Created的模板版本
     *
     * @return
     */
    @AutoLog(value = "video_template_version-查询一个状态为Created的模板版本")
    @GetMapping(value = "/queryFirstCreatedVersion")
    public Result<IPage<VideoTemplateVersion>> queryFirstCreatedVersion() {
        QueryWrapper<VideoTemplateVersion> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", "Created");
        Page<VideoTemplateVersion> page = new Page<VideoTemplateVersion>(1, 1);
        IPage<VideoTemplateVersion> pageList = videoTemplateVersionService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     *   添加
     *
     * @param videoTemplateVersion
     * @return
     */
    @AutoLog(value = "video_template_version-添加")
//    @ApiOperation(value="video_template_version-添加", notes="video_template_version-添加")
    @RequiresPermissions("vision:vision_video_template_version:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody VideoTemplateVersion videoTemplateVersion) {
        videoTemplateVersionService.save(videoTemplateVersion);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     *
     * @param videoTemplateVersion
     * @return
     */
    @AutoLog(value = "video_template_version-编辑")
//    @ApiOperation(value="video_template_version-编辑", notes="video_template_version-编辑")
//    @RequiresPermissions("vision:vision_video_template_version:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<String> edit(@RequestBody VideoTemplateVersion videoTemplateVersion) {
        videoTemplateVersionService.updateById(videoTemplateVersion);
        return Result.OK("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "video_template_version-通过id删除")
//    @ApiOperation(value="video_template_version-通过id删除", notes="video_template_version-通过id删除")
    @RequiresPermissions("vision:vision_video_template_version:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name="id",required=true) String id) {
        videoTemplateVersionService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     *  批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "video_template_version-批量删除")
//    @ApiOperation(value="video_template_version-批量删除", notes="video_template_version-批量删除")
    @RequiresPermissions("vision:vision_video_template_version:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        this.videoTemplateVersionService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "video_template_version-通过id查询")
//    @ApiOperation(value="video_template_version-通过id查询", notes="video_template_version-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<VideoTemplateVersion> queryById(@RequestParam(name="id",required=true) String id) {
        VideoTemplateVersion videoTemplateVersion = videoTemplateVersionService.getById(id);
        if(videoTemplateVersion==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(videoTemplateVersion);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param videoTemplateVersion
     */
    @RequiresPermissions("vision:vision_video_template_version:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, VideoTemplateVersion videoTemplateVersion) {
        return super.exportXls(request, videoTemplateVersion, VideoTemplateVersion.class, "video_template_version");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("vision:vision_video_template_version:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, VideoTemplateVersion.class);
    }

}