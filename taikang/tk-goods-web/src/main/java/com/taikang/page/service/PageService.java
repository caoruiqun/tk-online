package com.taikang.page.service;

import java.util.Map;

/**
 * @author CaoRuiqun on 2020/5/1
 */
public interface PageService {
    Map<String, Object> loadModel(Long spuId);

    void createHtml(Long spuId);

    void deleteHtml(Long spuId);
}
