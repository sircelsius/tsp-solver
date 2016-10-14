package com.celsius.tsp.solver;

import com.celsius.tsp.proto.TspService;

@FunctionalInterface
public interface TravellingSalesmanHeuristic {
  TspService.TravellingSalesmanSolution solve( TspService.TravellingSalesmanProblem problem )
    throws Exception;
}
