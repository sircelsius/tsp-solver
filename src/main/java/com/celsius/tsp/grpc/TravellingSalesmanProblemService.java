package com.celsius.tsp.grpc;

import com.celsius.tsp.proto.TravellingSalesmanProblemServiceGrpc;
import com.celsius.tsp.proto.TspService;
import com.celsius.tsp.solver.NearestNeighbourHeuristic;
import com.celsius.tsp.solver.TravellingSalesmanHeuristic;
import com.celsius.tsp.solver.rx.ReactiveTravellingSalesmanHeuristic;
import com.celsius.tsp.solver.rx.RxNearestNeighbourHeuristic;

import io.grpc.stub.StreamObserver;

public class TravellingSalesmanProblemService
    extends TravellingSalesmanProblemServiceGrpc.TravellingSalesmanProblemServiceImplBase {

  private final TravellingSalesmanHeuristic heuristic;
  private final ReactiveTravellingSalesmanHeuristic rxheuristic;

  public TravellingSalesmanProblemService() {
    heuristic = new NearestNeighbourHeuristic();
    rxheuristic = new RxNearestNeighbourHeuristic();
  }

  @Override
  public void solve(TspService.TravellingSalesmanProblem request,
                    StreamObserver<TspService.TravellingSalesmanSolution> responseObserver) {
    try {
      double random = Math.random();
      if (random < 0.5) {
        System.out.println("Direct.");
        responseObserver.onNext(heuristic.solve(request));
        responseObserver.onCompleted();
      } else {
        System.out.println("Reactive.");
        rxheuristic.solve(request)
          .doOnSuccess(travellingSalesmanSolution -> {
            responseObserver.onNext(travellingSalesmanSolution);
            responseObserver.onCompleted();
          })
          .doOnError(throwable -> responseObserver.onError(throwable))
          .subscribe();
      }
    } catch (Exception e) {
      responseObserver.onError(e);
    }
  }
}
