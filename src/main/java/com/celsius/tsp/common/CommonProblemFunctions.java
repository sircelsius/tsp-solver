package com.celsius.tsp.common;

import com.celsius.tsp.proto.TspService;

import java.util.List;
import java.util.Optional;

/**
 * Created by marc on 09.11.16.
 */
public class CommonProblemFunctions {
  /**
   * Given a {@link com.celsius.tsp.proto.TspService.TravellingSalesmanProblem} and an integer,
   * Returns the {@link com.celsius.tsp.proto.TspService.Vertex} with that ID
   * @param problem com.celsius.tsp.proto.TspService.TravellingSalesmanProblem the problem.
   * @param id int the ID.
   * @return the {@link com.celsius.tsp.proto.TspService.Vertex} with that ID.
   */
  public static TspService.Vertex
      getVertexById(TspService.TravellingSalesmanProblem problem, int id) {
    Optional<TspService.Vertex> opt = problem.getVerticesList()
        .stream()
        .filter(vertex -> vertex.getId() == id)
        .findFirst();

    return opt.isPresent() ? opt.get() : TspService.Vertex.getDefaultInstance();
  }

  /**
   * Given a {@link List} of visited {@link com.celsius.tsp.proto.TspService.Vertex} and
   * a {@link com.celsius.tsp.proto.TspService.Vertex}, returns true if the vertex is in the list.
   * @param visited {@link List} visited {@link com.celsius.tsp.proto.TspService.Vertex}.
   * @param vertex {@link com.celsius.tsp.proto.TspService.Vertex} the vertex to look for.
   * @return {@link Boolean} true if the vertex is in the list.
   */
  public static Boolean isVertexVisited(List<TspService.Vertex> visited,
                                        TspService.Vertex vertex) {
    return visited.contains(vertex);
  }

  /**
   * Given a {@link com.celsius.tsp.proto.TspService.TravellingSalesmanProblem} and a
   * {@link com.celsius.tsp.proto.TspService.Vertex}, returns true if the vertex is the arrival.
   * @param problem {@link com.celsius.tsp.proto.TspService.TravellingSalesmanProblem} the problem.
   * @param vertex {@link com.celsius.tsp.proto.TspService.Vertex} the vertex.
   * @return {@link Boolean} true if the given vertex is the arrival.
   */
  public static Boolean isVertexArrival(TspService.TravellingSalesmanProblem problem,
                                  TspService.Vertex vertex) {
    return vertex.getId() == problem.getArrivalVertexId();
  }
}
