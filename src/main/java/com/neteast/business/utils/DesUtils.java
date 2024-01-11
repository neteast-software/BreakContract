package com.neteast.business.utils;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.DES;

import java.nio.charset.StandardCharsets;

/**
 * DES加密工具
 * @author lzp
 * @date 2023年10月19 17:10
 */
public class DesUtils {


    /**
     * @Description DES加密 使用ECB模式，UTF-8，填充方式PKCS7
     * @author lzp
     * @Date 2023/10/19
     */
    public static String desEnECB(String content,String key,String vi){

        byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
        DES des = new DES(Mode.ECB,Padding.PKCS5Padding,bytes);
        return des.encryptBase64(content);
    }

    /**
     * @Description DES解密 使用ECB模式，UTF-8，填充方式PKCS7
     * @Date 2023/10/20
     */
    public static String desDeECB(String content,String key,String vi){
        byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
        DES des = new DES(Mode.ECB,Padding.PKCS5Padding,bytes);

        return des.decryptStr(content);
    }

    public static void main(String[] args) {
        String s = desEnECB("bef19e4a921a4445b65a1192ab3df4e6", "FYOQGX43", "AQWH1EE7");
        System.out.println(s);
    }
}
