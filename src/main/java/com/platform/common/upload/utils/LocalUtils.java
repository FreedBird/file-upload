package com.platform.common.upload.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.IdUtil;
import com.platform.common.upload.vo.UploadFileVo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;

/**
 * 本地文件上传
 */
@Slf4j
public class LocalUtils extends BaseFileUtils {

    @SneakyThrows
    public static UploadFileVo uploadFile(MultipartFile file, String serverUrl, String uploadPath) {
        String fileName = getFileName(file);
        String fileType = getFileType(file);
        // 文件路径
        String fileKey = getFileKey(uploadPath, fileType);
        InputStream inputStream = file.getInputStream();
        // 文件拷贝
        file.transferTo(new File(uploadPath + FileNameUtil.UNIX_SEPARATOR + fileKey));
        // 组装对象
        UploadFileVo fileVo = format(fileName, serverUrl, fileKey, fileType, inputStream)
                .setFullPath(serverUrl + fileKey);
        return fileVo;
    }

    public static UploadFileVo uploadFile(File file, String serverUrl, String uploadPath) {
        String fileName = getFileName(file);
        String fileType = getFileType(file);
        // 文件路径
        String fileKey = getFileKey(uploadPath, fileType);
        // 文件拷贝
        FileUtil.copyFile(file, new File(uploadPath + FileNameUtil.UNIX_SEPARATOR + fileKey));
        // 组装对象
        UploadFileVo fileVo = format(fileName, serverUrl, fileKey, fileType, null)
                .setFullPath(serverUrl + fileKey);
        return fileVo;
    }

    @SneakyThrows
    private static String getFileKey(String uploadPath, String fileType) {
        // 文件路径
        String filePath = DateUtil.format(DateUtil.date(), "yyyy/MM/dd");
        // 生成文件夹
        FileUtil.mkdir(uploadPath + FileNameUtil.UNIX_SEPARATOR + filePath);
        return filePath + FileNameUtil.UNIX_SEPARATOR + IdUtil.objectId() + "." + fileType;
    }

}
