package com.taikang.upload.service.impl;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.taikang.common.enums.ExceptionEnum;
import com.taikang.common.exception.TkException;
import com.taikang.upload.config.UploadProperties;
import com.taikang.upload.service.UploadService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @description:
 * @author: CaoRuiqun
 * @create: 2020-03-15 13:54
 **/
//@EnableConfigurationProperties(UploadProperties.class)
@Service
public class UploadServiceImpl implements UploadService {

    private static final Logger log = LoggerFactory.getLogger(UploadServiceImpl.class);

//    private static final List<String> ALLOW_TYPES = Arrays.asList("image/png", "image/jpeg", "image/jpg");

    @Autowired
    private FastFileStorageClient storageClient;

    @Autowired
    private UploadProperties uploadProperties;

    /**
     * 上传图片
     * @Param: [file]
     * @return:
     */
    @Override
    public String uploadImage(MultipartFile file) {
        try {
            //校验文件类型
            String contentType = file.getContentType();
            if (!uploadProperties.getAllowTypes().contains(contentType)) {
                throw new TkException(ExceptionEnum.INVALID_FILE_TYPE);
            }
            //校验文件内容
            BufferedImage image = ImageIO.read(file.getInputStream());
            if (null == image) {
                throw new TkException(ExceptionEnum.INVALID_FILE_TYPE);
            }

//            //准备文件路径
//            File dest = new File("D:/course/JavaProjects/taikang-mall/yun6/upload/", file.getOriginalFilename());
//            //保存文件到本地
//            file.transferTo(dest);
//            //返回文件路径
//            return "http://image.taikang.com" + file.getOriginalFilename();
            // 2、将图片上传到FastDFS
            // 2.1、获取文件后缀名
            String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
//            String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
            // 2.2、上传
            StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), extension, null);
            //返回文件路径
            return uploadProperties.getBaseUrl() + storePath.getFullPath();
        } catch (IOException e) {
            //上传失败
            //记录日志
            log.error("[文件上传] 文件上传失败！", e);
            //抛出异常
            throw new TkException(ExceptionEnum.FILE_UPLOAD_ERROR);
        }
    }

}
