package com.neteast.business.domain.vo;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.neteast.business.domain.BreakContractFile;
import com.neteast.business.domain.enums.ContractType;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author lzp
 * @date 2024年01月11 13:50
 */

@Data
public class BreakContractFileVO {

    /** 主键id */
    private Integer id;

    /** 计量期数 */
    private String measure;

    private Integer contractType;

    /** 项目类型 */
    private String contractName;

    /** 文号 */
    private String documentNumber;

    /** 标题 */
    private String title;

    /** 奖励金额 */
    private Double reward;

    /** EPC-1 */
    private Double EpcOne;

    /** EPC-2 */
    private Double EpcTwo;

    /** J1 */
    private Double JOne;

    /** J2 */
    private Double JTwo;

    /** 单位 */
    private String unit;

    /** 处理时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date handleTime;

    private List<UploadFileVO> fileIds;

    public static BreakContractFileVO convert(BreakContractFile file){
        BreakContractFileVO vo = new BreakContractFileVO();
        BeanUtil.copyProperties(file,vo);
        //设置类型
        vo.setContractName(ContractType.getType(file.getContractType()));
        return vo;
    }

    public static BreakContractFile convert(BreakContractFileVO vo){
        BreakContractFile file = new BreakContractFile();
        BeanUtil.copyProperties(vo,file);
        return file;
    }
}
