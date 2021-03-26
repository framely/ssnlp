package edu.stanford.nlp.pipeline;

/**
 * Wrapper around a CoreMap representing a sentence.  Adds some helpful methods.
 *
 */

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;

import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.Pair;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.stream.Collectors;

public class CoreSentence {

  private final CoreDocument document;
  private final CoreMap sentenceCoreMap;
  private List<CoreEntityMention> entityMentions;

  /** common patterns to search for constituency parses **/

  /** cache to hold general patterns **/

  public CoreSentence(CoreDocument myDocument, CoreMap coreMapSentence) {
    this.document = myDocument;
    this.sentenceCoreMap = coreMapSentence;
  }

  /** create list of CoreEntityMention's based on the CoreMap's entity mentions **/
  public void wrapEntityMentions() {
    if (this.sentenceCoreMap.get(CoreAnnotations.MentionsAnnotation.class) != null) {
      entityMentions = this.sentenceCoreMap.get(CoreAnnotations.MentionsAnnotation.class).
          stream().map(coreMapEntityMention -> new CoreEntityMention(this,coreMapEntityMention)).collect(Collectors.toList());
    }
  }

  /** get the document this sentence is in **/
  public CoreDocument document() {
    return document;
  }

  /** get the underlying CoreMap if need be **/
  public CoreMap coreMap() {
    return sentenceCoreMap;
  }

  /** full text of the sentence **/
  public String text() {
    return sentenceCoreMap.get(CoreAnnotations.TextAnnotation.class);
  }

  /** char offsets of mention **/
  public Pair<Integer,Integer> charOffsets() {
    int beginCharOffset = this.sentenceCoreMap.get(CoreAnnotations.CharacterOffsetBeginAnnotation.class);
    int endCharOffset = this.sentenceCoreMap.get(CoreAnnotations.CharacterOffsetEndAnnotation.class);
    return new Pair<>(beginCharOffset,endCharOffset);
  }

  /** list of tokens **/
  public List<CoreLabel> tokens() {
    return sentenceCoreMap.get(CoreAnnotations.TokensAnnotation.class);
  }

  /** list of tokens as String **/
  public List<String> tokensAsStrings() {
    return tokens().stream().map(token -> token.word()).collect(Collectors.toList()); }

  /** list of pos tags **/
  public List<String> posTags() { return tokens().stream().map(token -> token.tag()).collect(Collectors.toList()); }

  /** list of lemma tags **/
  public List<String> lemmas() { return tokens().stream().map(token -> token.lemma()).collect(Collectors.toList()); }

  /** list of ner tags **/
  public List<String> nerTags() { return tokens().stream().map(token -> token.ner()).collect(Collectors.toList()); }


  /** list of entity mentions **/
  public List<CoreEntityMention> entityMentions() { return this.entityMentions; }


  @Override
  public String toString() {
    return coreMap().toString();
  }
}
