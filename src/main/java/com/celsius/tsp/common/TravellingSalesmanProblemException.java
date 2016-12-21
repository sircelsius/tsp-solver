package com.celsius.tsp.common;

/**
 * General Exception for TravellingSalesmanProblem.
 *
 * @since 1.0.0
 * @author marc.bramaud
 */
public class TravellingSalesmanProblemException extends RuntimeException {
  public TravellingSalesmanProblemException() {}

  public TravellingSalesmanProblemException(String s) {
    super(s);
  }
}
