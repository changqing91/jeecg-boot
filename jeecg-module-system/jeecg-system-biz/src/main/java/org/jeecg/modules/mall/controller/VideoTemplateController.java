package org.jeecg.modules.mall.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.mall.entity.VideoTemplate;
import org.jeecg.modules.mall.service.IVideoTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;

 /**
 * @Description: 视频模板
 * @Author: jeecg-boot
 * @Date:   2023-04-24
 * @Version: V1.0
 */
@Api(tags="电商视频模板")
@RestController
@RequestMapping("/mall/videoTemplate")
@Slf4j
public class VideoTemplateController extends JeecgController<VideoTemplate, IVideoTemplateService> {
	@Autowired
	private IVideoTemplateService videoTemplateService;
	
	/**
	 * 分页列表查询
	 *
	 * @param videoTemplate
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "视频模板-分页列表查询")
	@ApiOperation(value="视频模板-分页列表查询", notes="视频模板-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<VideoTemplate>> queryPageList(VideoTemplate videoTemplate,
																						 @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
																						 @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
																						 HttpServletRequest req) {
		QueryWrapper<VideoTemplate> queryWrapper = QueryGenerator.initQueryWrapper(videoTemplate, req.getParameterMap());
		Page<VideoTemplate> page = new Page<VideoTemplate>(pageNo, pageSize);
		IPage<VideoTemplate> pageList = videoTemplateService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param videoTemplate
	 * @return
	 */
	@AutoLog(value = "视频模板-添加")
	@ApiOperation(value="视频模板-添加", notes="视频模板-添加")
//	@RequiresPermissions("mall:视频模板:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody VideoTemplate videoTemplate) {
		videoTemplateService.save(videoTemplate);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param videoTemplate
	 * @return
	 */
	@AutoLog(value = "视频模板-编辑")
	@ApiOperation(value="视频模板-编辑", notes="视频模板-编辑")
//	@RequiresPermissions("mall:视频模板:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody VideoTemplate videoTemplate) {
		videoTemplateService.updateById(videoTemplate);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "视频模板-通过id删除")
	@ApiOperation(value="视频模板-通过id删除", notes="视频模板-通过id删除")
//	@RequiresPermissions("mall:视频模板:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		videoTemplateService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "视频模板-批量删除")
	@ApiOperation(value="视频模板-批量删除", notes="视频模板-批量删除")
//	@RequiresPermissions("mall:视频模板:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.videoTemplateService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "视频模板-通过id查询")
	@ApiOperation(value="视频模板-通过id查询", notes="视频模板-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<VideoTemplate> queryById(@RequestParam(name="id",required=true) String id) {
		VideoTemplate videoTemplate = videoTemplateService.getById(id);
		if(videoTemplate==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(videoTemplate);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param videoTemplate
    */
//    @RequiresPermissions("mall:视频模板:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, VideoTemplate videoTemplate) {
        return super.exportXls(request, videoTemplate, VideoTemplate.class, "视频模板");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
//    @RequiresPermissions("mall:视频模板:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, VideoTemplate.class);
    }

}
