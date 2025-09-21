package com.spring.picturebackend.controller;

import com.qcloud.cos.model.COSObject;
import com.qcloud.cos.model.COSObjectInputStream;
import com.qcloud.cos.utils.IOUtils;
import com.spring.picturebackend.annotation.AuthCheck;
import com.spring.picturebackend.common.BaseResponse;
import com.spring.picturebackend.common.ResultUtils;
import com.spring.picturebackend.constant.UserConstant;
import com.spring.picturebackend.exception.BusinessException;
import com.spring.picturebackend.exception.ErrorCode;
import com.spring.picturebackend.manager.CosManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    @Resource
    private CosManager cosManager; // key, file

    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @PostMapping("/test/upload")
    public BaseResponse<String> testUploadFile(@RequestPart("file") MultipartFile multipartFile) {
        // 文件目录
        String filename = multipartFile.getOriginalFilename();
        String filePath = String.format("/test/%s", filename);
        File tempFile = null;
        try {
            // 上传文件 创建一个临时的模板文件 multipartFile -> file
            tempFile = File.createTempFile(filePath, null);
            // 此时 tempFile 还是没有内容的，得从 multipartFile 拷贝一份
            multipartFile.transferTo(tempFile);
            // 上传到 COS 中
            cosManager.putObject(filePath, tempFile);
            // 返回可访问地址
            return ResultUtils.success(filePath);
        } catch (IOException e) {
            log.error("file upload error, filepath = {}", filePath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件上传失败");
        } finally {
            if (tempFile != null) {
                boolean delete = tempFile.delete();
                if (!delete) {
                    log.error("file delete error, filepath = {}", filePath);
                }
            }
        }
    }

    /**
     * 测试文件下载
     *
     * @param filepath 文件路径
     * @param response 响应对象
     */
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @GetMapping("/test/download/")
    public void testDownloadFile(String filepath, HttpServletResponse response) throws IOException {
        COSObjectInputStream cosObjectInputStream = null;
        try {
            // 下载对象
            COSObject cosObject = cosManager.getObject(filepath);
            // 处理下载到的流
            cosObjectInputStream = cosObject.getObjectContent();
            // 将流转化为 byte[]
            byte[] bytes = IOUtils.toByteArray(cosObjectInputStream);
            // 设置响应头
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + filepath);
            // 写入响应 getOutputStream 需要一个 bytes[] 数组
            response.getOutputStream().write(bytes);
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("file download error, filepath = {}", filepath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "下载图片失败");
        } finally {
            if (cosObjectInputStream != null) {
                // 一定得关闭 stream
                cosObjectInputStream.close();
            }
        }
    }
}























