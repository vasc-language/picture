package com.spring.picturebackend.manager.upload;

import cn.hutool.core.io.FileUtil;
import com.spring.picturebackend.exception.ErrorCode;
import com.spring.picturebackend.exception.ThrowUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 姚东名
 * Date: 2025-09-26
 * Time: 17:23
 */
@Service
public class FilePictureUpload extends PictureUploadTemplate {
    /**
     * 将上传的文件放到 file
     * @param inputSource
     * @param file
     */
    @Override
    protected void processFile(Object inputSource, File file) throws IOException {
        MultipartFile multipartFile = (MultipartFile) inputSource;
        multipartFile.transferTo(file);
    }

    @Override
    protected String getOriginFileName(Object inputSource) {
        MultipartFile multipartFile = (MultipartFile) inputSource;
        return multipartFile.getOriginalFilename();
    }

    /**
     * 验证图片是否合法
     *
     * @param inputSource
     */
    @Override
    protected void validPicture(Object inputSource) {
        MultipartFile multipartFile = (MultipartFile) inputSource;
        // 文件不能为空
        ThrowUtils.throwIf(multipartFile.isEmpty(), ErrorCode.PARAMS_ERROR, "文件不能为空");
        // 校验文件大小 getSize()
        long fileSize = multipartFile.getSize();
        final long MAX_SIZE = 1024 * 1024L;
        ThrowUtils.throwIf(fileSize > 2 * MAX_SIZE, ErrorCode.PARAMS_ERROR, "文件大小不能超过 2M");
        // 校验文件后缀 fileSuffix
        String fileSuffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        // 允许上传文件后缀 "jpeg", "jpg", "png", "webp"
        final List<String> ALLOW_FORMAT_LIST = Arrays.asList("jpeg", "jpg", "png", "webp");
        ThrowUtils.throwIf(!ALLOW_FORMAT_LIST.contains(fileSuffix), ErrorCode.PARAMS_ERROR, "文件格式不符合要求");
    }
}
