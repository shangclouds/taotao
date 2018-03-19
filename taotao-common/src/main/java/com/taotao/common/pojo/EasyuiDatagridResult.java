package com.taotao.common.pojo;

import java.io.Serializable;
import java.util.List;

public class EasyuiDatagridResult implements Serializable {
    private Long total;//总数
    private List<?> rows;//列表

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<?> getRows() {
        return rows;
    }

    public void setRows(List<?> rows) {
        this.rows = rows;
    }
}
