package com.celsius.tsp.solver.rx.sa;

import static io.reactivex.Single.just;

import com.celsius.tsp.common.CommonSolutionFunctions;
import com.celsius.tsp.proto.TspService;
import com.celsius.tsp.solver.rx.ReactiveTravellingSalesmanHeuristic;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.google.common.collect.Lists;
import io.reactivex.Single;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Random;

/**
 * Reactive implementation of the Simulated Annealing heuristic.
 *
 * @since 1.0.0
 * @author marc.bramaud
 */
@Log4j2
public class RxSimulatedAnnealingHeuristic implements ReactiveTravellingSalesmanHeuristic {
  private final ReactiveTravellingSalesmanHeuristic initialSolutionProvider;

  private final MetricRegistry registry = new MetricRegistry();
  private final Histogram enhancement = registry.histogram(
      MetricRegistry.name(this.getClass(), "enhancement")
  );
  private final Timer timer = registry.timer(MetricRegistry.name(this.getClass(), "solve"));
  private final JmxReporter reporter = JmxReporter.forRegistry(registry).build();

  public RxSimulatedAnnealingHeuristic(
      ReactiveTravellingSalesmanHeuristic initialSolutionProvider) {
    this.initialSolutionProvider = initialSolutionProvider;
    reporter.start();
  }

  @Override
  public Single<TspService.TravellingSalesmanSolution>
      solve(TspService.TravellingSalesmanProblem problem) {
    log.debug("Starting Simulated Annealing reactive heuristic.");
    final Timer.Context context = timer.time();

    return getInitialSolution(problem)
      .flatMap(travellingSalesmanSolution -> getNext(travellingSalesmanSolution,
        problem,
        100.0))
      .map(travellingSalesmanSolution -> {
        context.stop();
        return travellingSalesmanSolution;
      });
  }

  private Single<TspService.TravellingSalesmanSolution>
      getInitialSolution(TspService.TravellingSalesmanProblem problem) {
    return initialSolutionProvider.solve(problem);
  }

  private Single<TspService.TravellingSalesmanSolution> getNext(
      TspService.TravellingSalesmanSolution currentSolution,
      TspService.TravellingSalesmanProblem problem,
      double currentTemperature) {
    final double epsilon = 0.000001;
    if ( Math.abs(currentTemperature) < epsilon) {
      return just(currentSolution);
    }

    return iterateRandom(currentSolution, problem)
      .flatMap(travellingSalesmanSolution -> {
        if (travellingSalesmanSolution.getCost() < currentSolution.getCost()) {
          enhancement.update(
              ((currentSolution.getCost() - travellingSalesmanSolution.getCost()) * 100)
                / currentSolution.getCost()
          );
          return just(travellingSalesmanSolution);
        }

        enhancement.update(0);

        double newTemperature = 0.9 * currentTemperature;
        if (getProbability(currentSolution.getCost(),
            travellingSalesmanSolution.getCost(),
            currentTemperature) ) {
          return getNext(travellingSalesmanSolution, problem, newTemperature);
        }

        return getNext(currentSolution, problem, newTemperature);
      });
  }

  private Single<TspService.TravellingSalesmanSolution>
      iterateRandom(TspService.TravellingSalesmanSolution sourceSolution,
                    TspService.TravellingSalesmanProblem problem) {
    return just(sourceSolution)
      .map(travellingSalesmanSolution ->
        swapElementsInList(travellingSalesmanSolution.getVerticesList())
      )
      .map(vertices -> TspService.TravellingSalesmanSolution.newBuilder()
        .addAllVertices(vertices)
        .setCost(CommonSolutionFunctions
          .calculateWeightFromEdgesAndSolution(problem.getEdgesList(), vertices)
        ).build());
  }

  private List<TspService.Vertex> swapElementsInList(List<TspService.Vertex> vertices) {
    int size = vertices.size();
    List result = Lists.newArrayList(vertices);

    if ( size < 3) {
      return vertices;
    }

    Random generator = new Random();
    // generate random integer between 1 and size - 2.
    // This is so that we do not take the first or two before last vertex.
    int random = 1 + generator.nextInt(size - 2);
    TspService.Vertex tempVertex = vertices.get(random);
    result.set(random, vertices.get(random + 1));
    result.set(random +  1, tempVertex);
    return result;
  }

  private Boolean getProbability(int oldEnergy, int newEnergy, double temperature) {
    return Math.exp(( oldEnergy - newEnergy ) / temperature) > Math.random();
  }
}
