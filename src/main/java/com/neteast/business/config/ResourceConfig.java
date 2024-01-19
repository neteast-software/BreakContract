package com.neteast.business.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * 资源映射配置
 * @author lzp
 * @date 2024年01月19 13:36
 */

@Configuration
public class ResourceConfig implements WebMvcConfigurer {

    /**
     * 上传文件存储在本地的根路径
     */
    @Value("${contract.file.path}")
    private String localFilePath;

    /**
     * 资源映射路径 前缀
     */
    @Value("${contract.file.prefix}")
    public String localFilePrefix;

    /**
     * 上传文件存储在本地的根路径
     */
    @Value("${contract.file.view-path}")
    private String viewLocalFilePath;

    /**
     * 资源映射路径 前缀
     */
    @Value("${contract.file.view-prefix}")
    public String viewLocalFilePrefix;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        /** 本地文件上传路径 */
        registry.addResourceHandler(viewLocalFilePrefix + "/**")
                .addResourceLocations("file:" + viewLocalFilePath + File.separator)
                .setCacheControl(CacheControl.empty().cachePublic().maxAge(48, TimeUnit.HOURS));
        registry.addResourceHandler(localFilePrefix + "/**")
                .addResourceLocations("file:" + localFilePath + File.separator)
                .setCacheControl(CacheControl.empty().cachePublic().maxAge(48, TimeUnit.HOURS));
    }

    /**
     * 开启跨域
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 设置允许跨域的路由
        registry.addMapping(localFilePrefix  + "/**")
                // 设置允许跨域请求的域名
                .allowedOrigins("*")
                // 设置允许的方法
                .allowedMethods("GET");
        registry.addMapping(viewLocalFilePrefix  + "/**")
                // 设置允许跨域请求的域名
                .allowedOrigins("*")
                // 设置允许的方法
                .allowedMethods("GET");
    }
}
