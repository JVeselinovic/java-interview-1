package io.delfidx.subject.repositories;

import io.delfidx.subject.models.Subject;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface SubjectRepo extends CrudRepository<Subject, Long> {
    Page<Subject> list(Pageable pageable);
    List<Subject> findByExternalId(String id);
}
