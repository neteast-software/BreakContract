package com.neteast.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.neteast.business.domain.UploadFile;

import java.util.List;

/**
 * @author lzp
 * @date 2024年01月16 16:17
 */
public interface IUploadFileService extends IService<UploadFile> {

    boolean removeFile(Integer id);

    boolean updateFileByProjectId(List<Integer> ids,Integer projectId);

    boolean removeFileByProjectId(Integer projectId);
}
