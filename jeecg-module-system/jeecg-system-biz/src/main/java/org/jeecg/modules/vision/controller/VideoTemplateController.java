package org.jeecg.modules.vision.controller;

import java.io.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.util.UUIDGenerator;
import org.jeecg.common.util.oss.OssBootUtil;
import org.jeecg.modules.vision.entity.VideoTemplate;
import org.jeecg.modules.vision.entity.VideoTemplateVersion;
import org.jeecg.modules.vision.service.IVideoTemplateService;
import org.jeecg.modules.vision.service.IVideoTemplateVersionService;
import org.jeecg.modules.vision.utils.DownloadUtil;
import org.jeecg.modules.vision.utils.VideoUtil;
import org.jeecg.modules.vision.vo.VideoTemplateAddVo;
import org.jeecg.modules.vision.vo.VideoTemplateEditVo;
import org.jeecg.modules.vision.vo.VideoTemplateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

/**
 * @Description: 视频模板管理
 * @Author: jeecg-boot
 * @Date: 2023-04-24
 * @Version: V1.0
 */
@Api(tags = "视频模板")
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

    // 指定存储分片文件的目录
    private static final String UPLOAD_DIR = System.getProperty("java.io.tmpdir") + "uploadChunk" + File.separator;

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
    //@AutoLog(value = "分页列表查询")
    @ApiOperation(value = "分页列表查询", notes = "分页列表查询")
    @GetMapping(value = "/list")
//    public Result<IPage<VideoTemplate>> queryPageList(VideoTemplate videoTemplate,
//                                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
//                                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
//                                                      HttpServletRequest req) {
//        QueryWrapper<VideoTemplate> queryWrapper = QueryGenerator.initQueryWrapper(videoTemplate, req.getParameterMap());
//        Page<VideoTemplate> page = new Page<VideoTemplate>(pageNo, pageSize);
//        IPage<VideoTemplate> pageList = videoTemplateService.page(page, queryWrapper);
//        return Result.OK(pageList);
//    }
    public Result<IPage<VideoTemplate>> queryPageList(VideoTemplate videoTemplate,
                                                      @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                      @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                      HttpServletRequest req) {
        Page<VideoTemplate> page = new Page<>(pageNo, pageSize);

        videoTemplate.setIsDelete(0);

        IPage<VideoTemplate> pageList = videoTemplateService.page(page, new QueryWrapper<>(videoTemplate));

        // For each VideoTemplate, retrieve the related VideoTemplateVersion
        List<VideoTemplate> records = pageList.getRecords();
        for (VideoTemplate vt : records) {
            QueryWrapper<VideoTemplateVersion> versionQueryWrapper = new QueryWrapper<>();
            versionQueryWrapper.eq("video_template_id", vt.getId()).eq("is_current", 1);
            VideoTemplateVersion version = videoTemplateVersionService.getOne(versionQueryWrapper);

            // If a related VideoTemplateVersion is found, set its status
            if (version != null) {
                vt.setStatus(version.getStatus());
            }
        }

        pageList.setRecords(records);

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
    @AutoLog(value = "添加")
    @ApiOperation(value = "添加", notes = "添加")
//	@RequiresPermissions("mall:视频模板:add")
    @PostMapping(value = "/add")
    public Result<VideoTemplate> add(@Valid @RequestBody VideoTemplateAddVo videoTemplateAddVo) {
        try {
            // check required fields
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
    @AutoLog(value = "文件上传")
    @ApiOperation(value = "文件上传", notes = "文件上传")
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


    @PostMapping("/uploadChunk")
    public Result uploadChunk(@RequestParam("file") MultipartFile file, @RequestParam("currentChunk") int currentChunk, @RequestParam("totalChunks") int totalChunks, @RequestParam("originalFileName") String fileName) {
        try {
            Result result = new Result();
            if (null == file || null == fileName) {
                return Result.error("文件信息缺失");
            }
            if (totalChunks <= 0) {
                return Result.error("totalChunks錯誤");
            }
            // 创建目标文件名，可以根据需要进行命名策略
            String targetFileName = fileName + currentChunk + ".part";

            // 将分片文件保存到服务器
            saveChunk(file, targetFileName);

            if (currentChunk == totalChunks - 1) {
                // 这是最后一个分片，可以合并分片文件
                String url = combineChunks(totalChunks, fileName, file.getContentType());
                result.setResult(url);
            }

            result.setMessage("分片" + currentChunk + "上传成功");
            result.setSuccess(true);
            result.setCode(CommonConstant.SC_OK_200);
            return result;
        } catch (Exception e) {
            return Result.error("分片上传失败: " + e.getMessage());
        }
    }

    // 保存分片文件到服务器
    private void saveChunk(MultipartFile file, String targetFileName) throws IOException {
        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File targetFile = new File(UPLOAD_DIR, targetFileName);
        try (OutputStream os = new FileOutputStream(targetFile)) {
            os.write(file.getBytes());
        }
    }

    public MultipartFile fileToMultipartFile(File file, String fileName, String contentType) throws IOException {
        // 创建一个DiskFileItem，它需要文件名和文件类型
        DiskFileItem fileItem = new DiskFileItem(
                "file", // 表单字段名
                contentType, // 文件类型
                false, // 是否是表单字段
                fileName, // 文件名
                (int) file.length(), // 文件大小，可以根据需要修改为适当的大小
                null // 上传临时目录，可以为null
        );

        try {
            // 从文件读取数据并设置给DiskFileItem
            fileItem.getOutputStream().write(org.apache.commons.io.FileUtils.readFileToByteArray(file));
        } catch (IOException e) {
            throw new IOException("Failed to create MultipartFile from file", e);
        }

        // 使用CommonsMultipartFile创建MultipartFile对象
        MultipartFile multipartFile = new CommonsMultipartFile(fileItem);

        return multipartFile;
    }

    // 合并分片文件
    private String combineChunks(int totalChunks, String fileName, String contentType) throws IOException {
        // 创建目标文件，这是最终合并后的文件
        File finalFile = new File(UPLOAD_DIR, fileName);

        System.out.println(UPLOAD_DIR);

        // 合并分片文件
        try (OutputStream os = new FileOutputStream(finalFile)) {
            for (int i = 0; i < totalChunks; i++) {
                File chunkFile = new File(UPLOAD_DIR, fileName + i + ".part");
                byte[] chunkData = org.apache.commons.io.FileUtils.readFileToByteArray(chunkFile);
                os.write(chunkData);
                chunkFile.delete(); // 删除分片文件
            }
            InputStream is = new FileInputStream(finalFile);
            String url = null;
            try {
                url = OssBootUtil.upload(fileToMultipartFile(finalFile, fileName, contentType), TEMPLATE_ASSETS_OSS);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return url;
        }
    }

    /**
     * 编辑
     *
     * @param videoTemplateEditVo
     * @return
     */
    @AutoLog(value = "编辑")
    @ApiOperation(value = "编辑", notes = "编辑")
//	@RequiresPermissions("mall:视频模板:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.POST})
    public Result<String> edit(@Valid @RequestBody VideoTemplateEditVo videoTemplateEditVo, BindingResult bindingResult) {
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

        VideoTemplate currentTemplate = videoTemplateService.getById(videoTemplateEditVo.getId());
        if (currentTemplate == null) {
            return Result.error("未找到对应数据");
        }

        QueryWrapper query = new QueryWrapper<VideoTemplateVersion>()
                .eq("video_template_id", videoTemplateEditVo.getId())
                .eq("is_current", 1);
        VideoTemplateVersion currentVersion = videoTemplateVersionService.getOne(query);

        if (currentVersion == null) {
            return Result.error("未找到对应数据");
        }

        String videoUrl = videoTemplateEditVo.getPreviewVideoUrl();

        if (!StringUtils.isEmpty(videoUrl) && !videoUrl.equals(currentTemplate.getPreviewVideoUrl())) {
            String firstPaintUrl = getFirstPaintFromVideoUrl(videoUrl);
            vt.setCoverImageUrl(firstPaintUrl);
        }
        copyVideoTemplateProperties(videoTemplateEditVo, vt);
        vt.setMtime(new Date());
        String aeProjectUrl = videoTemplateEditVo.getAeProjectUrl();
        if (!StringUtils.isEmpty(aeProjectUrl) && !currentVersion.getAeProjectUrl().equals(aeProjectUrl)) {
            VideoTemplateVersion vtv = new VideoTemplateVersion();
            // 版本号+1
            String[] version = currentVersion.getVersion().split("\\.");
            int major = Integer.parseInt(version[0]);
            int minor = Integer.parseInt(version[1]);
            int patch = Integer.parseInt(version[2]);
            patch++;
            vtv.setVersion(major + "." + minor + "." + patch);
            vtv.setIsCurrent(1);
            vtv.setCtime(new Date());
            vt.setVersion(major + "." + minor + "." + patch);
            vtv.setMtime(new Date());
            copyVideoTemplateVersionProperties(videoTemplateEditVo, vtv);
            videoTemplateVersionService.save(vtv);
            currentVersion.setIsCurrent(0);
            currentVersion.setMtime(new Date());
            videoTemplateVersionService.updateById(currentVersion);
        } else {
            currentVersion.setMtime(new Date());
            copyVideoTemplateVersionProperties(videoTemplateEditVo, currentVersion);
            videoTemplateVersionService.updateById(currentVersion);
        }
        vt.setId(videoTemplateEditVo.getId());
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
        if (null != videoTemplateVo.getFps()) {
            vt.setFps(videoTemplateVo.getFps());
        }
        if (null != videoTemplateVo.getHeight()) {
            vt.setHeight(videoTemplateVo.getHeight());
        }
        if (null != videoTemplateVo.getWidth()) {
            vt.setWidth(videoTemplateVo.getWidth());
        }
        if (StringUtils.isEmpty(videoTemplateVo.getMainRenderName())) {
            vt.setMainRenderName(DEFAULT_MAIN_RENDER_NAME);
        } else {
            vt.setMainRenderName(videoTemplateVo.getMainRenderName());
        }
        if (!StringUtils.isEmpty(videoTemplateVo.getRatio())) {
            vt.setRatio(videoTemplateVo.getRatio());
        }
        if (!StringUtils.isEmpty(videoTemplateVo.getResolution())) {
            vt.setResolution(videoTemplateVo.getResolution());
        }
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "通过id删除")
    @ApiOperation(value = "通过id删除", notes = "通过id删除")
//	@RequiresPermissions("mall:视频模板:delete")
    @DeleteMapping(value = "/delete")
//    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
//        // First, remove the related VideoTemplateVersion records
//        QueryWrapper<VideoTemplateVersion> versionQueryWrapper = new QueryWrapper<>();
//        versionQueryWrapper.eq("video_template_id", id);
//        videoTemplateVersionService.remove(versionQueryWrapper);
//
//        // Then, remove the VideoTemplate itself
//        videoTemplateService.removeById(id);
//
//        return Result.OK("删除成功!");
//    }
    //逻辑删除
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        // Update VideoTemplate to set is_delete to 1 for logical delete
        VideoTemplate videoTemplate = new VideoTemplate();
        videoTemplate.setId(id);
        videoTemplate.setIsDelete(1); // 1 can be the value to indicate a deleted record
        videoTemplateService.updateById(videoTemplate);

        // Update VideoTemplateVersion to set is_delete to 1 for logical delete
        VideoTemplateVersion version = new VideoTemplateVersion();
        version.setIsDelete(1); // 1 can be the value to indicate a deleted record

        QueryWrapper<VideoTemplateVersion> versionQueryWrapper = new QueryWrapper<>();
        versionQueryWrapper.eq("video_template_id", id);
        videoTemplateVersionService.update(version, versionQueryWrapper);

        return Result.OK("逻辑删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "批量删除")
//    @ApiOperation(value = "批量删除", notes = "批量删除")
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
    //@AutoLog(value = "通过id查询")
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping(value = "/queryById")
    public Result queryById(@RequestParam(name = "id", required = true) String id, @RequestParam(name = "version", required = false) String version) {
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
        vo.setRatio(videoTemplate.getRatio());
        vo.setBitRate(videoTemplate.getBitRate());
        vo.setResolution(videoTemplate.getResolution());
        vo.setIsDelete(videoTemplate.getIsDelete());
        VideoTemplateVersion ver = null;
        if (null != version) {
            ver = videoTemplateVersionService.getOne(new LambdaQueryWrapper<VideoTemplateVersion>().eq(VideoTemplateVersion::getVideoTemplateId, id).eq(VideoTemplateVersion::getVersion, version));
            ;
        } else {
            ver = videoTemplateVersionService.getOne(new LambdaQueryWrapper<VideoTemplateVersion>().eq(VideoTemplateVersion::getVideoTemplateId, id).eq(VideoTemplateVersion::getIsCurrent, 1));
        }
        if (ver == null) {
            return Result.OK(vo);
        }
        vo.setClipsJson(ver.getClipsJson());
        vo.setVersion(ver.getVersion());
        vo.setAeProjectUrl(ver.getAeProjectUrl());
        vo.setStatus(ver.getStatus());
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

    /**
     * 通过id查询版本列表
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "通过id查询版本列表")
    @ApiOperation(value = "通过id查询版本列表", notes = "通过id查询版本列表")
    @GetMapping(value = "/queryVersionListById")
    public Result<List<VideoTemplateVersion>> queryVersionListById(@RequestParam(name = "id", required = true) String id) {
        List<VideoTemplateVersion> list = videoTemplateVersionService.list(new LambdaQueryWrapper<VideoTemplateVersion>().eq(VideoTemplateVersion::getVideoTemplateId, id));
        return Result.OK(list);
    }

}
