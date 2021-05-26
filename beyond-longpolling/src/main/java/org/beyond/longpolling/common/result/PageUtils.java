package org.beyond.longpolling.common.result;

public final class PageUtils {

    private PageUtils() {
        throw new UnsupportedOperationException();
    }

    private static final int MIN_PAGE = 1;
    private static final int MAX_PAGE = 1000;
    private static final int MIN_PAGE_SIZE = 1;
    private static final int MAX_PAGE_SIZE = 1000;

    /**
     * 规范化页码，使页码的值保持在 1 - 1000 之间
     *
     * @param page 页码
     * @return 规范化后的页码
     */
    public static int normalizePage(final int page) {
        if (page < MIN_PAGE) {
            return MIN_PAGE;
        }
        return Math.min(page, MAX_PAGE);
    }

    /**
     * 规范化每页项目数，使每页项目数的值保持在 1 - 1000 之间
     *
     * @param pageSize 每页项目数
     * @return 规范化后的每页项目数
     */
    public static int normalizePageSize(final int pageSize) {
        if (pageSize < MIN_PAGE_SIZE) {
            return MIN_PAGE_SIZE;
        }
        return Math.min(pageSize, MAX_PAGE_SIZE);
    }

    /**
     * 计算总页数
     *
     * @param total    总项目数
     * @param pageSize 每页项目数
     * @return 总页数
     */
    public static int computePageCount(final int total, final int pageSize) {
        return (total < 1 || pageSize < 1) ? 0 : (int) Math.ceil((double) total / (double) pageSize);
    }

    /**
     * 计算跳过的项目数
     *
     * @param page     页码
     * @param pageSize 每页项目数
     * @return 跳过的项目数
     */
    public static int computeOffset(final int page, final int pageSize) {
        if (page < 1 || pageSize < 1) {
            return 0;
        }
        return (page - 1) * pageSize;
    }

}
