package io.delfidx.serviceutils.pagination;

import io.micronaut.data.model.Pageable;

public class PaginationImpl implements Pagination {
    private final Pageable defaultPageable;
    public PaginationImpl(PaginationConfiguration paginationConfiguration) {
        defaultPageable = new Pageable() {
            @Override
            public int getNumber() {
                return 1;
            }

            @Override
            public int getSize() {
                return paginationConfiguration.getDefaultPageSize();
            }
        };
    }

    @Override
    public Pageable defaultPaged() {
        return defaultPageable;
    }
}
