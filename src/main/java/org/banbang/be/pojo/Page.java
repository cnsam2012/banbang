package org.banbang.be.pojo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.junit.jupiter.api.Test;

/**
 * 封装分页相关的信息
 */
@Slf4j
@ApiModel("分页相关信息")
public class Page {

    @ApiModelProperty(value = "当前页码", example = "1")
    private int current = 1;

    @ApiModelProperty(value = "单页显示的帖子数量上限", example = "10")
    private int limit = 10;

    @ApiModelProperty(value = "帖子总数（用于计算总页数）", example = "3", hidden = true)
    private int rows;

    @ApiModelProperty(value = "查询路径（复用分页链接, 其他界面也可以有分页）", example = "/discuss/detail/3", hidden = true)
    private String path;

    @ApiModelProperty(value = "当前页的起始索引 offset", example = "0", hidden = true)
    private int offset;

    @ApiModelProperty(value = "总页数", example = "0", hidden = true)
    private int total;

    @ApiModelProperty(value = "获取分页栏起始页码，分页栏显示当前页码及其前后两页", example = "1", hidden = true)
    private int from;

    @ApiModelProperty(value = "获取分页栏结束页码", example = "0", hidden = true)
    private int to;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if (current >= 1) {
            this.current = current;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (current >= 1 && limit <= 100) {
            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if (rows >= 0) {
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取当前页的起始索引 offset
     *
     * @return
     */
    public int getOffset() {
        return current * limit - limit;
    }

    /**
     * 获取总页数
     *
     * @return
     */
    public int getTotal() {
        if (rows % limit == 0) {
            return rows / limit;
        } else {
            return rows / limit + 1;
        }
    }

    /**
     * 获取分页栏起始页码
     * 分页栏显示当前页码及其前后两页
     *
     * @return
     */
    public int getFrom() {
        int from = current - 2;
        return from < 1 ? 1 : from;
    }

    /**
     * 获取分页栏结束页码
     *
     * @return
     */
    public int getTo() {
        int to = current + 2;
        int total = getTotal();
        return to > total ? total : to;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public void setTo(int to) {
        this.to = to;
    }

    @Test
    void pagePojoTest() {
        var p = new Page();
        System.out.println(p.getOffset());
        System.out.println(p.getTotal());
        System.out.println(p.getFrom());
        System.out.println(p.getTo());
    }
}
