package com.neteast.business.exception.global;

import com.neteast.business.domain.common.AjaxResult;
import com.neteast.business.exception.BaseBusException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常捕获
 * @author lzp
 * @date 2024年01月11 10:42
 */

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * @Description 请求方法不支持
     * @author lzp
     * @Date 2024/1/11
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public AjaxResult handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                                          HttpServletRequest request) {

        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',不支持'{}'请求", requestURI, e.getMethod());
        return AjaxResult.error(e.getMessage());
    }

    /**
     * @Description
     * @author lzp
     * @Date 2024/1/11
     */
    @ExceptionHandler(BaseBusException.class)
    @ResponseStatus(org.springframework.http.HttpStatus.BAD_REQUEST)
    public AjaxResult handleBaseBusException(BaseBusException e, HttpServletRequest request) {

        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生业务异常.", requestURI, e);
        return AjaxResult.error(e.getCode(), e.getMessage());
    }


}
