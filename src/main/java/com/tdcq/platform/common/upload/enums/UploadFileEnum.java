package com.platform.common.upload.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 上传类型枚举
 */
@Getter
public enum UploadFileEnum {

    /**
     * 阿里oss
     */
    OSS("1", "阿里oss"),

    /**
     * 腾讯cos
     */
    COS("2", "腾讯cos"),

    /**
     * 七牛kodo
     */
    KODO("3", "七牛kodo"),

    /**
     * FAST_DFS
     */
    FAST_DFS("4", "FAST_DFS"),

    /**
     * 本地local
     */
    LOCAL("5", "本地local"),

    /**
     * 其他
     */
    OTHER("6", "其他"),
    ;

    @EnumValue
    @JsonValue
    private final String code;
    private final String info;

    UploadFileEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

}
