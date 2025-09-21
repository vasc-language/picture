package com.spring.picturebackend.controller;

import com.spring.picturebackend.annotation.AuthCheck;
import com.spring.picturebackend.common.BaseResponse;
import com.spring.picturebackend.common.ResultUtils;
import com.spring.picturebackend.constant.UserConstant;
import com.spring.picturebackend.model.dto.picture.PictureUploadRequest;
import com.spring.picturebackend.model.entity.User;
import com.spring.picturebackend.model.enums.UserRoleEnum;
import com.spring.picturebackend.model.vo.PictureVO;
import com.spring.picturebackend.service.PictureService;
import com.spring.picturebackend.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 姚东名
 * Date: 2025-09-21
 * Time: 22:25
 */
@RestController
@RequestMapping("/picture")
public class PictureController {

    @Resource
    private UserService userService;
    @Resource
    private PictureService pictureService;

    /**
     * 上传图片（可重新上传）
     */
    @PostMapping("/upload")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<PictureVO> uploadPicture(@RequestPart("file") MultipartFile multipartFile,
                                                 PictureUploadRequest pictureUploadRequest,
                                                 HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        PictureVO pictureVO = pictureService.uploadPicture(multipartFile, pictureUploadRequest, loginUser);
        return ResultUtils.success(pictureVO);
    }
}
