package com.zbbmeta.listener;


import com.zbbmeta.dto.OptLogDTO;
import com.zbbmeta.event.SysLogEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 异步监听日志事件
 */
@Slf4j
@Component
public class SysLogListener {
    @Async//异步处理
    @EventListener(SysLogEvent.class)
    public void saveSysLog(SysLogEvent event) {
        OptLogDTO sysLog = (OptLogDTO) event.getSource();
        long id = Thread.currentThread().getId();
        //TODO 可以输出日志到数据库
        System.out.println("监听到日志操作事件：" + sysLog + " 线程id：" + id);

        //将日志信息保存到数据库...
    }
}