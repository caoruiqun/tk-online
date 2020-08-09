package com.taikang.common.vo;

import com.taikang.common.enums.ExceptionEnum;

/**
 * @description:
 * @author: CaoRuiqun
 * @create: 2020-03-11 20:04
 **/
public class ExceptionResult {

    private int status;
    private String message;
    private Long timeStamp;

    public ExceptionResult() {
    }

    public ExceptionResult(ExceptionEnum em) {
        this.status = em.getCode();
        this.message = em.getMsg();
        this.timeStamp = System.currentTimeMillis();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
