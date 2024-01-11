package com.neteast.business.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.neteast.business.domain.common.AjaxResult;
import com.neteast.business.domain.common.HttpStatus;
import com.neteast.business.utils.DateUtils;
import com.neteast.business.utils.PageUtils;
import com.neteast.business.utils.SqlUtil;
import com.neteast.business.utils.StringUtils;
import com.neteast.business.utils.page.PageDomain;
import com.neteast.business.utils.page.TableDataInfo;
import com.neteast.business.utils.page.TableSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * web层通用数据处理
 * 
 * @author ruoyi
 */
public class BaseController
{
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final static String FILTER_STR = "filter";
    private final static String NAME_STR = "name";
    private final static String ATTRS_STR = "attrs";
    private final static String OPTIONS_STR = "options";
    private final static String ITEMS_STR = "items";
    private final static String GRAPH_STR = "graph";

    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder)
    {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport()
        {
            @Override
            public void setAsText(String text)
            {
                setValue(DateUtils.parseDate(text));
            }
        });
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage()
    {
        PageUtils.startPage();
    }

    /**
     * 设置请求排序数据
     */
    protected void startOrderBy()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        if (StringUtils.isNotEmpty(pageDomain.getOrderBy()))
        {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.orderBy(orderBy);
        }
    }

    /**
     * 清理分页的线程变量
     */
    protected void clearPage()
    {
        PageUtils.clearPage();
    }

    /**
     * 初始化分页参数
     */
    protected JSONObject initPageParams(TableDataInfo dataTable, Integer pageSize, Integer pageNum){
        return initPageParams(new JSONObject(),dataTable,pageSize,pageNum);
    }

    /**
     * 初始化分页参数
     */
    protected JSONObject initPageParams(JSONObject jsonObject, TableDataInfo dataTable, Integer pageSize, Integer pageNum){
        jsonObject.put("data", dataTable.getRows());
        jsonObject.put("total", dataTable.getTotal());
        jsonObject.put("size", pageSize);
        jsonObject.put("page", pageNum);
        jsonObject.put("sizes", Arrays.asList(1,10,20,30,50));
        return jsonObject;
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected TableDataInfo getDataTable(List<?> list)
    {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

    /**
     * 返回成功
     */
    public AjaxResult success()
    {
        return AjaxResult.success();
    }

    /**
     * 删除成功
     */
    public AjaxResult delSuccess() {return AjaxResult.success("删除成功");}

    /**
     * 保存成功
     */
    public AjaxResult updateSuccess() {return AjaxResult.success("保存成功");}

    /**
     * 添加成功
     */
    public AjaxResult addSuccess() {return AjaxResult.success("添加成功");}

    /**
     * 保存成功
     */
    public AjaxResult saveSuccess() {return AjaxResult.success("保存成功");}

    /**
     * 返回失败消息
     */
    public AjaxResult error()
    {
        return AjaxResult.error();
    }

    /**
     * 返回成功消息
     */
    public AjaxResult success(String message)
    {
        return AjaxResult.success(message);
    }
    
    /**
     * 返回成功消息
     */
    public AjaxResult success(Object data)
    {
        return AjaxResult.success(data);
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error(String message)
    {
        return AjaxResult.error(message);
    }

    /**
     * 返回警告消息
     */
    public AjaxResult warn(String message)
    {
        return AjaxResult.warn(message);
    }

    /**
     * 响应返回结果
     * 
     * @param rows 影响行数
     * @return 操作结果
     */
    protected AjaxResult toAjax(int rows)
    {
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 响应返回结果
     * 
     * @param result 结果
     * @return 操作结果
     */
    protected AjaxResult toAjax(boolean result)
    {
        return result ? success() : error();
    }

    /**
     * 填充过滤器选项值
     * @author hj
     * @date 2023/7/17 15:18
     * @param rendering
     * @param list
     * @param filterName
     * @return com.alibaba.fastjson2.JSONObject
     */
    protected JSONObject fillFilterOptions(JSONObject rendering, List list, String filterName) {
        if(CollectionUtils.isEmpty(list)){
            return rendering;
        }
        try {
            JSONArray jsonArray = rendering.getJSONObject(FILTER_STR).getJSONArray(ITEMS_STR);
            for (int i=0; i<jsonArray.size(); i++){
                JSONObject filterJson = jsonArray.getJSONObject(i);
                if(filterJson.getString(NAME_STR).equals(filterName)){
                    JSONObject attrsObj = filterJson.getJSONObject(ATTRS_STR);
                    if(!MapUtil.isEmpty(attrsObj)){
                        attrsObj.put(OPTIONS_STR, list);
                    }
                }
            }
        }catch (Exception e) {
            logger.error("填充过滤器选项值异常", e);
        }
        return rendering;
    }

    protected JSONObject fillGraphOptions(JSONObject rendering, List list, String filterName) {
        if(CollectionUtils.isEmpty(list)){
            return rendering;
        }
        try {
            JSONArray jsonArray = rendering.getJSONObject(GRAPH_STR).getJSONArray(ITEMS_STR);
            for (int i=0; i<jsonArray.size(); i++){
                JSONObject filterJson = jsonArray.getJSONObject(i);
                String nameStr = filterJson.getString(NAME_STR);
                nameStr = StrUtil.isNotBlank(nameStr)?nameStr:"";
                if(nameStr.equals(filterName)){
                    JSONObject attrsObj = filterJson.getJSONObject(ATTRS_STR);
                    if(!MapUtil.isEmpty(attrsObj)){
                        attrsObj.put(OPTIONS_STR, list);
                    }
                }
            }
        }catch (Exception e) {
            logger.error("填充过滤器选项值异常", e);
        }
        return rendering;
    }

}
