package com.celsius.tsp.grpc;

import com.celsius.tsp.proto.TravellingSalesmanProblemServiceGrpc;
import com.celsius.tsp.proto.TspService;
import com.celsius.tsp.solver.NearestNeighbourHeuristic;
import com.celsius.tsp.solver.TravellingSalesmanHeuristic;
import com.celsius.tsp.solver.rx.ReactiveTravellingSalesmanHeuristic;
import com.celsius.tsp.solver.rx.RxNearestNeighbourHeuristic;

import io.grpc.stub.StreamObserver;
import lombok.extern.log4j.Log4j2;

@Log4j2
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
    log.debug("Solving problem of size: {}", request.getVerticesCount());
    try {
      double random = Math.random();
      if (random < 0.5) {
        log.info("Running synchronous heuristic {}.", heuristic.getClass().getSimpleName());
        TspService.TravellingSalesmanSolution solution = heuristic.solve(request);
        log.debug("Problem solved with solution: {}", solution.toString());
        responseObserver.onNext(solution);
        responseObserver.onCompleted();
      } else {
        log.info("Running reactive heuristic {}.", rxheuristic.getClass().getSimpleName());
        rxheuristic.solve(request)
          .doOnSuccess(travellingSalesmanSolution -> {
            log.debug("Problem solved with solution: {}", travellingSalesmanSolution.toString());
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
