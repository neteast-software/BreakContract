package com.neteast.business.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neteast.business.domain.BreakContractFile;
import com.neteast.business.mapper.BreakContractFileMapper;
import com.neteast.business.service.IBreakContractFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private BreakContractFileMapper fileMapper;

    @Autowired
    public void setBreakContractFileMapper(BreakContractFileMapper mapper){
        this.fileMapper = mapper;
    }

    @Override
    public List<BreakContractFile> getBreakContractFileList(BreakContractFile file) {
        return fileMapper.getList(file);
    }

    @Override
    public Boolean removeBreakContract(Integer id) {

        BreakContractFile file = getById(id);
        if (file==null){
            return false;
        }
        //文件删除
        Path path = Paths.get(file.getFileAddress());
        try {
            Files.deleteIfExists(path);
        }catch (IOException e){
            log.info("文件不存在-{}",path);
        }
        //文件信息删除
        removeById(id);
        return true;
    }
}
