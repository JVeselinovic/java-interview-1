package io.delfidx.sample.models;

import java.time.Instant;

import lombok.*;
import java.time.Instant;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class AccessionSample {
    private String sampleType;
    private String externalSubjectId;
    private Instant collectionDate;
    private String user;
}