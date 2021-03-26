package edu.stanford.nlp.pipeline;

import edu.stanford.nlp.io.IOUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;

/**
 * This class contains mappings from strings to language properties files.
 */

public class LanguageInfo {

  /** languages supported **/
  public enum HumanLanguage {ARABIC, CHINESE, ENGLISH, FRENCH, GERMAN, SPANISH}

  /** list of properties files for each language **/
  public static final String ARABIC_PROPERTIES = "StanfordCoreNLP-arabic.properties";
  public static final String CHINESE_PROPERTIES = "StanfordCoreNLP-chinese.properties";
  public static final String ENGLISH_PROPERTIES = "StanfordCoreNLP.properties";
  public static final String FRENCH_PROPERTIES = "StanfordCoreNLP-french.properties";
  public static final String GERMAN_PROPERTIES = "StanfordCoreNLP-german.properties";
  public static final String SPANISH_PROPERTIES = "StanfordCoreNLP-spanish.properties";

  /** map enum to properties file **/
  public static final Map<HumanLanguage,String> languageToPropertiesFile;

  static {
    languageToPropertiesFile = new EnumMap<>(HumanLanguage.class);
    languageToPropertiesFile.put(HumanLanguage.ARABIC, ARABIC_PROPERTIES);
    languageToPropertiesFile.put(HumanLanguage.CHINESE, CHINESE_PROPERTIES);
    languageToPropertiesFile.put(HumanLanguage.ENGLISH, ENGLISH_PROPERTIES);
    languageToPropertiesFile.put(HumanLanguage.FRENCH, FRENCH_PROPERTIES);
    languageToPropertiesFile.put(HumanLanguage.GERMAN, GERMAN_PROPERTIES);
    languageToPropertiesFile.put(HumanLanguage.SPANISH, SPANISH_PROPERTIES);
  }

  private LanguageInfo() {

  }

  /**
   * Go through all of the paths via reflection, and print them out in a TSV format.
   * This is useful for command line scripts.
   *
   * @param args Ignored.
   */
  public static void main(String[] args) throws IllegalAccessException {
    for (Field field : LanguageInfo.class.getFields()) {
      System.out.println(field.getName() + "\t" + field.get(null));
    }
  }

  /** return the properties file name for a specific language **/
  public static String getLanguagePropertiesFile(String inputString) {
    return languageToPropertiesFile.get(getLanguageFromString(inputString));
  }

  /** return an actual properties object for a given language **/
  public static Properties getLanguageProperties(String inputString) throws IOException {
    Properties props = new Properties();
    InputStream is = IOUtils.getInputStreamFromURLOrClasspathOrFileSystem(getLanguagePropertiesFile(inputString));
    props.load(is);
    return props;
  }

  /** convert various input strings to language enum **/
  public static HumanLanguage getLanguageFromString(String inputString) {
    if (inputString.equalsIgnoreCase("arabic") || inputString.equalsIgnoreCase("ar"))
      return HumanLanguage.ARABIC;
    if (inputString.equalsIgnoreCase("english") || inputString.equalsIgnoreCase("en"))
      return HumanLanguage.ENGLISH;
    if (inputString.equalsIgnoreCase("chinese") || inputString.equalsIgnoreCase("zh"))
      return HumanLanguage.CHINESE;
    if (inputString.equalsIgnoreCase("french") || inputString.equalsIgnoreCase("fr"))
      return HumanLanguage.FRENCH;
    if (inputString.equalsIgnoreCase("german") || inputString.equalsIgnoreCase("de"))
      return HumanLanguage.GERMAN;
    if (inputString.equalsIgnoreCase("spanish") || inputString.equalsIgnoreCase("es"))
      return HumanLanguage.SPANISH;
    else
      return null;
  }

  /** boolean saying whether String represents a Stanford CoreNLP supported language **/
  public static boolean isStanfordCoreNLPSupportedLang(String lang) {
    return (getLanguageFromString(lang) != null);
  }

  /** Check if language is a segmenter language, return boolean. **/
  public static boolean isSegmenterLanguage(HumanLanguage language) {
    return language == HumanLanguage.ARABIC || language == HumanLanguage.CHINESE;
  }

  public static boolean isSegmenterLanguage(String inputString) {
    return isSegmenterLanguage(getLanguageFromString(inputString));
  }
}
