package com.taikang.order.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: 98050
 * @create: 2018-10-27
 **/
@Component
@ConfigurationProperties(prefix = "tk.worker")
public class IdWorkerProperties {

    /**
     * 当前机器id
     */
    private long workerId;

    /**
     * 序列号
     */
    private long dataCenterId;

    public long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(long workerId) {
        this.workerId = workerId;
    }

    public long getDataCenterId() {
        return dataCenterId;
    }

    public void setDataCenterId(long dataCenterId) {
        this.dataCenterId = dataCenterId;
    }
}
