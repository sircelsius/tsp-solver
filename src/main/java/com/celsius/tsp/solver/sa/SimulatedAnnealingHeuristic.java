package com.celsius.tsp.solver.sa;

import com.celsius.tsp.proto.TspService;
import com.celsius.tsp.solver.TravellingSalesmanHeuristic;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SimulatedAnnealingHeuristic implements TravellingSalesmanHeuristic {

  private TravellingSalesmanHeuristic initialSolutionProvider;

  @Override
  public TspService.TravellingSalesmanSolution solve(TspService.TravellingSalesmanProblem problem)
    throws Exception {
    return null;
  }

  private TspService.TravellingSalesmanSolution
      getInitialSolution(TspService.TravellingSalesmanProblem problem) throws Exception {
    return initialSolutionProvider.solve(problem);
  }



}
