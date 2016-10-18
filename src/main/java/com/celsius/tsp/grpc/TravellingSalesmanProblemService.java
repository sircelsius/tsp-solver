package com.celsius.tsp.grpc;

import com.celsius.tsp.proto.TravellingSalesmanProblemServiceGrpc;
import com.celsius.tsp.proto.TspService;
import com.celsius.tsp.solver.NearestNeighbourHeuristic;
import com.celsius.tsp.solver.TravellingSalesmanHeuristic;
import io.grpc.stub.StreamObserver;

public class TravellingSalesmanProblemService
    extends TravellingSalesmanProblemServiceGrpc.TravellingSalesmanProblemServiceImplBase {

  private final TravellingSalesmanHeuristic heuristic;

  public TravellingSalesmanProblemService() {
    heuristic = new NearestNeighbourHeuristic();
  }

  @Override
  public void solve(TspService.TravellingSalesmanProblem request,
                    StreamObserver<TspService.TravellingSalesmanSolution> responseObserver) {
    try {
      responseObserver.onNext(heuristic.solve(request));
    } catch (Exception e) {
      responseObserver.onError(e);
    } finally {
      responseObserver.onCompleted();
    }
  }
}
