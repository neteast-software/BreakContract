package com.neteast.business.domain.common;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.neteast.business.domain.LoginUser;
import lombok.Data;

import java.util.Date;

/**
 * @author lzp
 * @date 2024年01月10 17:47
 */

@Data
public class BaseEntity {

    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @TableField("create_by")
    private String createBy;

    @TableField("create_phone")
    private String createPhone;

    @TableField("update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @TableField("update_by")
    private String updateBy;

    @TableField("update_phone")
    private String updatePhone;

    public void setCreateMsg(LoginUser loginUser){
        this.createBy = loginUser.getUsername();
        this.createPhone = loginUser.getTel();
    }

    public void setUpdateMsg(LoginUser loginUser){
        this.updateBy = loginUser.getUsername();
        this.updatePhone = loginUser.getTel();
    }

}
