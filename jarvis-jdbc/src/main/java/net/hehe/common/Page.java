package net.hehe.common;

public class Page {

    private int pageNum = 1;// 页码
    private int pageSize = 10;// 每页显示记录数

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}

