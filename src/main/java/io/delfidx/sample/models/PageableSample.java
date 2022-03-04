package io.delfidx.sample.models;

import java.util.List;

public interface PageableSample {
    int getTotalPages();
    int getPageNumber();
    long getOffset();
    int getNumberOfElements();
    int getSize();
    List<Sample> getContent();
}