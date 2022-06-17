package com.platform.common.upload.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.InputStream;

/**
 * 文件上传
 */
@Data
@Accessors(chain = true) // 链式调用
public class UploadFileVo {

    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 文件地址
     */
    private String fullPath;
    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 文件流
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private InputStream inputStream;

}
