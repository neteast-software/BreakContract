package com.neteast.business.filter;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import com.alibaba.fastjson2.JSON;
import com.neteast.business.domain.LoginUser;
import com.neteast.business.domain.common.BaseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;


@Component
@Slf4j
public class AuthorizationFilter implements Filter{

    private static final String[] FILTER_URL = {"/**"};

    private static final String[] WHITE_URL = {"/access/login","/camerasMock","/userMock/**","/static/**", "/view/**", "/access/loginTest/**"};

    private static final int NO_AUTHORIZATION = 401;

    private static final String NO_AUTHORIZATION_MSG = "身份鉴权失败，请重新登录！";

    /** 请求头携带 */
    private static final String TEL = "tel";

    private static final String NAME = "name";

    private static final String T = "t";

    private static final String SIGN = "sign";

    /** key值 */
    @Value("${contract.key}")
    public String key;

    /** 超时时间 */
    @Value("${contract.time}")
    public Integer outTime;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestUrl = request.getRequestURI();

        PathMatcher matcher = new AntPathMatcher();
        boolean match = false;
        for(String url : FILTER_URL){
            match = matcher.match(url, requestUrl);
            if (match){
                break;
            }
        }
        //不在过滤范围内
        if (!match){
            filterChain.doFilter(request, response);
            return;
        }
        //是否在白名单内
        for (String url : WHITE_URL) {
            if (matcher.match(url, requestUrl)){
                filterChain.doFilter(request, response);
                return;
            }
        }

        LoginUser loginUser = validRequest(request);

        if (loginUser!=null){
            log.info("校验成功-{}",loginUser);
            request.setAttribute("userMsg",JSON.toJSONString(loginUser));
            //通过生效
            filterChain.doFilter(request, response);
        }else {
            //返回登录失效
            renderString(response);
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    private LoginUser validRequest(HttpServletRequest request){

        //校验请求内容
        LoginUser loginUser = new LoginUser();
        String tel = request.getHeader(TEL);
        String name = request.getHeader(NAME);
        //中文编码转化
        name = UnicodeUtil.toString(name);
        String t = request.getHeader(T);
        loginUser.setTel(tel);
        loginUser.setUsername(name);
        //检验时间戳数据正规性，以防后面检验错误
        if (StrUtil.isBlank(t) || !NumberUtil.isNumber(t)){
            return null;
        }
        long time = Long.parseLong(t);
        loginUser.setTimestamp(time);
        Date start = DateUtil.date(time*1000);
        loginUser.setValidTime(start);
        //用户信息校验
        if (!loginUser.valid()){
            return null;
        }
        Date end = DateUtil.offsetMinute(start,outTime);
        Date now = new Date();
        if (DateUtil.compare(end,now)<0){
            return null;
        }
        log.info("用户信息-{}",loginUser);
        //验签
        String sign = request.getHeader(SIGN);
        boolean res = validMD5(sign, loginUser.gainMD5(key));
        if (!res){
            return null;
        }
        return loginUser;
    }

    /**
     * 将字符串渲染到客户端
     * @param response 渲染对象
     * @return null
     */
    public String renderString(HttpServletResponse response)
    {
        try
        {
            String jsonString = JSON.toJSONString(BaseResult.error(NO_AUTHORIZATION, NO_AUTHORIZATION_MSG));
            response.setStatus(NO_AUTHORIZATION);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(jsonString);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private boolean validMD5(String sign,String msg){
        MD5 md5 = MD5.create();
        String res = md5.digestHex(msg, StandardCharsets.UTF_8);
        res = res.toUpperCase();
        log.info("用户信息MD5加密结果-{}-比较{}",res,sign);
        if (res.equals(sign)){
            return true;
        }
        return false;
    }
}
