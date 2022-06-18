package com.platform.common.upload.utils;

import cn.hutool.core.io.FileUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.platform.common.exception.BaseException;
import com.platform.common.upload.vo.UploadFileVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.text.ParseException;

/**
 * 阿里云文件上传
 */
@Component
@Slf4j
public class OssUtils extends BaseFileUtils {

    private static OSS initOSS() {
        return new OSSClientBuilder()
                .build(config.getRegion(), config.getAccessKey(), config.getSecretKey());
    }

    /**
     * 上传
     *
     * @param file
     * @return
     * @throws ParseException
     */
    public static UploadFileVo uploadFile(MultipartFile file) {
        OSS client = initOSS();
        try {
            String fileName = getFileName(file);
            String fileKey = getFileKey(file);
            String fileType = getFileType(file);
            client.putObject(config.getBucketName(), fileKey, file.getInputStream());
            // 服务器地址
            String serverUrl = config.getServerUrl();
            return format(fileName, serverUrl, fileKey, fileType, file.getInputStream());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BaseException("文件上传失败");
        } finally {
            client.shutdown();
        }
    }

    /**
     * 上传
     *
     * @param file
     * @return
     * @throws ParseException
     */
    public static UploadFileVo uploadFile(File file) {
        OSS client = initOSS();
        try {
            String fileName = getFileName(file);
            String fileKey = getFileKey(file);
            String fileType = getFileType(file);
            InputStream inputStream = FileUtil.getInputStream(file);
            client.putObject(config.getBucketName(), fileKey, inputStream);
            // 服务器地址
            String serverUrl = config.getServerUrl();
            return format(fileName, serverUrl, fileKey, fileType, inputStream);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BaseException("文件上传失败");
        } finally {
            client.shutdown();
        }
    }

}
