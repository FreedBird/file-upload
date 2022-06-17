package com.platform.common.upload.utils;

import cn.hutool.core.io.FileUtil;
import com.platform.common.exception.BaseException;
import com.platform.common.upload.vo.UploadFileVo;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.text.ParseException;

/**
 * 腾讯云上传
 */
@Slf4j
public class CosUtils extends BaseFileUtils {

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
        // 3 生成 cos 客户端。
        COSClient client = null;
        try {
            client = new COSClient(new BasicCOSCredentials(config.getAccessKey(), config.getSecretKey()),
                    new com.qcloud.cos.ClientConfig(new Region(config.getRegion())));
            //上传到腾讯云
            PutObjectRequest putObjectRequest = new PutObjectRequest(config.getBucketName()
                    , fileKey, file.getInputStream(), new ObjectMetadata());
            client.putObject(putObjectRequest);
            return format(fileName, config.getServerUrl(), fileKey, fileType, file.getInputStream());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BaseException("文件上传失败");
        } finally {
            if (client != null) {
                client.shutdown();
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
        InputStream inputStream = FileUtil.getInputStream(file);
        // 3 生成 cos 客户端。
        COSClient client = null;
        try {
            client = new COSClient(new BasicCOSCredentials(config.getAccessKey(), config.getSecretKey()),
                    new com.qcloud.cos.ClientConfig(new Region(config.getRegion())));
            //上传到腾讯云
            PutObjectRequest putObjectRequest = new PutObjectRequest(config.getBucketName()
                    , fileKey, inputStream, new ObjectMetadata());
            client.putObject(putObjectRequest);
            return format(fileName, config.getServerUrl(), fileKey, fileType, inputStream);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BaseException("文件上传失败");
        } finally {
            if (client != null) {
                client.shutdown();
            }
        }

    }

}
