package com.spring.picturebackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.spring.picturebackend.model.dto.picture.PictureQueryRequest;
import com.spring.picturebackend.model.dto.picture.PictureUploadRequest;
import com.spring.picturebackend.model.entity.Picture;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spring.picturebackend.model.entity.User;
import com.spring.picturebackend.model.vo.PictureVO;
import org.springframework.web.multipart.MultipartFile;

/**
* @author 姚东名
* @description 针对表【picture(图片)】的数据库操作Service
* @createDate 2025-09-21 19:30:46
*/
public interface PictureService extends IService<Picture> {

    /**
     * 上传图片
     *
     * @param multipartFile
     * @param pictureUploadRequest
     * @param loginUser
     * @return
     */
    PictureVO uploadPicture(MultipartFile multipartFile, PictureUploadRequest pictureUploadRequest, User loginUser);

    QueryWrapper<Picture> getQueryWrapper(PictureQueryRequest pictureQueryRequest);
}
