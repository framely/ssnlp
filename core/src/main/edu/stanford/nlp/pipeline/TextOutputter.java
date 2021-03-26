package edu.stanford.nlp.pipeline;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;
import java.util.Map;



import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;

import edu.stanford.nlp.util.CoreMap;

/**
 * @author John Bauer
 */
public class TextOutputter extends AnnotationOutputter {

  public TextOutputter() {}

  /** {@inheritDoc} */
  @Override
  public void print(Annotation annotation, OutputStream stream, Options options) throws IOException {
    PrintWriter os = new PrintWriter(IOUtils.encodedOutputStreamWriter(stream, options.encoding));
    print(annotation, os, options);
  }

  /**
   * The meat of the outputter.
   */
  private static void print(Annotation annotation, PrintWriter pw, Options options) {
    double beam = options.relationsBeam;

    List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);

    // Display docid if available
    String docId =  annotation.get(CoreAnnotations.DocIDAnnotation.class);
    if (docId != null) {
      List<CoreLabel> tokens = annotation.get(CoreAnnotations.TokensAnnotation.class);
      int nSentences = (sentences != null)? sentences.size():0;
      int nTokens = (tokens != null)? tokens.size():0;
      pw.printf("Document: ID=%s (%d sentences, %d tokens)%n", docId, nSentences, nTokens);
    }

    // Display doctitle if available
    String docTitle =  annotation.get(CoreAnnotations.DocTitleAnnotation.class);
    if (docTitle != null) {
      pw.printf("Document Title: %s%n", docTitle);
    }

    // Display docdate if available
    String docDate =  annotation.get(CoreAnnotations.DocDateAnnotation.class);
    if (docDate != null) {
      pw.printf("Document Date: %s%n", docDate);
    }

    // Display doctype if available
    String docType =  annotation.get(CoreAnnotations.DocTypeAnnotation.class);
    if (docType != null) {
      pw.printf("Document Type: %s%n", docType);
    }

    // Display docsourcetype if available
    String docSourceType =  annotation.get(CoreAnnotations.DocSourceTypeAnnotation.class);
    if (docSourceType != null) {
      pw.printf("Document Source Type: %s%n", docSourceType);
    }

    // display each sentence in this annotation
    if (sentences != null) {
      for (int i = 0, sz = sentences.size(); i < sz; i ++) {
        pw.println();
        CoreMap sentence = sentences.get(i);
        List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
        String piece = "";
        pw.printf("Sentence #%d (%d tokens%s):%n", (i + 1), tokens.size(), piece);

        String text = sentence.get(CoreAnnotations.TextAnnotation.class);
        pw.println(text);

        // display the token-level annotations
        String[] tokenAnnotations = {
                "Text", "PartOfSpeech", "Lemma", "Answer", "NamedEntityTag",
                "CharacterOffsetBegin", "CharacterOffsetEnd", "NormalizedNamedEntityTag",
                "CodepointOffsetBegin", "CodepointOffsetEnd",
                "Timex", "TrueCase", "TrueCaseText", "SentimentClass", "WikipediaEntity" };

        pw.println();
        pw.println("Tokens:");
        for (CoreLabel token: tokens) {
          pw.print(token.toShorterString(tokenAnnotations));
          pw.println();
        }

        // display the entity mentions
        List<CoreMap> entityMentions = sentence.get(CoreAnnotations.MentionsAnnotation.class);
        if (entityMentions != null) {
          pw.println();
          pw.println("Extracted the following NER entity mentions:");
          for (CoreMap entityMention : entityMentions) {
            String nerConfidenceEntry;
            Map<String,Double> nerConfidences = entityMention.get(CoreAnnotations.NamedEntityTagProbsAnnotation.class);
            String nerConfidenceKey =
                    nerConfidences.keySet().size() > 0 ? (String) nerConfidences.keySet().toArray()[0] : "" ;
            if (!nerConfidenceKey.equals("") && !nerConfidenceKey.equals("O"))
              nerConfidenceEntry = nerConfidenceKey + ":" + nerConfidences.get(nerConfidenceKey);
            else
              nerConfidenceEntry = "-";
            if (entityMention.get(CoreAnnotations.EntityTypeAnnotation.class) != null) {
              pw.println(entityMention.get(CoreAnnotations.TextAnnotation.class) + '\t'
                  + entityMention.get(CoreAnnotations.EntityTypeAnnotation.class) + '\t'
                      + nerConfidenceEntry);
            }
          }
        }

      }
    } else {
      List<CoreLabel> tokens = annotation.get(CoreAnnotations.TokensAnnotation.class);
      pw.println("Tokens:");
      pw.println(annotation.get(CoreAnnotations.TextAnnotation.class));
      for (CoreLabel token : tokens) {
        int tokenCharBegin = token.get(CoreAnnotations.CharacterOffsetBeginAnnotation.class);
        int tokenCharEnd = token.get(CoreAnnotations.CharacterOffsetEndAnnotation.class);
        String extra = "";
        Integer codepoint = token.get(CoreAnnotations.CodepointOffsetBeginAnnotation.class);
        if (codepoint != null) {
          extra = extra + " CodepointOffsetBegin=" + codepoint;
        }
        codepoint = token.get(CoreAnnotations.CodepointOffsetEndAnnotation.class);
        if (codepoint != null) {
          extra = extra + " CodepointOffsetEnd=" + codepoint;
        }
        pw.println("[Text="+token.word()+" CharacterOffsetBegin="+tokenCharBegin+" CharacterOffsetEnd="+tokenCharEnd+extra+']');
      }
    }

    // display the old-style doc-level coref annotations
    // this is not supported anymore!
    //String corefAnno = annotation.get(CorefPLAnnotation.class);
    //if(corefAnno != null) os.println(corefAnno);


    // display quotes if available
    if (annotation.get(CoreAnnotations.QuotationsAnnotation.class) != null) {
      pw.println();
      pw.println("Extracted quotes:");
      List<CoreMap> allQuotes = QuoteAnnotator.gatherQuotes(annotation);
      for (CoreMap quote : allQuotes) {
        String speakerString = "unknown";
        pw.printf("%s:\t%s\t[index=%d, charOffsetBegin=%d]%n",
                speakerString,
                quote.get(CoreAnnotations.TextAnnotation.class),
                quote.get(CoreAnnotations.QuotationIndexAnnotation.class),
                quote.get(CoreAnnotations.CharacterOffsetBeginAnnotation.class)
        );
      }
    }

    pw.flush();
  }

  /** Static helper */
  public static void prettyPrint(Annotation annotation, OutputStream stream, StanfordCoreNLP pipeline) {
    prettyPrint(annotation, new PrintWriter(stream), pipeline);
  }

  /** Static helper */
  public static void prettyPrint(Annotation annotation, PrintWriter pw, StanfordCoreNLP pipeline) {
    TextOutputter.print(annotation, pw, getOptions(pipeline.getProperties()));
    // already flushed
    // don't close, might not want to close underlying stream
  }

}
