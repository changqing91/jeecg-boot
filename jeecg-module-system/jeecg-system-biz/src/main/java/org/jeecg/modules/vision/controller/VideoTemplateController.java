package org.jeecg.modules.vision.controller;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.gson.Gson;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.config.mqtoken.UserTokenContext;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.query.QueryGenerator;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.util.UUIDGenerator;
import org.jeecg.common.util.oss.OssBootUtil;
import org.jeecg.modules.system.service.ISysFilesService;
import org.jeecg.modules.vision.entity.VideoTemplate;
import org.jeecg.modules.vision.entity.VideoTemplateVersion;
import org.jeecg.modules.vision.service.IVideoTemplateService;
import org.jeecg.modules.vision.service.IVideoTemplateVersionService;
import org.jeecg.modules.vision.utils.DownloadUtil;
import org.jeecg.modules.vision.utils.GenerateToken;
import org.jeecg.modules.vision.utils.VideoUtil;
import org.jeecg.modules.vision.vo.VideoTemplateAddVo;
import org.jeecg.modules.vision.vo.VideoTemplateEditVo;
import org.jeecg.modules.vision.vo.VideoTemplateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 素材库-视频模板
 * @Author: jeecg-boot
 * @Date: 2023-04-24
 * @Version: V1.0
 */
@Api(tags = "素材库-视频模板")
@RestController
@RequestMapping("/vision/videoTemplate")
@Slf4j
public class VideoTemplateController extends JeecgController<VideoTemplate, IVideoTemplateService> {
    @Autowired
    private IVideoTemplateService videoTemplateService;

    @Autowired
    private IVideoTemplateVersionService videoTemplateVersionService;

    private static String FIRST_VERSION = "1.0.0";
    private static String DEFAULT_MAIN_RENDER_NAME = "总合成";
    private static String FIRST_PAINT_PATH = System.getProperty("java.io.tmpdir") + "firstPaint" + File.separator;
    private static String FIRST_PAINT_OSS = "first_painting";
    private static String TEMPLATE_ASSETS_OSS = "template_assets";

    /**
     * create enum, value is string
     * <p>
     * Available：可用，有版本状态可生产视频
     * Created：已创建，未产生版本
     * Uploading：正在上传
     * Processing：正在执行AE工程解析
     * UploadFailed：上传失败
     * ProcessFailed：解析失败
     */
    public enum StatusEnum {
        Available("Available"), Created("Created"), Uploading("Uploading"), Processing("Processing"), UploadFailed("UploadFailed"), ProcessFailed("ProcessFailed");

        private final String value;

        StatusEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 分页列表查询
     *
     * @param videoTemplate
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "素材库-视频模板-分页列表查询")
    @ApiOperation(value = "视频模板-分页列表查询", notes = "视频模板-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<VideoTemplate>> queryPageList(VideoTemplate videoTemplate,
                                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                      HttpServletRequest req) {
        QueryWrapper<VideoTemplate> queryWrapper = QueryGenerator.initQueryWrapper(videoTemplate, req.getParameterMap());
        Page<VideoTemplate> page = new Page<VideoTemplate>(pageNo, pageSize);
        IPage<VideoTemplate> pageList = videoTemplateService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    private String getFirstPaintFromVideoUrl(String videoUrl) {
        try {
            String firstPaintUrl = "";
            if (!StringUtils.isEmpty(videoUrl)) {
                // 如果没有FIRST_PAINT_PATH目录，创建目录
                File dir = new File(FIRST_PAINT_PATH);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                String imagePath = FIRST_PAINT_PATH + UUIDGenerator.generate() + ".jpg";
                String videoPath = FIRST_PAINT_PATH + UUIDGenerator.generate() + ".mp4";
                DownloadUtil.downloadFile(videoUrl, videoPath);
                VideoUtil.getVideoFirstFrame(videoPath, imagePath);
                File file = new File(imagePath);
                FileInputStream input = new FileInputStream(file);
                MultipartFile multipartFile = new MockMultipartFile(file.getName(), input);
                firstPaintUrl = OssBootUtil.upload(multipartFile, FIRST_PAINT_OSS);
                file.delete();
                new File(videoPath).delete();
            }
            return firstPaintUrl;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 添加
     *
     * @param videoTemplateAddVo
     * @return
     */
    @AutoLog(value = "视频模板-添加")
    @ApiOperation(value = "视频模板-添加", notes = "视频模板-添加")
//	@RequiresPermissions("mall:视频模板:add")
    @PostMapping(value = "/add")
    public Result<VideoTemplate> add(@RequestBody VideoTemplateAddVo videoTemplateAddVo) {
        try {
            VideoTemplate vt = new VideoTemplate();
            copyVideoTemplateProperties(videoTemplateAddVo, vt);
            String videoUrl = videoTemplateAddVo.getPreviewVideoUrl();
            if (!StringUtils.isEmpty(videoUrl)) {
                String firstPaintUrl = getFirstPaintFromVideoUrl(videoUrl);
                vt.setCoverImageUrl(firstPaintUrl);
            }
            vt.setVersion(FIRST_VERSION);
            vt.setStatus(StatusEnum.Created.getValue());
            vt.setIsDelete(0);
            vt.setIsPublish(0);
            vt.setMtime(new Date());
            vt.setCtime(new Date());
            videoTemplateService.save(vt);

            VideoTemplateVersion vtv = new VideoTemplateVersion();
            copyVideoTemplateVersionProperties(videoTemplateAddVo, vtv);
            vtv.setVideoTemplateId(vt.getId());
            vtv.setVersion(FIRST_VERSION);
            vtv.setIsCurrent(1);
            vtv.setStatus(StatusEnum.Created.getValue());
            videoTemplateVersionService.save(vtv);

            return Result.OK(vt);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }


    /**
     * 文件上传
     *
     * @param multipartFile
     * @return
     */
    @AutoLog(value = "视频模板-文件上传")
    @ApiOperation(value = "视频模板-文件上传", notes = "视频模板-文件上传")
//	@RequiresPermissions("mall:视频模板:add")
    @PostMapping(value = "/upload")
    public Result upload(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request) {
        try {
            Result result = new Result();
            String url = OssBootUtil.upload(multipartFile, TEMPLATE_ASSETS_OSS);
            result.setMessage("上传成功");
            result.setSuccess(true);
            result.setResult(url);
            result.setCode(CommonConstant.SC_OK_200);
            return result;
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 编辑
     *
     * @param videoTemplateEditVo
     * @return
     */
    @AutoLog(value = "视频模板-编辑")
    @ApiOperation(value = "视频模板-编辑", notes = "视频模板-编辑")
//	@RequiresPermissions("mall:视频模板:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@Validated @RequestBody VideoTemplateEditVo videoTemplateEditVo, BindingResult bindingResult) {
        System.out.println("VideoTemplateEditVo object: " + videoTemplateEditVo);
        System.out.println("ID: " + videoTemplateEditVo.getId());
        // 校验videoTemplateEditVo的属性不为空
        if (bindingResult.hasErrors()) {
            // 如果存在验证错误，处理错误并返回错误信息
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            StringBuilder errorMessages = new StringBuilder();
            for (FieldError fieldError : fieldErrors) {
                errorMessages.append(fieldError.getDefaultMessage()).append("; ");
            }
            return Result.error(errorMessages.toString());
        }

        VideoTemplate vt = new VideoTemplate();
        VideoTemplateVersion vtv = new VideoTemplateVersion();
        QueryWrapper query = new QueryWrapper<VideoTemplateVersion>()
                .eq("video_template_id", videoTemplateEditVo.getId())
                .eq("is_current", 1);
        VideoTemplateVersion currentVersion = videoTemplateVersionService.getOne(query);

        if (currentVersion == null) {
            return Result.error("未找到对应数据");
        }

        String aeProjectUrl = videoTemplateEditVo.getAeProjectUrl();

        // 设置版本号
        if (currentVersion == null) {
            vtv.setVersion(FIRST_VERSION);
            vtv.setIsCurrent(1);
        } else if (!StringUtils.isEmpty(aeProjectUrl) && !currentVersion.getAeProjectUrl().equals(aeProjectUrl)) {
            // 版本号+1
            String[] version = currentVersion.getVersion().split("\\.");
            int major = Integer.parseInt(version[0]);
            int minor = Integer.parseInt(version[1]);
            int patch = Integer.parseInt(version[2]);
            patch++;
            vtv.setVersion(major + "." + minor + "." + patch);
            vtv.setIsCurrent(1);
        } else {
            vtv.setVersion(currentVersion.getVersion());
        }
        copyVideoTemplateVersionProperties(videoTemplateEditVo, vtv);
        copyVideoTemplateProperties(videoTemplateEditVo, vt);

        vt.setMtime(new Date());
        vtv.setMtime(new Date());

        String videoUrl = videoTemplateEditVo.getPreviewVideoUrl();

        if (!StringUtils.isEmpty(videoUrl)) {
            String firstPaintUrl = getFirstPaintFromVideoUrl(videoUrl);
            vt.setCoverImageUrl(firstPaintUrl);
        }

        videoTemplateVersionService.saveOrUpdate(vtv);
        videoTemplateService.updateById(vt);

        return Result.OK("编辑成功!");
    }

    private void copyVideoTemplateVersionProperties(VideoTemplateVo videoTemplateVo, VideoTemplateVersion vtv) {
        // 如果不为空，则set
        if (!StringUtils.isEmpty(videoTemplateVo.getAeProjectUrl())) {
            vtv.setAeProjectUrl(videoTemplateVo.getAeProjectUrl());
        }
        if (!StringUtils.isEmpty(videoTemplateVo.getClipsJson())) {
            vtv.setClipsJson(videoTemplateVo.getClipsJson());
        }
        if (!StringUtils.isEmpty(videoTemplateVo.getAuthor())) {
            vtv.setAuthor(videoTemplateVo.getAuthor());
        }
        if (!StringUtils.isEmpty(videoTemplateVo.getId())) {
            vtv.setVideoTemplateId(videoTemplateVo.getId());
        }
    }

    private void copyVideoTemplateProperties(VideoTemplateVo videoTemplateVo, VideoTemplate vt) {
        if (!StringUtils.isEmpty(videoTemplateVo.getName())) {
            vt.setName(videoTemplateVo.getName());
        }
        if (!StringUtils.isEmpty(videoTemplateVo.getAuthor())) {
            vt.setAuthor(videoTemplateVo.getAuthor());
        }
        if (!StringUtils.isEmpty(videoTemplateVo.getBitRate())) {
            vt.setBitRate(videoTemplateVo.getBitRate());
        }
        if (!StringUtils.isEmpty(videoTemplateVo.getCoverImageUrl())) {
            vt.setCoverImageUrl(videoTemplateVo.getCoverImageUrl());
        }
        if (!StringUtils.isEmpty(videoTemplateVo.getDescription())) {
            vt.setDescription(videoTemplateVo.getDescription());
        }
        if (!StringUtils.isEmpty(videoTemplateVo.getPreviewVideoUrl())) {
            vt.setPreviewVideoUrl(videoTemplateVo.getPreviewVideoUrl());
        }
        if (videoTemplateVo.getFps() != 0) {
            vt.setFps(videoTemplateVo.getFps());
        }
        if (videoTemplateVo.getHeight() != 0) {
            vt.setHeight(videoTemplateVo.getHeight());
        }
        if (videoTemplateVo.getWidth() != 0) {
            vt.setWidth(videoTemplateVo.getWidth());
        }
        if (StringUtils.isEmpty(videoTemplateVo.getMainRenderName())) {
            vt.setMainRenderName(DEFAULT_MAIN_RENDER_NAME);
        } else {
            vt.setMainRenderName(videoTemplateVo.getMainRenderName());
        }
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "视频模板-通过id删除")
//    @ApiOperation(value = "视频模板-通过id删除", notes = "视频模板-通过id删除")
//	@RequiresPermissions("mall:视频模板:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        videoTemplateService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "视频模板-批量删除")
//    @ApiOperation(value = "视频模板-批量删除", notes = "视频模板-批量删除")
//	@RequiresPermissions("mall:视频模板:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
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
    @ApiOperation(value = "视频模板-通过id查询", notes = "视频模板-通过id查询")
    @GetMapping(value = "/queryById")
    public Result queryById(@RequestParam(name = "id", required = true) String id) {
        VideoTemplateEditVo vo = new VideoTemplateEditVo();
        VideoTemplate videoTemplate = videoTemplateService.getById(id);
        if (videoTemplate == null) {
            return Result.error("未找到对应数据");
        }
        vo.setId(videoTemplate.getId());
        vo.setName(videoTemplate.getName());
        vo.setAuthor(videoTemplate.getAuthor());
        vo.setBitRate(videoTemplate.getBitRate());
        vo.setCoverImageUrl(videoTemplate.getCoverImageUrl());
        vo.setDescription(videoTemplate.getDescription());
        vo.setPreviewVideoUrl(videoTemplate.getPreviewVideoUrl());
        vo.setFps(videoTemplate.getFps());
        vo.setHeight(videoTemplate.getHeight());
        vo.setWidth(videoTemplate.getWidth());
        vo.setMainRenderName(videoTemplate.getMainRenderName());
        VideoTemplateVersion version = videoTemplateVersionService.getOne(new LambdaQueryWrapper<VideoTemplateVersion>().eq(VideoTemplateVersion::getVideoTemplateId, id).eq(VideoTemplateVersion::getIsCurrent, 1));
        if (version == null) {
            return Result.OK(vo);
        }
        vo.setClipsJson(version.getClipsJson());
        vo.setVersion(version.getVersion());
        vo.setAeProjectUrl(version.getAeProjectUrl());
        return Result.OK(vo);
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
