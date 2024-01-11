package com.neteast.business.caches;

import cn.hutool.extra.spring.SpringUtil;
import com.github.benmanes.caffeine.cache.Cache;

/**
 * 本地缓存
 * 用户信息缓存使用(token)
 * @author hj
 * @date 2023年10月19日 10:29
 */
public class LocalCache {

    public final static String USER_INFO = "user_info:";


    private static Cache<String, Object> caffeineCache = SpringUtil.getBean(Cache.class);

    public static void putCache(String key, Object value){
        caffeineCache.put(key, value);
    }

    public static Object getCache(String key){
        return caffeineCache.getIfPresent(key);
    }

    public static <T> T getCache(String key, Class<T> clazz){
        return (T) caffeineCache.get(key, clazz::cast);
    }
}
