package io.delfidx.subject.models;

import java.util.List;

public interface PageableSubject {
    int getTotalPages();
    int getPageNumber();
    long getOffset();
    int getNumberOfElements();
    int getSize();
    List<Subject> getContent();
}
