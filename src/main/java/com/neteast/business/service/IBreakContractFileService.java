package com.neteast.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.neteast.business.domain.BreakContractFile;
import com.neteast.business.domain.vo.BreakContractFileVO;

import java.util.List;

/**
 * @author lzp
 * @date 2024年01月10 18:30
 */
public interface IBreakContractFileService extends IService<BreakContractFile> {

    List<BreakContractFile> getBreakContractFileList(BreakContractFile file);

    Boolean removeBreakContract(Integer id);

    Boolean addBreakContact(BreakContractFile file,List<Integer> id);
}
