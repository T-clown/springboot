package com.springboot.common.entity;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageSerializable;

import java.util.Collection;
import java.util.List;

public class PageResult<T> extends PageSerializable<T> {
    private int pageNum;
    private int pageSize;

    public PageResult() {
    }

    public PageResult(List<T> list) {
        this(list, 8);
    }

    public PageResult(List<T> list, int navigatePages) {
        super(list);
        if (list instanceof com.github.pagehelper.Page) {
            com.github.pagehelper.Page page = (Page) list;
            this.pageNum = page.getPageNum();
            this.pageSize = page.getPageSize();
        } else if (list instanceof Collection) {
            this.pageNum = 1;
            this.pageSize = list.size();
        }

    }

    public static <T> PageResult<T> of(List<? extends T> list) {
        return new PageResult(list);
    }

    public static <T> PageResult<T> of(List<T> list, int navigatePages) {
        return new PageResult(list, navigatePages);
    }

    public static void copyPageInfo(PageResult source, PageResult dest) {
        dest.setTotal(source.getTotal());
        dest.setPageSize(source.getPageSize());
        dest.setPageNum(source.getPageNum());
    }

    public int getPageNum() {
        return this.pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("PageResult{");
        sb.append("pageNum=").append(this.pageNum);
        sb.append(", pageSize=").append(this.pageSize);
        sb.append(", total=").append(this.total);
        sb.append(", list=").append(this.list);
        sb.append('}');
        return sb.toString();
    }
}
