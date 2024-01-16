package com.neteast.business.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neteast.business.domain.BreakContractFile;
import com.neteast.business.domain.LoginUser;
import com.neteast.business.domain.vo.BreakContractFileVO;
import com.neteast.business.mapper.BreakContractFileMapper;
import com.neteast.business.service.IBreakContractFileService;
import com.neteast.business.service.IUploadFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author lzp
 * @date 2024年01月10 18:30
 */

@Service
@Slf4j
public class BreakContractFileServiceImpl extends ServiceImpl<BreakContractFileMapper, BreakContractFile> implements IBreakContractFileService{

    @Resource
    private BreakContractFileMapper fileMapper;

    @Resource
    private IUploadFileService uploadFileService;


    @Autowired
    public void setBreakContractFileMapper(BreakContractFileMapper mapper){
        this.fileMapper = mapper;
    }

    @Override
    public List<BreakContractFile> getBreakContractFileList(BreakContractFile file) {
        return fileMapper.getList(file);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addBreakContract(BreakContractFile file, List<Integer> ids) {
        fileMapper.insert(file);
        uploadFileService.updateFileByProjectId(ids,file.getId());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeBreakContract(Integer id) {

        uploadFileService.removeFileByProjectId(id);
        //文件信息删除
        removeById(id);
        return true;
    }

    @Override
    public boolean updateBreakContract(BreakContractFile file, List<Integer> ids) {

        uploadFileService.updateFileByProjectId(ids,file.getId());
        this.updateById(file);
        return true;
    }
}
