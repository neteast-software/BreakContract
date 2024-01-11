package com.neteast.business.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author lzp
 * @date 2024年01月11 15:19
 */

@Data
public class LoginUser {

    /** 用户名 */
    private String username;

    /** 电话 */
    private String tel;

    /** 有效时间 */
    private Date validTime;

    public boolean valid(){
        if (username==null||tel==null||validTime==null){
            return false;
        }
        return true;
    }
}
