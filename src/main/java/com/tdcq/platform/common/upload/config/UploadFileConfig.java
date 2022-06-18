package com.platform.common.upload.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 文件上传
 */
@Data
@NoArgsConstructor
@Accessors(chain = true) // 链式调用
public class UploadFileConfig {

    /**
     * 服务端域名
     */
    private String serverUrl;
    /**
     * accessKey
     */
    private String accessKey;
    /**
     * secretKey
     */
    private String secretKey;
    /**
     * bucketName
     */
    private String bucketName;
    /**
     * region
     */
    private String region;

}
