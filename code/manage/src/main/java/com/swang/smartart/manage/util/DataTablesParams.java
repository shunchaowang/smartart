package com.swang.smartart.manage.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by swang on 3/30/2015.
 * DataTables Params Wrapper class.
 * Parse DataTables params based on HttpServletRequest.
 */
public class DataTablesParams {

    private String search;
    private String offset;
    private String max;
    private String order;
    private String orderDir;

    public DataTablesParams(HttpServletRequest request) {

        // parse sorting column
        String orderIndex = request.getParameter("order[0][column]");
        order = request.getParameter("columns[" + orderIndex + "][name]");

        // parse sorting direction and return as upper case
        orderDir = StringUtils.upperCase(request.getParameter("order[0][dir]"));

        // parse search keyword
        search = request.getParameter("search[value]");

        // parse pagination
        offset = request.getParameter("start");
        max = request.getParameter("length");
    }

    public String getSearch() {
        return search;
    }

    public String getOffset() {
        return offset;
    }

    public String getMax() {
        return max;
    }

    public String getOrder() {
        return order;
    }

    public String getOrderDir() {
        return orderDir;
    }
}
