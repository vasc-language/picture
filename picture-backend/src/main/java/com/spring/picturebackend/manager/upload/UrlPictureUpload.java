package com.spring.picturebackend.manager.upload;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.spring.picturebackend.exception.BusinessException;
import com.spring.picturebackend.exception.ErrorCode;
import com.spring.picturebackend.exception.ThrowUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 姚东名
 * Date: 2025-09-26
 * Time: 17:24
 */
@Service
public class UrlPictureUpload extends PictureUploadTemplate {
    /**
     * 使用工具类从 URL 中下载图片
     *
     * @param inputSource
     * @param file
     * @throws IOException
     */
    @Override
    protected void processFile(Object inputSource, File file) throws IOException {
        String fileUrl = (String) inputSource;
        HttpUtil.downloadFile(fileUrl, file);
    }

    /**
     * 从 URL 中获取原始文件名
     *
     * @param inputSource
     * @return
     */
    @Override
    protected String getOriginFileName(Object inputSource) {
        String fileUrl = (String) inputSource;
        // 用 URL 提取文件名
        return FileUtil.getName(fileUrl);
    }

    @Override
    protected void validPicture(Object inputSource) {
        String fileUrl = (String) inputSource;
        ThrowUtils.throwIf(StrUtil.isBlank(fileUrl), ErrorCode.PARAMS_ERROR, "文件地址不能为空");

        URL url;
        try {
            url = new URL(fileUrl);
        } catch (MalformedURLException e) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件地址格式不正确");
        }

        String protocol = url.getProtocol();
        ThrowUtils.throwIf(!(StrUtil.equalsIgnoreCase(protocol, "http") || StrUtil.equalsIgnoreCase(protocol, "https")),
                ErrorCode.PARAMS_ERROR, "仅支持 HTTP 或 HTTPS 协议的文件地址");

        this.checkForbiddenHost(url);

        HttpResponse response = null;
        try {
            response = HttpUtil.createRequest(Method.HEAD, fileUrl).execute();
            ThrowUtils.throwIf(response.getStatus() != HttpStatus.HTTP_OK,
                    ErrorCode.PARAMS_ERROR, "文件地址不可用");

            String contentType = response.header("Content-Type");
            if (StrUtil.isNotBlank(contentType)) {
                final List<String> ALLOW_CONTENT_TYPES = Arrays.asList("image/jpeg", "image/jpg", "image/png", "image/webp");
                String normalizedContentType = contentType.toLowerCase();
                ThrowUtils.throwIf(!ALLOW_CONTENT_TYPES.contains(normalizedContentType),
                        ErrorCode.PARAMS_ERROR, "文件类型错误");
            }

            String contentLengthStr = response.header("Content-Length");
            ThrowUtils.throwIf(StrUtil.isBlank(contentLengthStr), ErrorCode.PARAMS_ERROR, "无法获取文件大小");
            try {
                long contentLength = Long.parseLong(contentLengthStr);
                final long TWO_MB = 2 * 1024 * 1024L;
                ThrowUtils.throwIf(contentLength > TWO_MB, ErrorCode.PARAMS_ERROR, "文件大小不能超过 2M");
            } catch (NumberFormatException e) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小格式错误");
            }
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    private void checkForbiddenHost(URL url) {
        try {
            InetAddress[] addresses = InetAddress.getAllByName(url.getHost());
            for (InetAddress address : addresses) {
                if (isForbiddenAddress(address)) {
                    throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件地址不可用");
                }
            }
        } catch (UnknownHostException e) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件地址解析失败");
        }
    }

    private boolean isForbiddenAddress(InetAddress address) {
        return address.isAnyLocalAddress()
                || address.isLoopbackAddress()
                || address.isLinkLocalAddress()
                || address.isSiteLocalAddress()
                || address.isMulticastAddress();
    }
}
