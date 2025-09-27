package com.spring.picturebackend.manager.upload;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.ciModel.persistence.ImageInfo;
import com.spring.picturebackend.config.CosClientConfig;
import com.spring.picturebackend.exception.BusinessException;
import com.spring.picturebackend.exception.ErrorCode;
import com.spring.picturebackend.manager.CosManager;
import com.spring.picturebackend.model.dto.file.UploadPictureResult;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * 上传图片的模板方法
 */
@Slf4j
public abstract class PictureUploadTemplate {

    @Resource
    private CosManager cosManager;

    @Resource
    private CosClientConfig cosClientConfig;

    public UploadPictureResult uploadPicture(Object inputSource, String uploadPathPrefix) {
        // 1. 图片参数校验
        validPicture(inputSource);
        // 2. 图片上传地址 uploadPath = uploadPathPrefix + uploadPictureFileName(data_uuid.getOriginFileName)
        String originFileName = getOriginFileName(inputSource);
        String uuid = RandomUtil.randomString(16);
        String uploadPictureFileName = String.format("%s_%s.%s", DateUtil.formatDate(new Date()), uuid, FileUtil.getSuffix(originFileName));
        String uploadPath = String.format("/%s/%s", uploadPathPrefix, uploadPictureFileName);
        // 3. 创建临时文件
        File file = null;
        try {
            file = File.createTempFile(uploadPath, null);
            // 4. 判断是图片文件还是 URL 上传 processFile(inputSource, file);
            processFile(inputSource, file);
            // 5. 上传图片到对象存储中
            PutObjectResult putObjectResult = cosManager.putPictureObject(uploadPath, file);
            ImageInfo imageInfo = putObjectResult.getCiUploadResult().getOriginalInfo().getImageInfo();
            // 6. 封装图片对象并返回 buildResult
            return buildResult(originFileName, file, uploadPath, imageInfo);
        } catch (IOException e) {
            log.error("图片上传对象存储失败", e);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "图片上传失败");
        } finally {
            // 7. 将临时文件清除
            this.deleteTemplate(file);
        }

    }

    /**
     * 判断是图片文件还是 URL 上传
     *
     * @param inputSource
     * @param file
     */
    protected abstract void processFile(Object inputSource, File file) throws IOException;

    /**
     * 获取文件原始名称
     *
     * @param inputSource
     * @return
     */
    protected abstract String getOriginFileName(Object inputSource);

    /**
     * 图片参数校验
     *
     * @param inputSource
     */
    protected abstract void validPicture(Object inputSource);

    /**
     * 封装返回结果
     */
    private UploadPictureResult buildResult(String originFilename, File file, String uploadPath, ImageInfo imageInfo) {
        UploadPictureResult uploadPictureResult = new UploadPictureResult();
        int picWidth = imageInfo.getWidth();
        int picHeight = imageInfo.getHeight();
        double picScale = NumberUtil.round(picWidth * 1.0 / picHeight, 2).doubleValue();
        uploadPictureResult.setPicName(FileUtil.getName(originFilename));
        uploadPictureResult.setPicWidth(picWidth);
        uploadPictureResult.setPicHeight(picHeight);
        uploadPictureResult.setPicScale(picScale);
        uploadPictureResult.setPicFormat(imageInfo.getFormat());
        uploadPictureResult.setPicSize(FileUtil.size(file));
        uploadPictureResult.setUrl(cosClientConfig.getHost() + "/" + uploadPath);
        return uploadPictureResult;
    }

    /**
     * 删除临时文件
     */
    public void deleteTemplate(File file) {
        if (file == null) {
            return;
        }
        boolean delete = file.delete();
        if (!delete) {
            log.error("file delete error, filepath = {}", file.getAbsolutePath());
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "删除失败");
        }
    }
}
