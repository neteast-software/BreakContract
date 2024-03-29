package com.neteast.business.domain.vo;

import cn.hutool.core.bean.BeanUtil;
import com.neteast.business.domain.UploadFile;
import lombok.Data;

import java.io.File;

/**
 * @author lzp
 * @date 2024年01月16 16:01
 */

@Data
public class UploadFileVO {

    private Integer id;

    private Double fileSize;

    private String fileName;

    private String url;

    public static UploadFileVO convert(UploadFile uploadFile){
        UploadFileVO vo = new UploadFileVO();
        BeanUtil.copyProperties(uploadFile,vo);
        return vo;
    }
}
