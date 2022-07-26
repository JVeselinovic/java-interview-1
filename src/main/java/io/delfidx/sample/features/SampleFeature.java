package io.delfidx.sample.features;

import io.delfidx.sample.models.AccessionSample;
import io.delfidx.sample.models.Sample;
import io.delfidx.sample.repositories.SampleRepo;
import io.delfidx.subject.models.Subject;
import io.delfidx.subject.repositories.SubjectRepo;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public class SampleFeature {
    public static final Logger log = LoggerFactory.getLogger(SampleFeature.class);
    private final SampleRepo sampleRepo;
    private final SubjectRepo subjectRepo;

    public SampleFeature(SampleRepo sampleRepo, SubjectRepo subjectRepo) {
        this.sampleRepo = sampleRepo;
        this.subjectRepo = subjectRepo;
    }

    public Page<Sample> getSamples(@NotNull Pageable pageable) {
        return sampleRepo.list(pageable);
    }

    public Optional<Sample> findById(@NotNull Long id) {
        return sampleRepo.findById(id);
    }

    public List<Sample> accessionSamples(List<AccessionSample> accessionSamples) {
        List<Sample> samples = new ArrayList<>();
        for(AccessionSample accessionSample: accessionSamples) {
            Subject subject = new Subject();
            subject.setModDt(Instant.now());
            subject.setModBy(accessionSample.getUser());
            subject.setCreateDt(Instant.now());
            subject.setCreateBy(accessionSample.getUser());
            Subject saved = subjectRepo.save(subject);

            Sample sample = new Sample();
            sample.setSampleTypeId(accessionSample.getSampleType());
            sample.setCollectionDate(accessionSample.getCollectionDate());
            sample.setCreateDt(Instant.now());
            sample.setCreateBy(accessionSample.getUser());
            sample.setModDt(Instant.now());
            sample.setModBy(accessionSample.getUser());
            sample.setSubjectId(saved.getId());
            Sample savedSample = sampleRepo.save(sample);
            samples.add(savedSample);
        }
        return samples;
    }
}
