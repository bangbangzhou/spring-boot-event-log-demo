package com.zbbmeta.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author springboot葵花宝典
 * @description: TODO
 */
@ToString
@Data
@Accessors(chain = true)
public class OptLogDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 日志类型
     */
    private String type;

    /**
     * 日志标题
     */
    private String title;
    /**
     * 操作内容
     */
    private String operation;
    /**
     * 执行方法
     */

    private String method;

    /**
     * 请求路径
     */
    private String url;
    /**
     * 参数
     */
    private String params;
    /**
     * ip地址
     */
    private String ip;
    /**
     * 耗时
     */
    private Long executeTime;
    /**
     * 地区
     */
    private String location;
    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date startTime;
    /**
     * 更新时间
     */
    private Date endTime;


    /**
     * 异常信息
     */

    private String exception;
}