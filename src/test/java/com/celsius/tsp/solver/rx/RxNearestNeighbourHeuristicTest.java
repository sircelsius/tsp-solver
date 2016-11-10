package com.celsius.tsp.solver.rx;

import static org.junit.Assert.assertEquals;

import com.celsius.tsp.proto.TspService;

import com.google.common.collect.Lists;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import org.junit.Test;

import java.util.List;


/**
 * Created by marc on 09.11.16.
 */
public class RxNearestNeighbourHeuristicTest {

  @Test
  public void solve() throws Exception {
    List<TspService.Vertex> vertices = Lists.newArrayList();
    List<TspService.Edge> edges = Lists.newArrayList();
    TspService.Vertex vertex;
    TspService.Edge edge;
    int size = 25;

    for (int i = 1; i < size; i++) {
      vertex = TspService.Vertex.newBuilder()
        .setId(i)
        .build();
      vertices.add(vertex);

      for (int j = 1; j < i; j++) {
        edge = TspService.Edge.newBuilder()
          .setValue( i - j )
          .setDepartureVerticeId(i)
          .setArrivalVerticeId( i - j )
          .build();
        edges.add(edge);
      }

      for ( int k = i + 1; k < size; k++) {
        edge = TspService.Edge.newBuilder()
          .setValue( size + i - k )
          .setDepartureVerticeId(i)
          .setArrivalVerticeId( k )
          .build();
        edges.add(edge);
      }
    }

    TspService.TravellingSalesmanProblem problem = TspService.TravellingSalesmanProblem
        .newBuilder()
        .setIsSymmetric(true)
        .setDepartureVertexId(1)
        .setArrivalVertexId(size - 1)
        .addAllVertices(vertices)
        .addAllEdges(edges)
        .build();

    ReactiveTravellingSalesmanHeuristic heuristic = new RxNearestNeighbourHeuristic();
    Single<TspService.TravellingSalesmanSolution> solution = heuristic.solve(problem);
    TestObserver<TspService.TravellingSalesmanSolution> observer = new TestObserver<>();
    solution.subscribe(observer);

    observer.assertSubscribed();
    observer.awaitTerminalEvent();
    observer.assertValueCount(1);
    observer.assertNoErrors();

    observer.values()
        .forEach(travellingSalesmanSolution -> {
          for (int l = 0; l < size - 2; l++) {
            if ( l % 2 == 0 ) {
              assertEquals( (l / 2 ) + 1, travellingSalesmanSolution.getVertices(l).getId());
            } else {
              assertEquals( size - 1 - ( l + 1 ) / 2,
                  travellingSalesmanSolution.getVertices(l).getId());
            }
          }
          assertEquals(size - 1, travellingSalesmanSolution.getVertices(size - 2).getId());
        });

  }
}
