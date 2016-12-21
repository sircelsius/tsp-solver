package com.celsius.tsp.common;

import com.celsius.tsp.proto.TspService;

import org.slf4j.helpers.MessageFormatter;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * Common functions for {@link com.celsius.tsp.proto.TspService.TravellingSalesmanSolution}.
 *
 * @since 1.0.0
 * @author marc.bramaud
 */
public class CommonSolutionFunctions {
  /**
   * Given a {@link List} of {@link com.celsius.tsp.proto.TspService.Edge},
   * Calculate their accumulated weight.
   * @param edges {@link List} the {@link com.celsius.tsp.proto.TspService.Edge} to accumulate.
   * @return {@link int} the accumulated weight.
   * @throws TravellingSalesmanProblemException when unable to reduce the list.
   */
  public static int calculateWeight(List<TspService.Edge> edges)
  throws TravellingSalesmanProblemException {
    Optional<TspService.Edge> reduced =  edges.stream()
        .reduce((edge, edge1) -> TspService.Edge.newBuilder()
            .setDepartureVerticeId(edge.getDepartureVerticeId())
            .setArrivalVerticeId(edge1.getArrivalVerticeId())
            .setValue(edge.getValue() + edge1.getValue())
            .build()
      );

    if (reduced.isPresent()) {
      return reduced.get().getValue();
    } else {
      throw new TravellingSalesmanProblemException("Unable to calculate weight.");
    }
  }

  /**
   * Given a {@link List} of {@link com.celsius.tsp.proto.TspService.Edge}
   * and a {@link List} of {@link com.celsius.tsp.proto.TspService.Vertex},
   * Return the accumulated cost of going to these vertices in order
   * @param edges {@link List} the list of {@link com.celsius.tsp.proto.TspService.Edge}
   * @param vertices {@link List} the list of {@link com.celsius.tsp.proto.TspService.Vertex}
   * @return int the accumulated cost
   * @throws TravellingSalesmanProblemException when no edge was found between to vertices
   */
  public static int calculateWeightFromEdgesAndSolution(List<TspService.Edge> edges,
                                                        List<TspService.Vertex> vertices)
  throws TravellingSalesmanProblemException {
    return IntStream.range(0, vertices.size() - 2)
      .map(i -> {
            Optional<TspService.Edge> edgeOptional = edges.stream()
                .filter(edge -> edge.getDepartureVerticeId() == vertices.get(i).getId()
                  && edge.getArrivalVerticeId() == vertices.get(i + 1).getId())
                .findFirst();

            if (edgeOptional.isPresent()) {
              return edgeOptional.get().getValue();
            } else {
              throw new TravellingSalesmanProblemException(
                MessageFormatter.format("Unable to get Edge between Vertices {} and {}",
                  i,
                  i + 1).getMessage()
              );
            }
          }
      )
      .sum();
  }
}
