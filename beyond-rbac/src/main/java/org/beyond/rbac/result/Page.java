package org.beyond.rbac.result;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Page<T> implements Paged<T> {

    private int total;
    private int page;
    private int pageSize;
    private int pageCount;
    private List<T> data;

    protected Page(final int total, final int page, final int pageSize, final List<T> data) {
        this(total, page, pageSize, PageUtils.computePageCount(total, pageSize), data);
    }

    protected Page(final int total, final int page, final int pageSize, final int pageCount, final List<T> data) {
        this.total = total;
        this.page = page;
        this.pageSize = pageSize;
        this.pageCount = pageCount;
        this.data = data;
    }

    @Override
    public int getTotal() {
        return total;
    }

    public void setTotal(final int total) {
        this.total = total;
    }

    @Override
    public int getPage() {
        return page;
    }

    public void setPage(final int page) {
        this.page = page;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(final int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(final int pageCount) {
        this.pageCount = pageCount;
    }

    @Override
    public List<T> getData() {
        return data;
    }

    public void setData(final List<T> data) {
        this.data = data;
    }

    public static <T> Page<T> empty(final int page, final int pageSize) {
        return new Page<>(0, page, pageSize, new ArrayList<>());
    }

    public static <T> Page<T> make(final int total, final int page, final int pageSize, final List<T> data) {
        return new Page<>(total, page, pageSize, data);
    }

    public static <T> Page<T> from(final Paged<T> page) {
        return new Page<>(page.getTotal(), page.getPage(), page.getPageSize(), page.getPageCount(), page.getData());
    }

    public static <T, R> Page<R> map(final Paged<T> page, Function<T, R> mapper) {
        List<R> list = page.getData()
            .stream()
            .map(mapper)
            .collect(Collectors.toList());
        return new Page<>(page.getTotal(), page.getPage(), page.getPageSize(), page.getPageCount(), list);
    }

}
