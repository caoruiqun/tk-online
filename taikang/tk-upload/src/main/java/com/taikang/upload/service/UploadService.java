package com.taikang.upload.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author CaoRuiqun on 2020/3/15
 */
public interface UploadService {
    String uploadImage(MultipartFile file);
}
