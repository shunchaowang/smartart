package com.swang.smartart.manage.web.vo.table;

import java.io.Serializable;
import java.util.List;

/**
 * Created by swang on 3/15/2015.
 */
public class DataTablesResultSet<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<T> data;
    private Integer recordsTotal;
    private Integer recordsFiltered;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Integer getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(Integer recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public Integer getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(Integer recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }
}
