package edu.stanford.nlp.util;

/**
 * Disjoint set interface.
 *
 * @author Dan Klein
 * @version 4/17/01
 */
public interface DisjointSet<T> {
  T find(T o);

  void union(T a, T b);
}
