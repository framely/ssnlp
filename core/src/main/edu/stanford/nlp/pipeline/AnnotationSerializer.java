package edu.stanford.nlp.pipeline;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.util.Pair;

public abstract class AnnotationSerializer {
  /**
   * Append a single object to this stream. Subsequent calls to append on the same stream must supply the returned
   * output stream; furthermore, implementations of this function must be prepared to handle
   * the same output stream being passed in as it returned on the previous write.
   *
   * @param corpus  The document to serialize to the stream.
   * @param os The output stream to serialize to.
   * @return The output stream which should be closed when done writing, and which should be passed into subsequent
   *         calls to write() on this serializer.
   * @throws IOException Thrown if the underlying output stream throws the exception.
   */
  public abstract OutputStream write(Annotation corpus, OutputStream os) throws IOException;

  /**
   * Read a single object from this stream. Subsequent calls to read on the same input stream must supply the
   * returned input stream; furthermore, implementations of this function must be prepared to handle the same
   * input stream being passed to it as it returned on the previous read.
   *
   * @param is The input stream to read a document from.
   * @return A pair of the read document, and the implementation-specific input stream which it was actually read from.
   *         This stream should be passed to subsequent calls to read on the same stream, and should be closed when reading
   *         completes.
   * @throws IOException Thrown if the underlying stream throws the exception.
   * @throws ClassNotFoundException Thrown if an object was read that does not exist in the classpath.
   * @throws ClassCastException Thrown if the signature of a class changed in way that was incompatible with the serialized document.
   */
  public abstract Pair<Annotation, InputStream> read(InputStream is) throws IOException, ClassNotFoundException, ClassCastException;

  /**
   * Append a CoreDocument to this output stream.
   *
   * @param document The CoreDocument to serialize (its internal annotation is serialized)
   * @param os The output stream to serialize to
   * @return The output stream which should be closed
   * @throws IOException
   */
  public OutputStream writeCoreDocument(CoreDocument document, OutputStream os) throws IOException {
    Annotation wrappedAnnotation = document.annotation();
    return write(wrappedAnnotation, os);
  }

  /**
   * Read in a CoreDocument from this input stream.
   *
   * @param is The input stream to read a CoreDocument's annotation from
   * @return A pair with the CoreDocument and the input stream
   * @throws IOException
   * @throws ClassNotFoundException
   * @throws ClassCastException
   */

  public Pair<CoreDocument, InputStream> readCoreDocument(InputStream is)
      throws IOException, ClassNotFoundException, ClassCastException {
    Pair<Annotation, InputStream> readPair = read(is);
    CoreDocument readCoreDocument = new CoreDocument(readPair.first());
    return new Pair<CoreDocument, InputStream>(readCoreDocument, is);
  }


  public static class IntermediateNode {
    String docId;
    int sentIndex;
    int index;
    int copyAnnotation;
    boolean isRoot;
    public IntermediateNode(String docId, int sentIndex, int index, int copy, boolean isRoot) {
      this.docId = docId;
      this.sentIndex = sentIndex;
      this.index = index;
      this.copyAnnotation = copy;
      this.isRoot = isRoot;
    }
  }

  public static class IntermediateEdge {
    int source;
    int sourceCopy;
    int target;
    int targetCopy;
    String dep;
    boolean isExtra;
    public IntermediateEdge(String dep, int source, int sourceCopy, int target, int targetCopy, boolean isExtra) {
      this.dep = dep;
      this.source = source;
      this.sourceCopy = sourceCopy;
      this.target = target;
      this.targetCopy = targetCopy;
      this.isExtra = isExtra;
    }
  }

  public static class IntermediateSemanticGraph {
    public List<IntermediateNode> nodes;
    public List<IntermediateEdge> edges;
    public IntermediateSemanticGraph() {
      nodes = new ArrayList<>();
      edges = new ArrayList<>();
    }

    public IntermediateSemanticGraph(List<IntermediateNode> nodes, List<IntermediateEdge> edges) {
      this.nodes = new ArrayList<>(nodes);
      this.edges = new ArrayList<>(edges);
    }

    private static final Object LOCK = new Object();
  }

}
