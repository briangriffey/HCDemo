package com.briangriffey.extraction.exception;

import com.briangriffey.extraction.DataFeatureExtractor;
import com.briangriffey.extraction.Extraction;
import com.briangriffey.extraction.ExtractionVisitor;

/**
 * An extraction that represents an error being thrown during the extraction process. This implements the
 * {@link Extraction Extraction} interface so that we can possibly use it with the onErrorResumeNext operator, or so
 * any visitor can visit this object
 */
public class ErrorExtraction implements Extraction<Class<? extends DataFeatureExtractor>, Throwable>{
    private final Class<? extends DataFeatureExtractor> clazz;
    private final Throwable e;

    public ErrorExtraction(Class<? extends DataFeatureExtractor> clazz, Throwable e) {
        this.clazz = clazz;
        this.e = e;
    }

    @Override
    public Class<? extends DataFeatureExtractor> getSource() {
        return clazz;
    }

    @Override
    public Throwable getExtraction() {
        return e;
    }

    @Override
    public void acceptVisitor(ExtractionVisitor visitor) {
        visitor.visitExtraction(this);
    }
}
