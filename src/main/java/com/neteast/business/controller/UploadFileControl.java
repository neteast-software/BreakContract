package com.neteast.business.controller;

import com.alibaba.fastjson2.JSON;
import com.neteast.business.domain.LoginUser;
import com.neteast.business.domain.UploadFile;
import com.neteast.business.domain.common.AjaxResult;
import com.neteast.business.domain.enums.ContractType;
import com.neteast.business.domain.vo.UploadFileVO;
import com.neteast.business.service.IUploadFileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author lzp
 * @date 2024年01月16 16:18
 */

@RestController
@RequestMapping("/uploadFile")
public class UploadFileControl extends BaseController{

    @Resource
    IUploadFileService uploadFileService;

    @Value("${contract.file.path}")
    public String filePath;

    @PostMapping("/upload")
    public AjaxResult uploadFile(@RequestParam(value = "file",required = false) MultipartFile file,
                                 @RequestParam("contractType")Integer contractType,
                                 @RequestAttribute(value = "userMsg")String userMsg) throws IOException {
        if (file==null){
            return error("上传文件为空");
        }
        LoginUser user = JSON.parseObject(userMsg,LoginUser.class);
        //无目录进行创建
        String dirType = ContractType.getType(contractType);
        String realFilePath = filePath+ File.separator+dirType;
        boolean res = true;
        File dir = new File(realFilePath);
        if (!dir.exists()){
            res = dir.mkdir();
        }
        if (res){
            String originName = file.getOriginalFilename();
            if (originName!=null){
                UploadFile uploadFile = new UploadFile();
                String fileName = StringUtils.cleanPath(originName);
                String fileAddress = getFileAddress(fileName,realFilePath,contractType);
                if (fileAddress==null){
                    return error("该文件已经存在");
                }
                //文件信息保存
                uploadFile.setFileName(fileName);
                uploadFile.setFileAddress(fileAddress);
                uploadFile.setCreateMsg(user);
                uploadFile.setContractType(contractType);
                uploadFileService.save(uploadFile);
                //文件保存
                Path path = Paths.get(fileAddress);
                Files.copy(file.getInputStream(),path);
                UploadFileVO vo = UploadFileVO.convert(uploadFile);
                return success(vo);
            }else {
                return error("无法获取文件名称");
            }
        }else {
            return error("无法创建文件目录，请手动创建目录:"+realFilePath);
        }
    }

    /**
     * @Description 获取文件
     * @author lzp
     * @Date 2024/1/16
     */
    @GetMapping("/getFile/{id}")
    public ResponseEntity<org.springframework.core.io.Resource> getFile(@PathVariable("id")Integer id){

        //获取文件信息
        UploadFile file = uploadFileService.getById(id);
        if (file==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
        String path = file.getFileAddress();
        String fileName = file.getFileName();
        org.springframework.core.io.Resource resource = new FileSystemResource(path);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/octet-stream"));
        headers.setContentDispositionFormData("attachment", new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
        logger.info("下载文件名称-{}",fileName);
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }

    @PostMapping("/del/{id}")
    public AjaxResult delFile(@PathVariable("id")Integer id){

        boolean res = uploadFileService.removeFile(id);
        if (!res){
            return error("文件删除失败");
        }
        return success();
    }

    /**
     * @Description 获取文件地址
     * @author lzp
     * @Date 2024/1/12
     */
    private synchronized String getFileAddress(String fileName,String realFilePath,Integer contractType){

        //String title = fileName.replaceFirst("\\.\\w+$", "");
        //String type = fileName.substring(fileName.lastIndexOf('.')+1);
        //long timeStamp = new Date().getTime();
        List<UploadFile> uploadFiles = this.uploadFileService.lambdaQuery().eq(UploadFile::getFileName,fileName)
                .eq(UploadFile::getContractType,contractType).list();
        if (uploadFiles.size()!=0){
            for (UploadFile file:uploadFiles) {
                if (file.getProjectId()!=null){
                    return null;
                }else {
                    //进行文件删除
                    uploadFileService.removeFile(file.getId());
                }
            }
        }
        return realFilePath+File.separator+fileName;
    }
    
}
