package com.neteast.business.domain.common;

import cn.hutool.core.util.ObjectUtil;
import lombok.Data;

import java.util.HashMap;

/**
 * @author hj
 * @date 2023年10月19日 14:07
 */
@Data
public class BaseResult extends HashMap<String, Object> {

    /** 状态码 */
    public static final String CODE_TAG = "code";

    /** 返回内容 */
    public static final String MSG_TAG = "msg";

    /** 数据对象 */
    public static final String DATA_TAG = "data";

    public static final int SUCCESS = 200;
    public static final int FAIL = 500;

    public BaseResult(){}

    public BaseResult(int code, String msg){
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
    }

    public BaseResult(int code, String msg, Object data){
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        if (ObjectUtil.isNotNull(data))
        {
            super.put(DATA_TAG, data);
        }
    }

    public static BaseResult success(String msg, Object data){
        return new BaseResult(SUCCESS, msg, data);
    }

    public static BaseResult success(){
        return success("操作成功", null);
    }

    public static BaseResult success(Object data){
        return success("操作成功", data);
    }

    public static BaseResult error(String message) {
        return error(message, null);
    }

    public static BaseResult error(String message , Object data) {
        return new BaseResult(FAIL, message, data);
    }

    public static BaseResult error(int code , String message) {
        return new BaseResult(code, message);
    }
}
