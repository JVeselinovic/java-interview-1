package io.delfidx.sample.features;

import io.delfidx.sample.models.AccessionSample;
import io.delfidx.sample.models.Sample;
import io.delfidx.sample.repositories.SampleRepo;
import io.delfidx.subject.features.SubjectFeature;
import io.delfidx.subject.models.Subject;
import io.delfidx.subject.repositories.SubjectRepo;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;
import liquibase.pro.packaged.id;

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
    private final SubjectFeature subjectFeature;

    public SampleFeature(SampleRepo sampleRepo, SubjectRepo subjectRepo, SubjectFeature subjectFeature) {
        this.sampleRepo = sampleRepo;
        this.subjectRepo = subjectRepo;
        this.subjectFeature = subjectFeature;
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

             List<Subject> sampleList = subjectFeature.findSubjectByExternalId(accessionSample.getExternalSubjectId());
             long subjectId=0;   
             if(sampleList.size() == 0){

                Subject subject = new Subject();    
                subject.setModDt(Instant.now());
                subject.setModBy(accessionSample.getUser());
                subject.setCreateDt(Instant.now());
                subject.setCreateBy(accessionSample.getUser());
                subject.setExternalId(accessionSample.getExternalSubjectId());
                Subject saved = subjectRepo.save(subject);
                subjectId = saved.getId();
             }
             else{
                subjectId =  sampleList.get(0).getId();
             }
           
          
            Sample sample = new Sample();
            sample.setSampleTypeId(accessionSample.getSampleType());
            sample.setCollectionDate(accessionSample.getCollectionDate());
            sample.setCreateDt(Instant.now());
            sample.setCreateBy(accessionSample.getUser());
            sample.setModDt(Instant.now());
            sample.setModBy(accessionSample.getUser());
            sample.setSubjectId(subjectId);
            Sample savedSample = sampleRepo.save(sample);
            samples.add(savedSample); 
        
        }

        return samples;
    }
}
