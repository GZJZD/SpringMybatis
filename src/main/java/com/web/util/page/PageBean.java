package com.web.util.page;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageBean<T> {

    public static final int DEFAULT_PAGE_SIZE = 50; //默认每页条数
    /**
     * 当前页
     */
    private  Integer indexPage;
    /**
     * 每页数据条数
     */
    private Integer pageSize;
    /**
     *总页数
     */
    private Integer totalPage;
    /**
     * 数据总条数
     */
    private Integer totalNumber;

    /**
     * 当前页记录List形式
     */
    protected List<T> results; // 当前页记录List形式
    /**
     * 设置页面查询参数
     */
    public Map<String, Object> params = new HashMap<String, Object>();//
    public PageBean byPage(Integer pageSize ){
        PageBean pageBean = new PageBean();
        if(pageSize  != null && !pageSize.equals("")){
            pageBean.pageSize = pageSize;
        }

        return pageBean;
    }
}
