package com.taikang.upload.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description:
 * @author: CaoRuiqun
 * @create: 2020-03-15 17:22
 **/
@Component
@ConfigurationProperties(prefix = "tk.upload")
public class UploadProperties {
    private String baseUrl;
    private List<String> allowTypes;

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public List<String> getAllowTypes() {
        return allowTypes;
    }

    public void setAllowTypes(List<String> allowTypes) {
        this.allowTypes = allowTypes;
    }
}
