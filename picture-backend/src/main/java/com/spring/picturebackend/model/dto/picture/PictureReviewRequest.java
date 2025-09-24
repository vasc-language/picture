package com.spring.picturebackend.model.dto.picture;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class PictureReviewRequest implements Serializable {

    /**
     * id
     */
    @NotNull(message = "图片ID不能为空")
    private Long id;

    /**
     * 状态：0-待审核, 1-通过, 2-拒绝
     */
    @NotNull(message = "审核状态不能为空")
    @Min(value = 0, message = "审核状态值无效")
    @Max(value = 2, message = "审核状态值无效")
    private Integer reviewStatus;

    /**
     * 审核信息
     */
    private String reviewMessage;


    private static final long serialVersionUID = 1L;
}
