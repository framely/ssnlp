package edu.stanford.nlp.pipeline;

import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.Pair;

import java.util.*;

/**
 * Wrapper around a CoreMap representing a quote.  Adds some helpful methods.
 *
 * @author Jason Bolton
 */

public class CoreQuote {

  private final CoreMap quoteCoreMap;
  private final CoreDocument document;
  private final List<CoreSentence> sentences;
  // optional speaker info...note there may not be an entity mention corresponding to the speaker
  public boolean hasSpeaker;
  public boolean hasCanonicalSpeaker;
  private Optional<String> speaker;
  private Optional<String> canonicalSpeaker;
  private final Optional<List<CoreLabel>> speakerTokens;
  private final Optional<List<CoreLabel>> canonicalSpeakerTokens;
  private final Optional<Pair<Integer,Integer>> speakerCharOffsets;
  private final Optional<Pair<Integer,Integer>> canonicalSpeakerCharOffsets;
  private final Optional<CoreEntityMention> speakerEntityMention;
  private final Optional<CoreEntityMention> canonicalSpeakerEntityMention;

  public CoreQuote(CoreDocument myDocument, CoreMap coreMapQuote) {
    this.document = myDocument;
    this.quoteCoreMap = coreMapQuote;
    // attach sentences to the quote
    this.sentences = new ArrayList<>();
    int firstSentenceIndex = this.quoteCoreMap.get(CoreAnnotations.SentenceBeginAnnotation.class);
    int lastSentenceIndex = this.quoteCoreMap.get(CoreAnnotations.SentenceEndAnnotation.class);
    for (int currSentIndex = firstSentenceIndex ; currSentIndex <= lastSentenceIndex ; currSentIndex++) {
      this.sentences.add(this.document.sentences().get(currSentIndex));
    }
    // set up the speaker info

    // set up info for direct speaker mention (example: "He")

    this.speakerTokens = Optional.empty();
    this.speakerCharOffsets = Optional.empty();
    this.speakerEntityMention = Optional.empty();
    // set up info for canonical speaker mention (example: "Joe Smith")

    this.canonicalSpeakerTokens = Optional.empty();
    this.canonicalSpeakerCharOffsets = Optional.empty();
    this.canonicalSpeakerEntityMention = Optional.empty();
    // record if there is speaker info
    this.hasSpeaker = this.speaker.isPresent();
    this.hasCanonicalSpeaker = this.canonicalSpeaker.isPresent();
  }

  /** get the underlying CoreMap if need be **/
  public CoreMap coreMap() {
    return quoteCoreMap;
  }

  /** get this quote's document **/
  public CoreDocument document() {
    return document;
  }

  /** full text of the mention **/
  public String text() {
    return this.quoteCoreMap.get(CoreAnnotations.TextAnnotation.class);
  }

  /** retrieve the CoreSentence's attached to this quote **/
  public List<CoreSentence> sentences() {
    return this.sentences;
  }

  /** retrieve the text of the speaker **/
  public Optional<String> speaker() {
    return this.speaker;
  }

  /** retrieve the text of the canonical speaker **/
  public Optional<String> canonicalSpeaker() { return this.canonicalSpeaker; }

  /** retrieve the tokens of the speaker **/
  public Optional<List<CoreLabel>> speakerTokens() {
    return this.speakerTokens;
  }

  /** retrieve the character offsets of the speaker **/
  public Optional<Pair<Integer,Integer>> speakerCharOffsets() {
    return this.speakerCharOffsets;
  }

  /** retrieve the entity mention corresponding to the speaker if there is one **/
  public Optional<CoreEntityMention> speakerEntityMention() {
    return this.speakerEntityMention;
  }

  /** retrieve the tokens of the canonical speaker **/
  public Optional<List<CoreLabel>> canonicalSpeakerTokens() {
    return this.canonicalSpeakerTokens;
  }

  /** retrieve the character offsets of the canonical speaker **/
  public Optional<Pair<Integer,Integer>> canonicalSpeakerCharOffsets() {
    return this.canonicalSpeakerCharOffsets;
  }

  /** retrieve the entity mention corresponding to the canonical speaker if there is one **/
  public Optional<CoreEntityMention> canonicalSpeakerEntityMention() {
    return this.canonicalSpeakerEntityMention;
  }

  /** char offsets of quote **/
  public Pair<Integer,Integer> quoteCharOffsets() {
    int beginCharOffset = this.quoteCoreMap.get(CoreAnnotations.CharacterOffsetBeginAnnotation.class);
    int endCharOffset = this.quoteCoreMap.get(CoreAnnotations.CharacterOffsetEndAnnotation.class);
    return new Pair<>(beginCharOffset,endCharOffset);
  }

  @Override
  public String toString() {
    return coreMap().toString();
  }
}
