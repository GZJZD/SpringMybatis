package com.web.util.query;



import java.util.Collections;
import java.util.List;


public class PageResult {
    private int currentPage;
    private int pageSize;
    private int totalCount;
    private List<?> data;
    private int totalPage;
    private int nextPage;
    private int prevPage;

    public PageResult(int currentPage, int pageSize, int totalCount, List<?> data) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.data = data;

        totalPage=totalCount%pageSize==0?totalCount/pageSize:totalCount/pageSize+1;
        nextPage=currentPage+1<totalPage?currentPage+1:totalPage;
        prevPage=currentPage-1>1?currentPage-1:1;
    }

    public PageResult(int pageSize) {
       this(1,pageSize,0,Collections.EMPTY_LIST);
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public int getPrevPage() {
        return prevPage;
    }

    public void setPrevPage(int prevPage) {
        this.prevPage = prevPage;
    }
}
