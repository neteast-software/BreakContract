package com.neteast.business.domain;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * @author lzp
 * @date 2024年01月11 15:19
 */

@Data
@Slf4j
@ToString
public class LoginUser {

    /** 用户名 */
    private String username;

    /** 电话 */
    private String tel;

    /** 有效时间 */
    private Date validTime;

    /** 时间戳 */
    private Long timestamp;

    public boolean valid(){
        if (username==null||tel==null||validTime==null){
            return false;
        }
        return true;
    }

    public String gainMD5(){
        log.info("加密前字符串为-{}",tel+username+timestamp+"09B4D22E12142406BB85DA671D1F9A1C");
        return tel+username+timestamp+"09B4D22E12142406BB85DA671D1F9A1C";
    }

}
