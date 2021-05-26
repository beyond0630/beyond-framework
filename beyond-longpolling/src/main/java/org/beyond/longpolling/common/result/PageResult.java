package org.beyond.longpolling.common.result;

import java.util.List;

public class PageResult<T> implements Paged<T>, IResult<List<T>> {

    private static final int CODE_OK = 0;

    private int code;
    private String message;
    private List<T> data;
    private int total;
    private int page;
    private int pageSize;
    private int pageCount;

    protected PageResult(final int total, final int page, final int pageSize, final List<T> data) {
        this(total, page, pageSize, PageUtils.computePageCount(total, pageSize), data);
    }

    protected PageResult(final int total, final int page, final int pageSize, final int pageCount, final List<T> data) {
        this.code = CODE_OK;
        this.message = null;
        this.data = data;
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
        this.pageCount = pageCount;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public List<T> getData() {
        return data;
    }

    @Override
    public int getTotal() {
        return this.total;
    }

    @Override
    public int getPage() {
        return this.page;
    }

    @Override
    public int getPageSize() {
        return this.pageSize;
    }

    @Override
    public int getPageCount() {
        return this.pageCount;
    }

    public static <T> PageResult<T> make(final int total, final int page, final int pageSize, final List<T> data) {
        return new PageResult<>(total, page, pageSize, data);
    }

    public static <T> PageResult<T> make(final long total, final int page, final int pageSize, final List<T> data) {
        return new PageResult<>((int) total, page, pageSize, data);
    }

    public static <T> PageResult<T> from(final Paged<T> page) {
        return new PageResult<>(page.getTotal(), page.getPage(), page.getPageSize(), page.getPageCount(), page.getData());
    }

}
