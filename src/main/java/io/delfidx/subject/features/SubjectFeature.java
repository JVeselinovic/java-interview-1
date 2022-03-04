package io.delfidx.subject.features;

import io.delfidx.subject.models.Subject;
import io.delfidx.subject.repositories.SubjectRepo;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Singleton
public class SubjectFeature {
    public static final Logger log = LoggerFactory.getLogger(SubjectFeature.class);
    private final SubjectRepo subjectRepo;

    public SubjectFeature(SubjectRepo subjectRepo) {
        this.subjectRepo = subjectRepo;
    }

    public Page<Subject> getSubjects(@NotNull Pageable pageable) {
        return subjectRepo.list(pageable);
    }

    public Optional<Subject> findSubjectById(@NotNull Long id){
        return subjectRepo.findById(id);
    }

    public List<Subject> findSubjectByExternalId(String id) {
        return subjectRepo.findByExternalId(id);
    }
}
