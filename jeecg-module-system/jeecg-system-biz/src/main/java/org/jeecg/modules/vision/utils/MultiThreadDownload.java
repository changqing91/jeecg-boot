package org.jeecg.modules.vision.utils;

import org.jeecg.modules.vision.entity.Video;

import java.io.*;
import java.net.*;
import java.util.*;

public class MultiThreadDownload {
    private static final int BUFFER_SIZE = 4096;
    private static final String downloadPath = "/tmp/mall-video/";

    private static int numThreads = 10;

    public static void main(String[] args) {
        List<Video> videos = new ArrayList<>();
        Video v1 = new Video();
        v1.setVideoUrl("http://upos-sz-staticcos.bilivideo.com/mallboss/nextvideo/720p/standing1_v1/10047665_6d0c640d2415400988297e56325a6503_v.mp4");
        for (int i = 0; i < 10; i++) {
            videos.add(v1);
        }
        // 假设List中已经存在要下载的Video对象
        String saveDir = downloadPath;

        download(videos, saveDir);
    }

    public static void download(List<Video> videos, String saveDir) {
        Long startTime = System.currentTimeMillis();
        if (videos.size() < 10) {
            numThreads = videos.size();
        }
        // 启动多个线程并下载视频
        List<DownloadThread> threads = new ArrayList<>();
        for (Video v : videos) {
            String url = v.getVideoUrl();
//            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String fileName = url.substring(url.lastIndexOf("/") + 1);
            DownloadThread thread = new DownloadThread(url, saveDir + "/" + fileName);
            thread.start();
            threads.add(thread);
        }

        // 等待所有线程完成下载
        for (DownloadThread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Long endTime = System.currentTimeMillis();
        System.out.println("Total time: " + (endTime - startTime) / 1000 + "s");

        System.out.println("Videos downloaded successfully");
    }

    private static class DownloadThread extends Thread {
        private String fileURL;
        private String savePath;

        public DownloadThread(String fileURL, String savePath) {
            this.fileURL = fileURL;
            this.savePath = savePath;
        }

        @Override
        public void run() {
            try {
                URL url = new URL(fileURL);
                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                httpConn.setRequestMethod("GET");

                int responseCode = httpConn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    int contentLength = httpConn.getContentLength();

                    // 创建本地文件
                    File outputFile = new File(savePath);
                    RandomAccessFile output = new RandomAccessFile(outputFile, "rw");
                    output.setLength(contentLength);
                    output.close();

                    // 计算每个线程应下载的数据量
                    int partSize = (int) Math.ceil((double) contentLength / numThreads);

                    // 启动多个线程并下载文件
                    List<DownloadPartThread> partThreads = new ArrayList<>();
                    for (int i = 0; i < numThreads; i++) {
                        int startByte = i * partSize;
                        int endByte = Math.min(startByte + partSize, contentLength) - 1;
                        DownloadPartThread partThread = new DownloadPartThread(fileURL, savePath, startByte, endByte);
                        partThread.start();
                        partThreads.add(partThread);
                    }

                    // 等待所有线程完成下载
                    for (DownloadPartThread partThread : partThreads) {
                        partThread.join();
                    }
                } else {
                    System.out.println("No video to download. Server replied HTTP code: " + responseCode);
                }

                httpConn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class DownloadPartThread extends Thread {
        private String fileURL;
        private String savePath;
        private int startByte;
        private int endByte;

        public DownloadPartThread(String fileURL, String savePath, int startByte, int endByte) {
            this.fileURL = fileURL;
            this.savePath = savePath;
            this.startByte = startByte;
            this.endByte = endByte;
        }

        @Override
        public void run() {
            try {
                URL url = new URL(fileURL);
                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                httpConn.setRequestMethod("GET");
                // 设置下载范围
                httpConn.setRequestProperty("Range", "bytes=" + startByte + "-" + endByte);

                int responseCode = httpConn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_PARTIAL) {
                    // 打开本地文件并将数据写入
                    RandomAccessFile output = new RandomAccessFile(savePath, "rw");
                    output.seek(startByte);

                    InputStream inputStream = httpConn.getInputStream();
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        output.write(buffer, 0, bytesRead);
                    }

                    output.close();
                    inputStream.close();
                } else {
                    System.out.println("Failed to download video. Server replied HTTP code: " + responseCode);
                }

                httpConn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
