package edu.stanford.nlp.pipeline;

import edu.stanford.nlp.io.RuntimeIOException;
import edu.stanford.nlp.util.MetaClass;
import edu.stanford.nlp.util.PropertiesUtils;
import edu.stanford.nlp.util.logging.Redwood;

import java.io.IOException;
import java.util.*;

/**
 * A class abstracting the implementation of various annotators.
 * Importantly, subclasses of this class can overwrite the implementation
 * of these annotators by returning a different annotator, and
 * {@link edu.stanford.nlp.pipeline.StanfordCoreNLP} will automatically load
 * the new annotator instead.
 *
 * @author Gabor Angeli
 */
public class AnnotatorImplementations  {

  /** A logger for this class */
  private static final Redwood.RedwoodChannels log = Redwood.channels(AnnotatorImplementations.class);

  /**
   * Tokenize, emulating the Penn Treebank
   */
  public Annotator tokenizer(Properties properties) {
    return new TokenizerAnnotator(properties);
  }

  /**
   * Clean XML input
   */
  public CleanXmlAnnotator cleanXML(Properties properties) {
    return new CleanXmlAnnotator(properties);
  }

  /**
   * Sentence split, in addition to a bunch of other things in this annotator (be careful to check the implementation!)
   */
  public Annotator wordToSentences(Properties properties) {
    return new WordsToSentencesAnnotator(properties);
  }

  /**
   * Multi-word-token, split tokens into words (e.g. "des" in French into "de" and "les")
   */
  public Annotator multiWordToken(Properties props) {
    // MWTAnnotator defaults to using "mwt." as prefix
    return new MWTAnnotator("", props);
  }

  /**
   * Set document date
   */
  public Annotator docDate(Properties properties) {
    return new DocDateAnnotator("docdate", properties);
  }

  /**
   * Annotate lemmas
   */
  public Annotator morpha(Properties properties, boolean verbose) {
    return new MorphaAnnotator(verbose);
  }



  /**
   * Run TokensRegex -- annotate patterns found in tokens
   */
  public Annotator tokensregex(Properties properties, String name) {
    return new TokensRegexAnnotator(name, properties);
  }

  /**
   * Run RegexNER -- rule-based NER based on a deterministic mapping file
   */
  public Annotator tokensRegexNER(Properties properties, String name) {
    return new TokensRegexNERAnnotator(name, properties);
  }



  /**
   * Annotate for gender of tokens
   */
  public Annotator gender(Properties properties, String name) {
    return new GenderAnnotator(name, properties);
  }



  public Annotator custom(Properties properties, String property) {
    String customName = property;
    String customClassName = properties.getProperty(StanfordCoreNLP.CUSTOM_ANNOTATOR_PREFIX + property);
    if (property.startsWith(StanfordCoreNLP.CUSTOM_ANNOTATOR_PREFIX)) {
      customName = property.substring(StanfordCoreNLP.CUSTOM_ANNOTATOR_PREFIX.length());
      customClassName = properties.getProperty(property);
    }

    try {
      // name + properties
      return new MetaClass(customClassName).createInstance(customName, properties);
    } catch (MetaClass.ConstructorNotFoundException e) {
      try {
        // name
        return new MetaClass(customClassName).createInstance(customName);
      } catch (MetaClass.ConstructorNotFoundException e2) {
        // properties
        try {
          return new MetaClass(customClassName).createInstance(properties);
        } catch (MetaClass.ConstructorNotFoundException e3) {
          // empty arguments
          return new MetaClass(customClassName).createInstance();
        }
      }
    }
  }

  /**
   * Annotate quotes and extract them like sentences
   */
  public Annotator quote(Properties properties) {
	Properties relevantProperties = PropertiesUtils.extractPrefixedProperties(properties,
	    Annotator.STANFORD_QUOTE + '.', true);
	Properties depparseProperties = PropertiesUtils.extractPrefixedProperties(properties,
	    Annotator.STANFORD_DEPENDENCIES + '.');
	for (String key: depparseProperties.stringPropertyNames())  {
	    relevantProperties.setProperty("quote.attribution." + Annotator.STANFORD_DEPENDENCIES + '.' + key,
		depparseProperties.getProperty(key));
		}
    return new QuoteAnnotator(relevantProperties);
  }
}
