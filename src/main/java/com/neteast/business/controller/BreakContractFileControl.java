package com.neteast.business.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSON;
import com.github.pagehelper.PageInfo;
import com.neteast.business.domain.BreakContractFile;
import com.neteast.business.domain.LoginUser;
import com.neteast.business.domain.common.AjaxResult;
import com.neteast.business.domain.vo.BreakContractFileVO;
import com.neteast.business.exception.BaseBusException;
import com.neteast.business.filter.AuthorizationFilter;
import com.neteast.business.service.IBreakContractFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lzp
 * @date 2024年01月10 17:57
 */

@RestController
@RequestMapping("/breakContract")
public class BreakContractFileControl extends BaseController{

    private String HEADER = "userMsg";

    private static final int NO_AUTHORIZATION = 401;

    private static final String NO_AUTHORIZATION_MSG = "身份鉴权失败，请重新登录！";

    private IBreakContractFileService breakContractFileService;

    @Autowired
    public void setBreakContractFileService(IBreakContractFileService service){
        this.breakContractFileService = service;
    }

    @Value("${contract.file.path}")
    public String filePath;

    @Value("${contract.time}")
    public Integer outTime;

    @GetMapping("/listByPage")
    public AjaxResult getList(BreakContractFile breakContractFile){

        startPage();
        List<BreakContractFileVO> voList = new ArrayList<>();
        List<BreakContractFile> fileList = breakContractFileService.getBreakContractFileList(breakContractFile);
        Long count = new PageInfo(fileList).getTotal();
        fileList.forEach(file -> {
            BreakContractFileVO vo = BreakContractFileVO.convert(file);
            voList.add(vo);
        });
        AjaxResult result = success();
        result.put("count",count);
        result.put("data",voList);
        return result;
    }

    @PostMapping("/del/{id}")
    public AjaxResult delBreakContractFile(@PathVariable("id") Integer id){

        boolean res = breakContractFileService.removeBreakContract(id);
        if (!res){
            return error("文件删除失败");
        }
        return success();
    }

    @PostMapping("/update")
    public AjaxResult updateBreakContractFile(@RequestBody BreakContractFile file,@RequestAttribute(value = "userMsg",required = false)String userMsg){

        logger.info("用户信息-{}",userMsg);
        breakContractFileService.updateById(file);
        return success();
    }

    @PostMapping("/upload")
    public AjaxResult uploadBreakContractFile(@RequestParam(value = "file",required = false)MultipartFile file,
                                              @RequestParam("contractType")Integer contractType,
                                              @RequestParam("documentNumber")String documentNumber,
                                              @RequestParam("unit")String unit,
                                              @RequestParam("handleTime")Date handleTime,
                                              @RequestParam("measure")String measure,
                                              @RequestHeader(value = "userMsg",required = false)String userMsg) throws IOException {

        logger.info("用户信息-{}",userMsg);
        //无目录进行创建
        boolean res = true;
        File dir = new File(filePath);
        if (!dir.exists()){
            res = dir.mkdir();
        }
        Long count = breakContractFileService.lambdaQuery().eq(BreakContractFile::getDocumentNumber,documentNumber).count();
        if (count!=0){
            return error("该文号已存在");
        }
        //文件保存
        if (res){
            String originName = file.getOriginalFilename();
            if (originName!=null){
                String title = originName.replaceFirst("\\.\\w+$", "");
                String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                //保存文件信息
                BreakContractFile contractFile = getBreakContractFile(originName);
                contractFile.setFileAddress(filePath+File.separator+fileName);
                contractFile.setContractType(contractType);
                contractFile.setTitle(title);
                contractFile.setMeasure(measure);
                contractFile.setDocumentNumber(documentNumber);
                contractFile.setUnit(unit);
                contractFile.setHandleTime(handleTime);
                breakContractFileService.save(contractFile);
                //文件保存
                Path path = Paths.get(filePath+File.separator+fileName);
                Files.copy(file.getInputStream(),path);
            }else {
                return error("无法获取文件名称");
            }
        }else {
            return error("无法创建文件目录，请手动创建目录:"+filePath);
        }
        return success("文件保存成功");
    }

    private BreakContractFile getBreakContractFile(String originName){

        BreakContractFile file = new BreakContractFile();
        logger.info("保存文件为-{}",originName);
        //正则表达式获取括号中内容
        String content = getFileNameMsg(originName);
        if (content!=null){
            logger.info("文件名称括号中内容-{}",content);
            String[] temp = content.split(",");
            for (int i=0;i<temp.length;i++){
                String[] earn = temp[i].split("违约金");
                Double price = getPrice(earn[1]);
                switch (earn[0]){
                    case "EPC-1":
                    case "EPC-1标":
                        file.setEpcOne(price);
                        break;
                    case "EPC-2":
                    case "EPC-2标":
                        file.setEpcTwo(price);
                        break;
                    case "J1":
                    case "J1标":
                        file.setJOne(price);
                        break;
                    case "J2":
                    case "J2标":
                        file.setJTwo(price);
                        break;

                }
            }
        }
        return file;
    }

    private String getFileNameMsg(String name){

        ArrayList<String> matcher = new ArrayList<>();
        //中文括号
        Pattern chinese  = Pattern.compile("（([^（）]*)）");
        Matcher chinsesMatcher = chinese.matcher(name);
        while (chinsesMatcher.find()){
            matcher.add(chinsesMatcher.group());
        }
        //英文括号
        Pattern english = Pattern.compile("\\((.*?)\\)");
        Matcher englishMatcher = english.matcher(name);
        while (englishMatcher.find()){
            matcher.add(englishMatcher.group());
        }
        //过滤
        for (String temp:matcher) {
            if (temp.contains("EPC-1")||temp.contains("EPC-2")||temp.contains("J1")||temp.contains("J2")){
                return temp.substring(1,temp.length()-1);
            }
        }
        return null;
    }

    private Double getPrice(String earn){

        logger.info("金额为-{}",earn);
        //小数
        Pattern decimal = Pattern.compile("\\d+.\\d+");
        Matcher decimalMatcher = decimal.matcher(earn);
        //整数
        Pattern number = Pattern.compile("\\d+");
        Matcher numberMatcher = number.matcher(earn);
        //单位
        Pattern unit = Pattern.compile("\\D+");
        Matcher unitMatcher = unit.matcher(earn);

        String price = "0.0";
        String unitType = "万元";

        while (unitMatcher.find()){
            unitType = unitMatcher.group();
        }

        while (numberMatcher.find()){
            price = numberMatcher.group();
        }

        while (decimalMatcher.find()){
            price = decimalMatcher.group();
        }

        double temp = Double.parseDouble(price);
        switch (unitType){
            case "万元":
                return temp*10000;
        }
        return null;
    }

}
