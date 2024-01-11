package com.neteast.business.exception;

import lombok.Data;

/**
 * @author lzp
 * @date 2023年11月20 15:11
 */

@Data
public class BaseBusException extends RuntimeException{

    /**
     * 错误码
     */
    private int code;

    /**
     * 错误提示
     */
    private String message;

    public BaseBusException(String message)
    {
        this.message = message;
    }

    public BaseBusException(int code, String message)
    {
        this.message = message;
        this.code = code;
    }
}
