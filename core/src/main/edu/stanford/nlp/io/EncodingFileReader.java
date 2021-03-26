package edu.stanford.nlp.io;

import java.io.*;

/**
 * This is a convenience class which works almost exactly like 
 * <code>FileReader</code>
 * but allows for the specification of input encoding.
 *
 * @author	Alex Kleeman
 */

public class EncodingFileReader extends InputStreamReader {

  private static final String DEFAULT_ENCODING = "UTF-8";

  /**
   * Creates a new <tt>EncodingFileReader</tt>, given the name of the
   * file to read from and an encoding
   *
   * @param fileName the name of the file to read from
   * @param encoding <tt>String</tt> specifying the encoding to be used
   * @throws java.io.UnsupportedEncodingException if the encoding does not exist.
   * @throws java.io.FileNotFoundException        if the named file does not exist,
   *                                              is a directory rather than a regular file,
   *                                              or for some other reason cannot be opened for
   *                                              reading.
   */
  public EncodingFileReader(String fileName, String encoding) throws UnsupportedEncodingException, FileNotFoundException {
    super(new FileInputStream(fileName),
            encoding == null ? DEFAULT_ENCODING : encoding);
  }
}
