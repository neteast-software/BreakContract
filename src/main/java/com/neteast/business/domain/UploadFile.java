package com.neteast.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.neteast.business.domain.common.BaseEntity;
import lombok.Data;

/**
 * 上传的文件
 * @author lzp
 * @date 2024年01月16 11:37
 */

@Data
@TableName("upload_file")
public class UploadFile extends BaseEntity {

    /** 主键id */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    /** 项目id */
    @TableField(value = "project_id")
    private Integer projectId;

    /** 文件名称 */
    @TableField(value = "file_name")
    private String fileName;

    /** 文件地址 */
    @TableField("file_address")
    private String fileAddress;

    /** 交易类型 */
    @TableField("contract_type")
    private Integer contractType;

    @TableField("file_size")
    private Double fileSize;
}
