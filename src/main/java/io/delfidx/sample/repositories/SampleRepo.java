package io.delfidx.sample.repositories;

import io.delfidx.sample.models.Sample;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.repository.CrudRepository;

import javax.persistence.EntityManager;

@Repository
public abstract class SampleRepo implements CrudRepository<Sample, Long> {

    private final EntityManager entityManager;

    public SampleRepo(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public abstract Page<Sample> list(Pageable pageable);

}
