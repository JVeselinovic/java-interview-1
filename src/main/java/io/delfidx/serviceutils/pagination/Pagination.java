package io.delfidx.serviceutils.pagination;

import io.micronaut.data.model.Pageable;

public interface Pagination {
    Pageable defaultPaged();
}
