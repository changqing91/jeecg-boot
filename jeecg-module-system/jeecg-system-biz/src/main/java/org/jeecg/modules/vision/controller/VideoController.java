package org.jeecg.modules.vision.controller;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.UUID;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.vision.entity.Video;
import org.jeecg.modules.vision.entity.VideoJob;
import org.jeecg.modules.vision.entity.VideoTemplate;
import org.jeecg.modules.vision.entity.VideoTemplateVersion;
import org.jeecg.modules.vision.service.IVideoJobService;
import org.jeecg.modules.vision.service.IVideoService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.vision.service.IVideoTemplateService;
import org.jeecg.modules.vision.service.IVideoTemplateVersionService;
import org.jeecg.modules.vision.utils.BasicAuth;
import org.jeecg.modules.vision.utils.DownloadUtil;
import org.jeecg.modules.vision.utils.MultiThreadDownload;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.vision.vo.VideoJobVo;
import org.jeecg.modules.vision.vo.VideoNew;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;

/**
 * @Description: 视频管理
 * @Author: jeecg-boot
 * @Date: 2023-04-24
 * @Version: V1.0
 */
@Api(tags = "视频管理")
@RestController
@RequestMapping("/vision/video")
@Slf4j
public class VideoController extends JeecgController<Video, IVideoService> {
    @Autowired
    private IVideoService videoService;

    @Autowired
    private IVideoJobService videoJobService;

    @Autowired
    private IVideoTemplateVersionService templateVersionService;

    @Autowired
    private IVideoTemplateService templateService;


    private static final String downloadPath = "/tmp/mall-video/";

//    @Value("${jeecg.databus.pubUrl}")
//    private String DATABUS_URL;
//
//    @Value("${jeecg.databus.topic}")
//    private String DATABUS_TOPIC;
//
//    @Value("${jeecg.databus.group}")
//    private String DATABUS_GROUP;
//
//    @Value("${jeecg.databus.appKey}")
//    private String DATABUS_APPKEY;
//
//    @Value("${jeecg.databus.appSecret}")
//    private String DATABUS_APPSECRET;

    /**
     * 分页列表查询
     *
     * @param video
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "视频分页列表查询")
    @ApiOperation(value = "视频分页列表查询", notes = "视频分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<Video>> queryPageList(Video video,
                                              @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                              HttpServletRequest req) {
        QueryWrapper<Video> queryWrapper = QueryGenerator.initQueryWrapper(video, req.getParameterMap());
        Page<Video> page = new Page<Video>(pageNo, pageSize);
        IPage<Video> pageList = videoService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param video
     * @return
     */
    @AutoLog(value = "视频添加")
    @ApiOperation(value = "视频添加", notes = "视频添加")
//    @RequiresPermissions("mall:vision_video:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody Video video) {
        videoService.save(video);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param video
     * @return
     */
    @AutoLog(value = "视频编辑")
    @ApiOperation(value = "视频编辑", notes = "视频编辑")
    @RequiresPermissions("mall:vision_video:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.POST})
    public Result<String> edit(@RequestBody Video video) {
        videoService.updateById(video);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "视频通过id删除")
//    @ApiOperation(value = "视频通过id删除", notes = "视频通过id删除")
    @RequiresPermissions("mall:vision_video:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        videoService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "视频批量删除")
//    @ApiOperation(value = "视频批量删除", notes = "视频批量删除")
    @RequiresPermissions("mall:vision_video:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.videoService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "视频通过id查询")
    @ApiOperation(value = "视频通过id查询", notes = "视频通过id查询")
    @GetMapping(value = "/queryById")
    public Result<Video> queryById(@RequestParam(name = "id", required = true) String id) {
        Video video = videoService.getById(id);
        if (video == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(video);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param video
     */
//    @RequiresPermissions("mall:vision_video:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Video video) {
        return super.exportXls(request, video, Video.class, "vision_video");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("mall:vision_video:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Video.class);
    }

    /**
     * 分页列表查询，并根据查询结果批量下载视频并打包zip文件
     *
     * @param video
     * @param pageNo
     * @param pageSize
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "视频批量下载", notes = "视频批量下载")
    @RequestMapping(value = "/download", method = RequestMethod.GET)
    public void download(Video video,
                         @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                         HttpServletRequest request, HttpServletResponse response) throws Exception {
        QueryWrapper<Video> queryWrapper = QueryGenerator.initQueryWrapper(video, request.getParameterMap());
//		Page<Video> page = new Page<Video>(pageNo, pageSize);
//		IPage<Video> pageList = videoService.page(page, queryWrapper);
// 		List<Video> list = pageList.getRecords();
        // 过滤选中数据
        String selections = request.getParameter("selections");
        if (oConvertUtils.isNotEmpty(selections)) {
            List<String> selectionList = Arrays.asList(selections.split(","));
            queryWrapper.in("id", selectionList);
        }
        // Step.2 获取导出数据
        List<Video> list = videoService.list(queryWrapper);
        // 批量下载视频
        String tmpPath = downloadPath + System.currentTimeMillis();
        // new folder by tmpPath, if exist, delete it
        if (FileUtil.exist(tmpPath)) {
            FileUtil.del(tmpPath);
        }
        FileUtil.mkdir(tmpPath);

        MultiThreadDownload.download(list, tmpPath);
//		for (Video v : list) {
//			String url = v.getVideoUrl();
//			String fileName = url.substring(url.lastIndexOf("/") + 1);
//			String filePath = tmpPath + "/" + fileName;
//			// 下载视频
//			DownloadUtil.downloadFile(url, filePath);
//		}
        // 打包zip文件
        String zipFileName = System.currentTimeMillis() + ".zip";
        String zipFilePath = downloadPath + zipFileName;
        ZipUtil.zip(tmpPath, zipFilePath);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + zipFileName + "\"");
        // 获取本地文件的输入流和响应的输出流
        FileInputStream inputStream = new FileInputStream(zipFilePath);
        BufferedInputStream bufferedInput = new BufferedInputStream(inputStream);
        OutputStream outputStream = response.getOutputStream();
        BufferedOutputStream bufferedOutput = new BufferedOutputStream(outputStream);
        // 创建一个缓冲区数组，并将文件内容读取到该数组中
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = bufferedInput.read(buffer)) != -1) {
            bufferedOutput.write(buffer, 0, bytesRead);
        }
        // 关闭所有相关的流和连接
        bufferedInput.close();
        inputStream.close();
        bufferedOutput.close();
        outputStream.close();
        FileUtil.del(zipFilePath);
        FileUtil.del(tmpPath);
    }

    /**
     * 视频databus生产
     * @param videoNew
     * @return
     */
//	 @ApiOperation(value="视频databus生产", notes="视频databus生产")
//	 @RequestMapping(value = "/publishBySpu", method = RequestMethod.POST)
//	 public Result<?> publishBySpu(@RequestBody VideoNew videoNew) {
//		 try {
//			 String auth = BasicAuth.generateAuth(DATABUS_APPKEY, DATABUS_APPSECRET);
//			 String url = DATABUS_URL;
//			 String query = "topic=" + URLEncoder.encode(DATABUS_TOPIC, "UTF-8") + "&group=" + URLEncoder.encode(DATABUS_GROUP, "UTF-8");
//			 Integer templateId = videoNew.getTemplateId();
//			 List<String> spuIds = videoNew.getSpuIds();
//
//			 JSONObject data = new JSONObject();
//			 JSONArray jsonArray = new JSONArray();
//			 Integer count = 0;
//			 for (String spuId : spuIds) {
//				 JSONObject jsonObject = new JSONObject();
////				 jsonObject.put("key", UUID.randomUUID().toString().replace("-", ""));
//				 jsonObject.put("key", (count++).toString());
//				 JSONObject jsonObject2 = new JSONObject();
//				 jsonObject2.put("templateId", templateId);
//				 jsonObject2.put("spuId", spuId);
//				 jsonObject.put("value", jsonObject2);
//				 jsonArray.add(jsonObject);
//			 }
//			 data.put("records", jsonArray);
//			 String reqBody = data.toString();
//
//			 // 创建连接
//			 URL obj = new URL(url + "?" + query);
//			 HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//			 con.setRequestMethod("POST");
//
//			 // 添加请求头
//			 con.setRequestProperty("Authorization", auth);
//			 con.setRequestProperty("Content-Type", "application/json");
//
//			 // 发送POST请求
//			 con.setDoOutput(true);
////			 con.getOutputStream().write(reqBody.getBytes("UTF-8"));
//			 DataOutputStream wr = new DataOutputStream(con.getOutputStream());
//			 wr.writeBytes(reqBody);
//			 wr.flush();
//			 wr.close();
//
//			 System.out.println("发送POST请求，请求路径：" + url + "?" + query);
//			 System.out.println("POST请求参数：" + reqBody);
//
//			 // 获取响应
//			 int responseCode = con.getResponseCode();
//			 BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), Charset.forName("UTF-8")));
//			 String inputLine;
//			 StringBuffer response = new StringBuffer();
//			 while ((inputLine = in.readLine()) != null) {
//				 response.append(inputLine);
//			 }
//			 in.close();
//
//			 // 输出响应
//			 System.out.println("请求响应：" + response.toString());
//			 return Result.OK(response);
//		 } catch (Exception e) {
//			 System.out.println("发送POST请求出现异常：" + e.getMessage());
//			 return Result.error("发布失败：" + e.getMessage());
//		 }
//	 }

//    /**
//     * 提交智能生产任务
//     *
//     * @param videoNew
//     * @return
//     */
//    @ApiOperation(value = "提交视频生产任务", notes = "提交视频生产任务")
//    @RequestMapping(value = "/submitMediaJob", method = RequestMethod.POST)
//    public Result<?> publish(@RequestBody VideoNew videoNew) {
//        try {
//            String auth = BasicAuth.generateAuth(DATABUS_APPKEY, DATABUS_APPSECRET);
//            String url = DATABUS_URL;
//            String query = "topic=" + URLEncoder.encode(DATABUS_TOPIC, "UTF-8") + "&group=" + URLEncoder.encode(DATABUS_GROUP, "UTF-8");
//            String templateId = videoNew.getTemplateId();
//            JSONArray clipsJson = videoNew.getClipsJson();
//
////            String clipsJsonStr = clipsJson.toString();
//
//            JSONObject body = new JSONObject();
//
//            JSONObject data = new JSONObject();
//
//            JSONArray jsonArray = new JSONArray();
//
//            JSONObject item = new JSONObject();
//
//            item.put("templateId", templateId);
//            item.put("clipsJson", clipsJson);
//
//            data.put("key", UUID.randomUUID().toString().replace("-", ""));
//            data.put("value", item);
//
//            jsonArray.add(data);
//
//            body.put("records", jsonArray);
//
//            String reqBody = body.toString();
//
//            // 创建连接
//            URL obj = new URL(url + "?" + query);
//            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//            con.setRequestMethod("POST");
//
//            // 添加请求头
//            con.setRequestProperty("Authorization", auth);
//            con.setRequestProperty("Content-Type", "application/json");
//
//            // 发送POST请求
//            con.setDoOutput(true);
//			 con.getOutputStream().write(reqBody.getBytes("UTF-8"));
//            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
//            wr.writeBytes(reqBody);
//            wr.flush();
//            wr.close();
//
//            System.out.println("发送POST请求，请求路径：" + url + "?" + query);
//            System.out.println("POST请求参数：" + reqBody);
//
//            // 获取响应
//            int responseCode = con.getResponseCode();
//            String msg = con.getResponseMessage();
//            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), Charset.forName("UTF-8")));
//            String inputLine;
//            StringBuffer response = new StringBuffer();
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//            in.close();
//
//            // 输出响应
//            System.out.println("请求响应：" + response.toString());
//            return Result.OK(response);
//        } catch (Exception e) {
//            System.out.println("发送POST请求出现异常：" + e.getMessage());
//            return Result.error("发布失败：" + e.getMessage());
//        }
//    }

    public boolean validateClipsJson(JSONArray clipsJson) {
        for (int i = 0; i < clipsJson.size(); i++) {
            JSONArray clipArray = clipsJson.getJSONArray(i);
            for (int j = 0; j < clipArray.size(); j++) {
                JSONObject clipObject = clipArray.getJSONObject(j);
                if (!clipObject.has("id") || !(clipObject.has("url") || clipObject.has("text"))) {
                    return false; // Missing 'id' or 'url' in at least one item
                }
            }
        }
        return true; // All items contain 'id' and 'url'
    }


    /**
     * 提交智能生产任务
     *
     * @param videoNew
     * @return
     */
    @ApiOperation(value = "提交视频生产任务", notes = "提交视频生产任务")
    @RequestMapping(value = "/submitMediaJob", method = RequestMethod.POST)
    public Result<?> publish(@RequestBody VideoNew videoNew) {
        try {
            String templateId = videoNew.getTemplateId();
            JSONArray clipsJson = videoNew.getClipsJson();
            if (null == templateId) {
                return Result.error("templateId为空");
            }
            if (null == clipsJson) {
                return Result.error("clipsJson为空");
            }
            if (!validateClipsJson(clipsJson)) {
                return Result.error("clipsJson数据格式有误：缺少id或url信息，或非二维数组");
            }
            VideoTemplate template = templateService.getById(templateId);
            if (null == template) {
                return Result.error("无效模板");
            }
            VideoJob job = new VideoJob();
            job.setVideoTemplateId(templateId);
            String clipsJsonStr = clipsJson.toString();
            job.setClipsJson(clipsJsonStr);
            job.setStatus("Created");
            videoJobService.save(job);
            return Result.OK(job);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    /**
     * 查询智能生产任务
     *
     * @return
     */
    @RequestMapping(value = "/queryCreatedMediaJob", method = RequestMethod.GET)
    public Result<VideoJobVo> queryOneCreatedMediaJob() {
        VideoJobVo jobVo = new VideoJobVo();
        List<VideoJob> jobList = videoJobService.list(new QueryWrapper<VideoJob>().eq("status", "Created"));
        if (jobList.size() == 0) {
            return Result.OK(null);
        }
        VideoJob job = jobList.get(0);
        if (job == null) {
            return Result.OK(null);
        }
        QueryWrapper query = new QueryWrapper<VideoTemplateVersion>()
                .eq("video_template_id", job.getVideoTemplateId())
                .eq("is_current", 1);
        VideoTemplateVersion currentVersion = templateVersionService.getOne(query);
        jobVo.setVideoTemplateVersion(currentVersion);
        jobVo.setId(job.getId());
        jobVo.setStatus(job.getStatus());
        jobVo.setClipsJson(job.getClipsJson());
        return Result.OK(jobVo);
    }

    /**
     * 更新智能生产任务状态
     *
     * @return
     */
    @ApiOperation(value = "提交视频生产任务", notes = "提交视频生产任务")
    @RequestMapping(value = "/changeMediaJobStatusById", method = RequestMethod.POST)
    public Result<?> changeMediaJobStatusById(@RequestBody VideoJobVo videoJobVo) {
        String status = videoJobVo.getStatus();
        String id = videoJobVo.getId();
        // Created：已创建，未合成；Processing：正在合成；Done: 完成
        if (!"Created".equals(status) && !"Processing".equals(status) && !"Done".equals(status)) {
            return Result.error("状态错误");
        }
        VideoJob job = videoJobService.getById(id);
        job.setStatus(status);
        videoJobService.updateById(job);
        return Result.OK(job);
    }


    public static void main(String[] args) throws Exception {
        String tmpPath = downloadPath + System.currentTimeMillis();
        // new folder by tmpPath, if exist, delete it
        if (FileUtil.exist(tmpPath)) {
            FileUtil.del(tmpPath);
        }
        FileUtil.mkdir(tmpPath);

        String url = "http://upos-sz-staticcos.bilivideo.com/mallboss/nextvideo/720p/golden_case_v1/10148517_5332388603664ea0a5237f5b7cb10224_v.mp4";
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        String filePath = tmpPath + "/" + fileName;
        DownloadUtil.downloadFile(url, filePath);
        String zipFilePath = downloadPath + System.currentTimeMillis() + ".zip";
        ZipUtil.zip(tmpPath, zipFilePath);
    }

}
