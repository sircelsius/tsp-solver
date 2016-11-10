package com.celsius.tsp.solver;

import com.celsius.tsp.proto.TspService;

/**
 * Service that solves {@link com.celsius.tsp.proto.TspService.TravellingSalesmanProblem}
 * using a fully synchronous approach.
 *
 * @since 1.0.0
 * @author marc.bramaud
 */
@FunctionalInterface
public interface TravellingSalesmanHeuristic {
  /**
   * Solves the problem synchronously.
   * @param problem {@link com.celsius.tsp.proto.TspService.TravellingSalesmanProblem} the problem.
   * @return {@link com.celsius.tsp.proto.TspService.TravellingSalesmanSolution} the solution.
   * @throws Exception
   */
  TspService.TravellingSalesmanSolution solve( TspService.TravellingSalesmanProblem problem )
    throws Exception;
}
