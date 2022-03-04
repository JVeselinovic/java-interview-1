package io.delfidx.subject.models;


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
public class Subject {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String externalId;
    private Instant createDt;
    private String createBy;
    private Instant modDt;
    private String modBy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Subject subject = (Subject) o;
        return id != null && Objects.equals(id, subject.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
