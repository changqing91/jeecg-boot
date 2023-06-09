package org.jeecg.modules.mall.utils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * @Description: 下载工具类
 * @className: DownloadUtil
 */
public class DownloadUtil {
        /**
        * 下载文件
        *
        * @param url
        * @param filePath
        * @throws Exception
        */
        public static void downloadFile(String url, String filePath) throws Exception {
            // 下载网络文件
            int bytesum = 0;
            int byteread = 0;
            URL httpurl = new URL(url);
            URLConnection conn = httpurl.openConnection();
            InputStream inStream = conn.getInputStream();
            FileOutputStream fs = new FileOutputStream(filePath);
            byte[] buffer = new byte[1204];
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                fs.write(buffer, 0, byteread);
            }
            fs.close();
            inStream.close();

        }

}