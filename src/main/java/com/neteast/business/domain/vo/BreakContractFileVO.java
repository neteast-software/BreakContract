package com.neteast.business.domain.vo;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.neteast.business.domain.BreakContractFile;
import com.neteast.business.domain.enums.ContractType;
import lombok.Data;

import java.util.Date;

/**
 * @author lzp
 * @date 2024年01月11 13:50
 */

@Data
public class BreakContractFileVO {

    /** 主键id */
    private Integer id;

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
    private Date handleTime;

    public static BreakContractFileVO convert(BreakContractFile file){
        BreakContractFileVO vo = new BreakContractFileVO();
        BeanUtil.copyProperties(file,vo);
        //设置类型
        vo.setContractName(ContractType.getType(file.getContractType()));
        return vo;
    }
}
