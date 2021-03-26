package edu.stanford.nlp.stats;

import java.text.NumberFormat;
import java.util.*;

/**
 * Interface representing a mapping between pairs of typed objects and double
 * values.
 *
 * @author Angel Chang
 */
public interface TwoDimensionalCounterInterface<K1, K2>  {

  void defaultReturnValue(double rv);
  double defaultReturnValue();

  /**
   * @return total number of entries (key pairs)
   */
  int size();

  boolean containsKey(K1 o1, K2 o2);

  /**
   */
  void incrementCount(K1 o1, K2 o2);

  /**
   */
  void incrementCount(K1 o1, K2 o2, double count);

  /**
   */
  void decrementCount(K1 o1, K2 o2);

  /**
   */
  void decrementCount(K1 o1, K2 o2, double count);

  /**
   */
  void setCount(K1 o1, K2 o2, double count);

  double remove(K1 o1, K2 o2);

  /**
   */
  double getCount(K1 o1, K2 o2);

  double totalCount();

  /**
   */
  double totalCount(K1 k1);

  Set<K1> firstKeySet();

  Set<K2> secondKeySet();

  boolean isEmpty();

  void remove(K1 key);

  String toMatrixString(int cellSize);

  /**
   * Given an ordering of the first (row) and second (column) keys, will produce
   * a double matrix.
   */
  double[][] toMatrix(List<K1> firstKeys, List<K2> secondKeys);

  String toCSVString(NumberFormat nf);


  /** Counter based operations */

  /**
   * @return the inner Counter associated with key o
   */
  Counter<K2> getCounter(K1 o);

  //public Set<Map.Entry<K1, ClassicCounter<K2>>> entrySet();

  /**
   * replace the counter for K1-index o by new counter c
   */
  //public Counter<K2> setCounter(K1 o, Counter<K2> c);

  //public Counter<Pair<K1, K2>> flatten();

  //public void addAll(TwoDimensionalCounterInterface<K1, K2> c);

  //public void addAll(K1 key, Counter<K2> c);

  //public void subtractAll(K1 key, Counter<K2> c);

  //public void subtractAll(TwoDimensionalCounterInterface<K1, K2> c, boolean removeKeys);

  /**
   * Returns the counters with keys as the first key and count as the
   * total count of the inner counter for that key
   *
   * @return counter of type K1
   */
  //public Counter<K1> sumInnerCounter();

}
