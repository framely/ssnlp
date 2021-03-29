package edu.stanford.nlp.ling.tokensregex.demo;

import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.tokensregex.MultiPatternMatcher;
import edu.stanford.nlp.ling.tokensregex.SequenceMatchResult;
import edu.stanford.nlp.ling.tokensregex.TokenSequencePattern;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.util.CoreMap;

/**
 * @author Christopher Manning
 */
public class TokensRegexMatcherDemo {

  private TokensRegexMatcherDemo() {} // static main only

  public static void main(String[] args) {

    AnnotationPipeline pipeline = new AnnotationPipeline();
    final boolean verbose = false;
    pipeline.addAnnotator(new TokenizerAnnotator(verbose, "en"));
    pipeline.addAnnotator(new WordsToSentencesAnnotator(verbose));

    Annotation annotation = new Annotation("Casey is 21. Sally Atkinson's age is 30.");
    pipeline.annotate(annotation);
    {
      List<CoreLabel> tokens = annotation.get(CoreAnnotations.TokensAnnotation.class);
      for (int i = 0; i < tokens.size(); i++) {
        System.out.println(tokens.get(i).toString(CoreLabel.OutputFormat.VALUE_MAP));
        System.out.println(tokens.get(i).beginPosition() + ":" + tokens.get(i).endPosition());
      }
    }
    List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);

    System.out.println(annotation.toString());

    List<TokenSequencePattern> tokenSequencePatterns = new ArrayList<>();
    String[] patterns = {  "(?$who [ ner: PERSON]+ ) /is/ (?$age [ pos: CD ] )",
            "(?$who [ ner: PERSON]+ ) /'s/ /age/ /is/ (?$age [ pos: CD ] )" };
    for (String line : patterns) {
      TokenSequencePattern pattern = TokenSequencePattern.compile(line);
      tokenSequencePatterns.add(pattern);
    }
    MultiPatternMatcher<CoreMap> multiMatcher = TokenSequencePattern.getMultiPatternMatcher(tokenSequencePatterns);

    int i = 0;
    System.out.println(sentences.size());
    for (CoreMap sentence : sentences) {
      List<CoreLabel> tokens = sentence.get(CoreAnnotations.TokensAnnotation.class);
      System.out.println("Sentence #" + ++i);
      System.out.print("  Tokens:");
      for (CoreLabel token : tokens) {
        System.out.print(' ');
        System.out.print(token.toShortString("Text", "PartOfSpeech", "NamedEntityTag"));
      }
      System.out.println();

      List<SequenceMatchResult<CoreMap>> answers = multiMatcher.findNonOverlapping(tokens);
      int j = 0;
      for (SequenceMatchResult<CoreMap> matched : answers) {
        System.out.println("  Match #" + ++j);
        System.out.println("    match: " + matched.group(0));
        System.out.println("      who: " + matched.group("$who"));
        System.out.println("      age: " + matched.group("$age"));
      }
    }
  }

}
