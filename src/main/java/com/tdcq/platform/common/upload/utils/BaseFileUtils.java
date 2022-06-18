package com.platform.common.upload.utils;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.IdUtil;
import com.platform.common.upload.config.UploadFileConfig;
import com.platform.common.upload.vo.UploadFileVo;
import lombok.SneakyThrows;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 文件上传
 */
public class BaseFileUtils {

    protected static UploadFileConfig config;

    public static void init(UploadFileConfig config) {
        BaseFileUtils.config = config;
    }

    @SneakyThrows
    protected static String getFileName(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (StringUtils.isEmpty(fileName)) {
            // 随机名称
            fileName = IdUtil.objectId() + "." + getFileType(file);
        }
        return fileName;
    }

    @SneakyThrows
    protected static String getFileName(File file) {
        String fileName = file.getName();
        if (StringUtils.isEmpty(fileName)) {
            // 随机名称
            fileName = IdUtil.objectId() + "." + getFileType(file);
        }
        return fileName;
    }

    @SneakyThrows
    protected static String getFileKey(MultipartFile file) {
        return IdUtil.objectId() + "." + getFileType(file);
    }

    @SneakyThrows
    protected static String getFileKey(File file) {
        return IdUtil.objectId() + "." + getFileType(file);
    }

    @SneakyThrows
    protected static String getFileType(MultipartFile file) {
        // 文件扩展名
        String fileType = FileNameUtil.extName(file.getOriginalFilename());
        if (StringUtils.isEmpty(fileType)) {
            // 文件后缀
            fileType = FileTypeUtil.getType(file.getInputStream());
        }
        if (StringUtils.isEmpty(fileType)) {
            fileType = "txt";
        }
        return fileType;
    }

    @SneakyThrows
    protected static String getFileType(File file) {
        // 文件扩展名
        String fileType = FileNameUtil.extName(file.getName());
        if (StringUtils.isEmpty(fileType)) {
            // 文件后缀
            fileType = FileTypeUtil.getType(file);
        }
        if (StringUtils.isEmpty(fileType)) {
            fileType = "txt";
        }
        return fileType;
    }

    /**
     * 封装对象
     *
     * @param fileName
     * @param serverUrl
     * @param fileKey
     * @param fileType
     * @return
     */
    protected static UploadFileVo format(String fileName, String serverUrl, String fileKey, String fileType, InputStream inputStream) {
        // 服务器地址
        UploadFileVo fileVo = new UploadFileVo()
                .setFileName(fileName)
                .setFullPath(serverUrl + FileNameUtil.UNIX_SEPARATOR + fileKey)
                .setFileType(fileType)
                .setInputStream(inputStream);
        return fileVo;
    }

    /**
     * 获取文件流
     */
    @SneakyThrows
    public static InputStream getInputStream(String urlPath) {
        URL url = new URL(urlPath);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        // 设置网络连接超时时间
        httpURLConnection.setConnectTimeout(5000);
        // 设置应用程序要从网络连接读取数据
        httpURLConnection.setDoInput(true);
        // 从服务器返回一个输入流
        return httpURLConnection.getInputStream();
    }

    /**
     * 删除本地文件
     */
    @SneakyThrows
    public static boolean delFile(File file) {
        return FileUtil.del(file);
    }

}
