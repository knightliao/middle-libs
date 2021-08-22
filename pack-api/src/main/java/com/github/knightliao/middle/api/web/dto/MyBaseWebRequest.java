package com.github.knightliao.middle.api.web.dto;

import com.github.knightliao.middle.api.core.dto.MyBaseRequest;

import lombok.Data;

/**
 * @author knightliao
 * @email knightliao@gmail.com
 * @date 2021/8/20 00:46
 */
@Data
public class MyBaseWebRequest extends MyBaseRequest {

    private Long uid;

    // 系统手机语言
    private String lang;

    private String utdid;

    private String ver;

    private String country;

    private String ticket;

    private String ip;

    private int debug;

    // 分辨率
    private String ss;

    // 纬度
    private String lat;

    // 经度
    private String lon;
}
