package com.neteast.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.neteast.business.domain.BreakContractFile;

import java.util.List;

/**
 * @author lzp
 * @date 2024年01月10 18:30
 */
public interface IBreakContractFileService extends IService<BreakContractFile> {

    public List<BreakContractFile> getBreakContractFileList(BreakContractFile file);

    public Boolean removeBreakContract(Integer id);
}
