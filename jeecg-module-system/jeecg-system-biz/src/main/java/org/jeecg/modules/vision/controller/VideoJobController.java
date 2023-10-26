package org.jeecg.modules.vision.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.UUIDGenerator;
import org.jeecg.modules.vision.entity.VideoJob;
import org.jeecg.modules.vision.service.IVideoJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * @Description: 视频-任务管理
 * @Author: jeecg-boot
 * @Date: 2023-04-24
 * @Version: V1.0
 */
//@Api(tags = "视频-任务管理")
//@RestController
//@RequestMapping("/vision/task")
@Slf4j
public class VideoJobController extends JeecgController<VideoJob, IVideoJobService> {
    @Autowired
    private IVideoJobService taskService;


    /**
     * 分页列表查询
     *
     * @param task
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "任务分页列表查询")
//    @ApiOperation(value = "任务分页列表查询", notes = "任务分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<VideoJob>> queryPageList(VideoJob task,
                                                 @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                 @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                 HttpServletRequest req) {
        QueryWrapper<VideoJob> queryWrapper = QueryGenerator.initQueryWrapper(task, req.getParameterMap());
        Page<VideoJob> page = new Page<VideoJob>(pageNo, pageSize);
        IPage<VideoJob> pageList = taskService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param task
     * @return
     */
    @AutoLog(value = "任务添加")
//    @ApiOperation(value = "任务添加", notes = "任务添加")
    @PostMapping(value = "/add")
    public Result<VideoJob> add(@RequestBody VideoJob task) {
        String id = UUIDGenerator.generate();
        task.setId(id);
        taskService.save(task);
        return Result.OK(task);
    }

    /**
     * 编辑
     *
     * @param task
     * @return
     */
    @AutoLog(value = "任务编辑")
//    @ApiOperation(value = "任务编辑", notes = "任务编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody VideoJob task) {
        taskService.updateById(task);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "任务通过id删除")
//    @ApiOperation(value = "任务通过id删除", notes = "任务通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        taskService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "任务批量删除")
//    @ApiOperation(value = "任务批量删除", notes = "任务批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.taskService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "任务通过id查询")
//    @ApiOperation(value = "任务通过id查询", notes = "任务通过id查询")
    @GetMapping(value = "/queryById")
    public Result<VideoJob> queryById(@RequestParam(name = "id", required = true) String id) {
        VideoJob task = taskService.getById(id);
        if (task == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(task);
    }
}
