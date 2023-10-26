package org.jeecg.modules.vision.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class VideoUtil {
    public static void getVideoFirstFrame(String videoPath, String imagePath) {
        try {
            if (videoPath == null || imagePath == null || videoPath.isEmpty() || imagePath.isEmpty()) {
                System.out.println("videoPath or imagePath is null or empty");
                return;
            }

            // Delete existing output file if it exists
            Path imageFilePath = Paths.get(imagePath);
            if (Files.exists(imageFilePath)) {
                Files.delete(imageFilePath);
            }

            ProcessBuilder processBuilder = new ProcessBuilder(
                    "ffmpeg",
                    "-i", videoPath,
                    "-ss", "00:00:01",
                    "-vframes", "1",
                    "-q:v", "5", // Adjust this value as needed
                    imagePath
            );

            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            int exitCode = process.waitFor();
            System.out.println("Process exited with code: " + exitCode);

            process.waitFor();

            System.out.println("Video first frame saved to: " + imagePath);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        String videoPath = "D:\\Projects\\AeRender\\output\\3b090344-579d-44b4-ba2d-adbdcdeb45b1.mp4";
        String imagePath = "D:\\Projects\\AeRender\\output\\path_to_save_image.png";

        getVideoFirstFrame(videoPath, imagePath);

        long endTime = System.currentTimeMillis();
        System.out.println("视频首帧提取耗时：" + (endTime - startTime) + "ms");
    }
}
