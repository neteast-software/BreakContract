package com.neteast.business.controller;

import com.alibaba.fastjson2.JSON;
import com.github.pagehelper.PageInfo;
import com.neteast.business.domain.BreakContractFile;
import com.neteast.business.domain.LoginUser;
import com.neteast.business.domain.UploadFile;
import com.neteast.business.domain.common.AjaxResult;
import com.neteast.business.domain.vo.BreakContractFileVO;
import com.neteast.business.domain.vo.UploadFileVO;
import com.neteast.business.service.IBreakContractFileService;
import com.neteast.business.service.IUploadFileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lzp
 * @date 2024年01月10 17:57
 */

@RestController
@RequestMapping("/breakContract")
public class BreakContractFileControl extends BaseController{

    @Value("${contract.file.path}")
    public String filePath;

    @Resource
    private IBreakContractFileService breakContractFileService;

    @Resource
    private IUploadFileService uploadFileService;

    @GetMapping("/listByPage")
    public AjaxResult getList(BreakContractFile breakContractFile){

        startPage();
        List<BreakContractFileVO> voList = new ArrayList<>();
        List<BreakContractFile> fileList = breakContractFileService.getBreakContractFileList(breakContractFile);
        Long count = new PageInfo(fileList).getTotal();
        fileList.forEach(file -> {
            BreakContractFileVO vo = BreakContractFileVO.convert(file);
            List<UploadFileVO> fileVOS = new ArrayList<>();
            List<UploadFile> files = uploadFileService.getUploadFileVOListByProjectId(file.getId());
            files.forEach(f->{
                UploadFileVO fileVO = UploadFileVO.convert(f);
                String url = getUrl(f);
                fileVO.setUrl(url);
                fileVOS.add(fileVO);
            });
            vo.setFileIds(fileVOS);
            voList.add(vo);
        });
        AjaxResult result = success();
        result.put("count",count);
        result.put("data",voList);
        return result;
    }

    @PostMapping("/del/{id}")
    public AjaxResult delBreakContractFile(@PathVariable("id") Integer id){

        breakContractFileService.removeBreakContract(id);
        return success("删除成功");
    }

    @PostMapping("/update")
    public AjaxResult updateBreakContractFile(@RequestBody BreakContractFileVO vo,@RequestAttribute(value = "userMsg")String userMsg){

        logger.info("用户信息-{}",userMsg);
        LoginUser user = JSON.parseObject(userMsg,LoginUser.class);
        BreakContractFile file = BreakContractFileVO.convert(vo);
        file.setUpdateMsg(user);
        List<UploadFileVO> uploadFiles = vo.getFileIds();
        if (uploadFiles!=null){
            List<Integer> ids = uploadFiles.stream().map(UploadFileVO::getId).collect(Collectors.toList());
            breakContractFileService.updateBreakContract(file,ids);
        }
        return success();
    }

    @PostMapping("/add")
    public AjaxResult addBreakContractFile(@RequestBody BreakContractFileVO vo,@RequestAttribute(value = "userMsg")String userMsg){

        LoginUser user = JSON.parseObject(userMsg,LoginUser.class);
        List<UploadFileVO> uploadFiles = vo.getFileIds();
        List<Integer> ids = uploadFiles.stream().map(UploadFileVO::getId).collect(Collectors.toList());
        BreakContractFile file = BreakContractFileVO.convert(vo);
        Long count = breakContractFileService.lambdaQuery().eq(BreakContractFile::getDocumentNumber,file.getDocumentNumber()).count();
        if (count!=0){
            return error("文号重复");
        }
        file.setCreateMsg(user);
        Boolean res = breakContractFileService.addBreakContract(file,ids);
        if (!res){
            return error("添加失败");
        }
        return success("添加成功");
    }

    private String getUrl(UploadFile file){

        int begin = file.getFileAddress().lastIndexOf(File.separator);
        int end = file.getFileAddress().lastIndexOf(File.separator,begin-1);
        String dir = file.getFileAddress().substring(end+1,begin);
        String name = file.getFileAddress().substring(begin+1);
        return "/static/"+dir+"/"+name;
    }

}
