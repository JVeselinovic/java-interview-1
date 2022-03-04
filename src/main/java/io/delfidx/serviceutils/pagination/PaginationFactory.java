package io.delfidx.serviceutils.pagination;

import io.micronaut.context.annotation.EachBean;
import io.micronaut.context.annotation.Factory;

@Factory
public class PaginationFactory {
    @EachBean(PaginationConfiguration.class)
    public Pagination pagination(PaginationConfiguration paginationConfiguration) {
        if(paginationConfiguration==null) {
            return new PaginationImpl(new PaginationConfiguration("default"));
        }
        return new PaginationImpl(paginationConfiguration);
    }
}
