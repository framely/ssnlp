package edu.stanford.nlp.pipeline;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * Creates a annotation from an input source
 *
 * @author Angel Chang
 */
public interface AnnotationCreator {
  Annotation createFromText(String text) throws IOException;

  Annotation createFromFile(String filename) throws IOException;
  Annotation createFromFile(File file) throws IOException;

  Annotation create(InputStream stream) throws IOException;
  Annotation create(InputStream stream, java.lang.String encoding) throws IOException;
  Annotation create(Reader reader)  throws IOException;
}
