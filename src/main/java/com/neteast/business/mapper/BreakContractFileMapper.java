package com.neteast.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neteast.business.domain.BreakContractFile;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author lzp
 * @date 2024年01月10 18:31
 */

@Mapper
public interface BreakContractFileMapper extends BaseMapper<BreakContractFile> {

    List<BreakContractFile> getList(BreakContractFile file);

}
