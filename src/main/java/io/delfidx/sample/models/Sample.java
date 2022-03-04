package io.delfidx.sample.models;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table
public class Sample {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sampleTypeId;
    private Instant collectionDate;
    //create/modify
    private Instant createDt;
    private String createBy;
    private Instant modDt;
    private String modBy;
    private Long subjectId;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Sample sample = (Sample) o;
        return id!=null && Objects.equals(id, sample.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
