package com.celsius.tsp.solver;


import com.celsius.tsp.common.CommonProblemFunctions;
import com.celsius.tsp.common.CommonSolutionFunctions;
import com.celsius.tsp.proto.TspService;
import com.celsius.tsp.solver.exceptions.HeuristicException;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Solves a {@link com.celsius.tsp.proto.TspService.TravellingSalesmanProblem} using the
 * Nearest Neighbour heuristic.
 * See https://en.wikipedia.org/wiki/Nearest_neighbour_algorithm
 *
 * @since 1.0.0
 * @author marc.bramaud
 */
@Log4j2
public class NearestNeighbourHeuristic implements TravellingSalesmanHeuristic {

  private final MetricRegistry registry = new MetricRegistry();
  private final Timer timer = registry.timer(MetricRegistry.name(this.getClass(), "solve"));
  private final JmxReporter reporter = JmxReporter.forRegistry(registry).build();

  public NearestNeighbourHeuristic() {
    reporter.start();
  }

  @Override
  public TspService.TravellingSalesmanSolution solve(TspService.TravellingSalesmanProblem problem)
    throws Exception {

    log.debug("Starting Nearest Neighbour synchronous heuristic.");
    final Timer.Context context = timer.time();

    List<TspService.Vertex> verticesToVisit = Lists.newArrayList(problem.getVerticesList());
    List<TspService.Vertex> visits = Lists.newArrayList();
    Set<TspService.Vertex> visitedVertices = Sets.newHashSet();

    TspService.Vertex currentVertex = CommonProblemFunctions
        .getVertexById(problem, problem.getDepartureVertexId());
    visits.add(currentVertex);
    visitedVertices.add(currentVertex);
    verticesToVisit.remove(currentVertex);


    while (visitedVertices.size() < problem.getVerticesCount() - 1) {
      Optional<TspService.Vertex> nextVertex =
          getNearestNeighbour(problem, currentVertex, visitedVertices);

      if (nextVertex.isPresent()) {
        visits.add(nextVertex.get());
        visitedVertices.add(nextVertex.get());
        currentVertex = nextVertex.get();
        verticesToVisit.remove(verticesToVisit.indexOf(nextVertex.get()));
      } else {
        throw new HeuristicException("Unable to get next vertex");
      }
    }
    // virtually add last vertex
    visits.add(CommonProblemFunctions.getVertexById(problem, problem.getArrivalVertexId()));

    context.stop();
    log.debug("Done with Nearest Neighbour synchronous heuristic.");
    return TspService.TravellingSalesmanSolution.newBuilder()
      .addAllVertices(visits)
      .setCost(
        CommonSolutionFunctions.calculateWeightFromEdgesAndSolution(problem.getEdgesList(), visits)
      )
      .build();

  }

  private Optional<TspService.Vertex> getNearestNeighbour(
      TspService.TravellingSalesmanProblem problem,
      TspService.Vertex currentVertex,
      Set<TspService.Vertex> visited) {
    return getEdgesFromVertex(problem.getEdgesList(), currentVertex)
      .filter(edge ->
        filterLast(edge, visited, problem.getArrivalVertexId(), problem.getVerticesCount()))
      .filter(edge -> filterVisited(visited, edge))
      .reduce((edge, edge1) -> edge.getValue() < edge1.getValue() ? edge : edge1)
      .map(edge -> getArrivalVertex(problem.getVerticesList(), edge.getArrivalVerticeId()))
      .orElse(Optional.empty());
  }

  private Stream<TspService.Edge> getEdgesFromVertex(List<TspService.Edge> edges,
                                                     TspService.Vertex vertex) {
    return edges
      .stream()
      .filter(edge -> edge.getDepartureVerticeId() == vertex.getId());
  }

  private Boolean filterVisited(Set<TspService.Vertex> visited, TspService.Edge candidate) {
    return visited.stream()
      .filter(vertex -> vertex.getId() != candidate.getArrivalVerticeId())
      .collect(Collectors.toList())
      .size() == visited.size();
  }

  private Boolean filterLast(TspService.Edge candidate, Set<TspService.Vertex> visited,
                             int lastId, int problemSize) {
    if (visited.size() == problemSize - 1) {
      return true;
    }

    if (lastId == candidate.getArrivalVerticeId()) {
      return false;
    }

    return true;
  }

  private Optional<TspService.Vertex> getArrivalVertex(List<TspService.Vertex> vertices, int id) {
    return vertices.stream()
      .filter(vertex -> vertex.getId() == id)
      .findFirst();
  }
}
