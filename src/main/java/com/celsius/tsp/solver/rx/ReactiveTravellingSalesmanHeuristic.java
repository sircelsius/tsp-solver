package com.celsius.tsp.solver.rx;

import com.celsius.tsp.proto.TspService;

import io.reactivex.Single;

/**
 * A service that solves TSPs using {@link io.reactivex.Observable}.
 *
 * @since 1.0.0
 * @author marc.bramaud
 */
@FunctionalInterface
public interface ReactiveTravellingSalesmanHeuristic {
  /**
   * Solves a {@link com.celsius.tsp.proto.TspService.TravellingSalesmanProblem} using a ReactiveX
   * implementation.
   * @param problem {@link com.celsius.tsp.proto.TspService.TravellingSalesmanProblem} the problem.
   * @return {@link Single} a Single that emits a
   *  {@link com.celsius.tsp.proto.TspService.TravellingSalesmanSolution}
   */
  Single<TspService.TravellingSalesmanSolution> solve(TspService.TravellingSalesmanProblem problem);
}
