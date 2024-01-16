package com.neteast.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neteast.business.domain.UploadFile;
import com.neteast.business.domain.vo.UploadFileVO;
import com.neteast.business.mapper.UploadFileMapper;
import com.neteast.business.service.IUploadFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author lzp
 * @date 2024年01月16 16:17
 */

@Service
@Slf4j
public class UploadFileServiceImpl extends ServiceImpl<UploadFileMapper, UploadFile> implements IUploadFileService {

    @Resource
    UploadFileMapper uploadFileMapper;

    @Override
    public List<UploadFileVO> getUploadFileVOListByProjectId(Integer projectId) {
        return uploadFileMapper.getListByProjectId(projectId);
    }

    @Override
    public boolean removeFile(Integer id) {

        UploadFile file = getById(id);
        if (file==null){
            return true;
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

    @Override
    public boolean updateFileByProjectId(List<Integer> ids, Integer projectId) {
        ids.forEach(id->{
            this.lambdaUpdate().eq(UploadFile::getId,id).set(UploadFile::getProjectId,projectId).update();
        });
        return true;
    }

    @Override
    public boolean removeFileByProjectId(Integer projectId) {
        List<UploadFile> files =  this.lambdaQuery().eq(UploadFile::getProjectId,projectId).list();
        QueryWrapper<UploadFile> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("project_id",projectId);
        this.remove(queryWrapper);
        //文件删除
        files.forEach(f->{
            Path path = Paths.get(f.getFileAddress());
            try {
                Files.deleteIfExists(path);
            }catch (IOException e){
                log.info("文件不存在-{}",path);
            }
        });
        return true;
    }
}
