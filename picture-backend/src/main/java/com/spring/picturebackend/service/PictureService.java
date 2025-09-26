package com.spring.picturebackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.picturebackend.model.dto.picture.PictureQueryRequest;
import com.spring.picturebackend.model.dto.picture.PictureReviewRequest;
import com.spring.picturebackend.model.dto.picture.PictureUploadRequest;
import com.spring.picturebackend.model.entity.Picture;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.picturebackend.model.entity.User;
import com.spring.picturebackend.model.vo.PictureVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
* @author 姚东名
* @description 针对表【picture(图片)】的数据库操作Service
* @createDate 2025-09-21 19:30:46
*/
public interface PictureService extends IService<Picture> {

    /**
     * 校验图片
     *
     * @param picture
     */
    void validPicture(Picture picture);

    /**
     * 上传图片
     *
     * @param multipartFile
     * @param pictureUploadRequest
     * @param loginUser
     * @return
     */
    // PictureVO uploadPicture(MultipartFile multipartFile, PictureUploadRequest pictureUploadRequest, User loginUser);

    /**
     * 上传图片或者 URL
     *
     * @param inputSource
     * @param pictureUploadRequest
     * @param loginUser
     * @return
     */
    PictureVO uploadPicture(Object inputSource, PictureUploadRequest pictureUploadRequest, User loginUser);

    /**
     * 获取查询对象
     *
     * @param pictureQueryRequest
     * @return
     */
    QueryWrapper<Picture> getQueryWrapper(PictureQueryRequest pictureQueryRequest);

    /**
     * 获取图片包装类（单条）
     *
     * @param picture
     * @param request
     * @return
     */
    PictureVO getPictureVO(Picture picture, HttpServletRequest request);

    /**
     * 获取图片包装类（分页）
     *
     * @param picturePage
     * @param request
     * @return
     */
    Page<PictureVO> getPictureVOPage(Page<Picture> picturePage, HttpServletRequest request);

    /**
     * 图片审核
     *
     * @param pictureReviewRequest
     * @param loginUser
     */
    void doPictureReview(PictureReviewRequest pictureReviewRequest, User loginUser);

    /**
     * 管理员自动过审
     *
     * @param picture
     * @param loginUser
     */
    void fillReviewParams(Picture picture, User loginUser);
}
