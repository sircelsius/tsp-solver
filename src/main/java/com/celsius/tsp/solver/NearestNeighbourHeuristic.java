package com.celsius.tsp.solver;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import com.celsius.tsp.proto.TspService;
import com.celsius.tsp.solver.exceptions.HeuristicException;

import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor
public class NearestNeighbourHeuristic implements TravellingSalesmanHeuristic {
  @Override
  public TspService.TravellingSalesmanSolution solve(TspService.TravellingSalesmanProblem problem)
    throws Exception {
    List<TspService.Vertex> verticesToVisit = Lists.newArrayList(problem.getVerticesList());
    List<TspService.Vertex> visits = Lists.newArrayList();
    Set<TspService.Vertex> visitedVertices = Sets.newHashSet();

    TspService.Vertex currentVertex = problem.getVertices(problem.getDepartureVertexId());
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

    return TspService.TravellingSalesmanSolution.newBuilder()
      .addAllVertices(visits)
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
      .min((edge, t1) -> edge.getValue())
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
