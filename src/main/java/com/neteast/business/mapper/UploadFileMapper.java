package com.neteast.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neteast.business.domain.UploadFile;
import com.neteast.business.domain.vo.UploadFileVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author lzp
 * @date 2024年01月16 16:18
 */

@Mapper
public interface UploadFileMapper extends BaseMapper<UploadFile> {

    List<UploadFile> getListByProjectId(Integer projectId);
}
