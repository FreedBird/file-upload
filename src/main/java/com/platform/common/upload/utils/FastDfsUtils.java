package com.platform.common.upload.utils;

import com.github.tobato.fastdfs.domain.conn.FdfsWebServer;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.fdfs.ThumbImageConfig;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.platform.common.upload.vo.UploadFileVo;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * 文件上传
 */
@Component
public class FastDfsUtils extends BaseFileUtils {

    private static ThumbImageConfig thumbImageConfig;

    private static FastFileStorageClient fastFileStorageClient;

    private static FdfsWebServer fdfsWebServer;

    public FastDfsUtils(ThumbImageConfig thumbImageConfig, FastFileStorageClient fastFileStorageClient, FdfsWebServer fdfsWebServer) {
        FastDfsUtils.thumbImageConfig = thumbImageConfig;
        FastDfsUtils.fastFileStorageClient = fastFileStorageClient;
        FastDfsUtils.fdfsWebServer = fdfsWebServer;
    }

    /**
     * @param multipartFile 文件对象
     * @return 返回文件地址
     * @author qbanxiaoli
     * @description 上传文件
     */
    @SneakyThrows
    public static UploadFileVo uploadFile(MultipartFile multipartFile) {
        StorePath storePath = fastFileStorageClient.uploadFile(multipartFile.getInputStream(), multipartFile.getSize(), FilenameUtils.getExtension(multipartFile.getOriginalFilename()), null);
        String fileKey = storePath.getFullPath();
        String fileType = getFileType(multipartFile);
        String fileName = getFileName(multipartFile);
        String serverUrl = fdfsWebServer.getWebServerUrl();
        return format(fileName, serverUrl, fileKey, fileType, multipartFile.getInputStream());
    }

    /**
     * @param multipartFile 图片对象
     * @return 返回图片地址
     * @author qbanxiaoli
     * @description 上传缩略图
     */
    @SneakyThrows
    public static UploadFileVo uploadImageAndCrtThumbImage(MultipartFile multipartFile) {
        StorePath storePath = fastFileStorageClient.uploadImageAndCrtThumbImage(multipartFile.getInputStream(), multipartFile.getSize(), FilenameUtils.getExtension(multipartFile.getOriginalFilename()), null);
        String fileKey = thumbImageConfig.getThumbImagePath(storePath.getFullPath());
        String fileType = getFileType(multipartFile);
        String fileName = getFileName(multipartFile);
        String serverUrl = fdfsWebServer.getWebServerUrl();
        return format(fileName, serverUrl, fileKey, fileType, multipartFile.getInputStream());
    }

    /**
     * @param file 文件对象
     * @return 返回文件地址
     * @author qbanxiaoli
     * @description 上传文件
     */
    @SneakyThrows
    public static UploadFileVo uploadFile(File file) {
        @Cleanup FileInputStream inputStream = new FileInputStream(file);
        StorePath storePath = fastFileStorageClient.uploadFile(inputStream, file.length(), FilenameUtils.getExtension(file.getName()), null);
        String fileKey = storePath.getFullPath();
        String fileType = getFileType(file);
        String fileName = getFileName(file);
        String serverUrl = fdfsWebServer.getWebServerUrl();
        return format(fileName, serverUrl, fileKey, fileType, inputStream);
    }

    /**
     * @param file 图片对象
     * @return 返回图片地址
     * @author qbanxiaoli
     * @description 上传缩略图
     */
    @SneakyThrows
    public static UploadFileVo uploadImageAndCrtThumbImage(File file) {
        @Cleanup FileInputStream inputStream = new FileInputStream(file);
        StorePath storePath = fastFileStorageClient.uploadImageAndCrtThumbImage(inputStream, file.length(), FilenameUtils.getExtension(file.getName()), null);
        String fileKey = thumbImageConfig.getThumbImagePath(storePath.getFullPath());
        String fileType = getFileType(file);
        String fileName = getFileName(file);
        String serverUrl = fdfsWebServer.getWebServerUrl();
        return format(fileName, serverUrl, fileKey, fileType, inputStream);
    }

    /**
     * @param bytes    byte数组
     * @param fileType 文件扩展名
     * @return 返回文件地址
     * @author qbanxiaoli
     * @description 将byte数组生成一个文件上传
     */
    @SneakyThrows
    public static UploadFileVo uploadFile(String fileName, byte[] bytes, String fileType) {
        @Cleanup ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        StorePath storePath = fastFileStorageClient.uploadFile(inputStream, bytes.length, fileType, null);
        String fileKey = storePath.getFullPath();
        String serverUrl = fdfsWebServer.getWebServerUrl();
        return format(fileName, serverUrl, fileKey, fileType, inputStream);
    }

    /**
     * @param filePath 文件访问地址
     * @author qbanxiaoli
     * @description 下载文件
     */
    @SneakyThrows
    public static byte[] downloadFile(String filePath) {
        StorePath storePath = StorePath.parseFromUrl(filePath);
        return fastFileStorageClient.downloadFile(storePath.getGroup(), storePath.getPath(), new DownloadByteArray());
    }

    /**
     * @param filePath 文件访问地址
     * @author qbanxiaoli
     * @description 删除文件
     */
    @SneakyThrows
    public static void deleteFile(String filePath) {
        StorePath storePath = StorePath.parseFromUrl(filePath);
        fastFileStorageClient.deleteFile(storePath.getGroup(), storePath.getPath());
    }

}
