package io.delfidx.serviceutils.pagination;

import io.micronaut.context.annotation.EachProperty;
import io.micronaut.context.annotation.Parameter;

@EachProperty("service-utils.pagination")
public class PaginationConfiguration {
    private final String name;
    private int defaultPageSize = 100;
    private int maxAllowedPageSize = 1000;

    public PaginationConfiguration(@Parameter String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getDefaultPageSize() {
        return defaultPageSize;
    }

    public void setDefaultPageSize(int defaultPageSize) {
        this.defaultPageSize = defaultPageSize;
    }

    public int getMaxAllowedPageSize() {
        return maxAllowedPageSize;
    }

    public void setMaxAllowedPageSize(int maxAllowedPageSize) {
        this.maxAllowedPageSize = maxAllowedPageSize;
    }
}
