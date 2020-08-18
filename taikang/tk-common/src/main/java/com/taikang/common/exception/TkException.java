package com.taikang.common.exception;

import com.taikang.common.enums.ExceptionEnum;

/**
 * @description:
 * @author: CaoRuiqun
 * @create: 2020-03-11 00:22
 **/
public class TkException extends RuntimeException {

    private ExceptionEnum exceptionEnum;

    public TkException() {
    }

    public TkException(ExceptionEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
    }

    public ExceptionEnum getExceptionEnum() {
        return exceptionEnum;
    }

}
