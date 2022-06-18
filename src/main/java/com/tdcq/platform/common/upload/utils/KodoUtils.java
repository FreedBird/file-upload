package com.platform.common.upload.utils;

import cn.hutool.core.io.FileUtil;
import com.platform.common.exception.BaseException;
import com.platform.common.upload.vo.UploadFileVo;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.text.ParseException;

/**
 * 七牛云上传
 */
@Component
@Slf4j
public class KodoUtils extends BaseFileUtils {

    /**
     * 获取上传凭证
     *
     * @return
     */
    public static String getUploadToken(String key) {
        return Auth.create(config.getAccessKey(), config.getSecretKey())
                .uploadToken(config.getBucketName(), key);
    }

    /**
     * 上传
     *
     * @param file
     * @return
     * @throws ParseException
     */
    public static UploadFileVo uploadFile(MultipartFile file) {
        String fileName = getFileName(file);
        String fileKey = getFileKey(file);
        String fileType = getFileType(file);
        String token = getUploadToken(fileKey);
        Response response = null;
        try {
            UploadManager uploadManager = new UploadManager(new Configuration());
            response = uploadManager.put(file.getInputStream(), fileKey, token, null, fileType);
            return format(fileName, config.getServerUrl(), fileKey, fileType, file.getInputStream());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BaseException("文件上传失败");
        } finally {
            if (response != null) {
                response.close();
            }
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
        String fileName = getFileName(file);
        String fileKey = getFileKey(file);
        String fileType = getFileType(file);
        String token = getUploadToken(fileKey);
        InputStream inputStream = FileUtil.getInputStream(file);
        Response response = null;
        try {
            UploadManager uploadManager = new UploadManager(new Configuration());
            response = uploadManager.put(inputStream, fileKey, token, null, fileType);
            return format(fileName, config.getServerUrl(), fileKey, fileType, inputStream);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BaseException("文件上传失败");
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

}
