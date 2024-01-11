package com.neteast.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.neteast.business.domain.common.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * 违约项目清单
 * @author lzp
 * @date 2024年01月10 17:26
 */

@Data
@TableName("break_contract_file")
public class BreakContractFile extends BaseEntity {

    /** 主键id */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    /** 计量期数 */
    @TableField(value = "measure")
    private String measure;

    /** 项目类型 */
    @TableField("contract_type")
    private Integer contractType;

    /** 文件地址 */
    @TableField("file_address")
    private String fileAddress;

    /** 文号 */
    @TableField("document_number")
    private String documentNumber;

    /** 标题 */
    @TableField("title")
    private String title;

    /** 奖励金额 */
    @TableField("reward")
    private Double reward = 0.0D;

    /** EPC-1 */
    @TableField("epc_one")
    private Double EpcOne = 0.0D;

    /** EPC-2 */
    @TableField("epc_two")
    private Double EpcTwo = 0.0D;

    /** J1 */
    @TableField("j_one")
    private Double JOne = 0.0D;

    /** J2 */
    @TableField("j_two")
    private Double JTwo = 0.0D;

    /** 单位 */
    @TableField("unit")
    private String unit;

    /** 处理时间 */
    @TableField("handle_time")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date handleTime;
}
