package io.delfidx.sample.models;

import java.time.Instant;

public record AccessionSample(
        String sampleType,
        String externalSubjectId,
        Instant collectionDate,
        String user
) {
}
