package com.celsius.tsp.solver.rx;

import com.celsius.tsp.proto.TspService;

import io.reactivex.Single;

/**
 * A service that solves TSPs using Observables.
 */
@FunctionalInterface
public interface ReactiveTravellingSalesmanHeuristic {
  Single<TspService.TravellingSalesmanSolution> solve(TspService.TravellingSalesmanProblem problem);
}
