package com.celsius.tsp.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class TravellingSalesmanServer {
  private final int port;
  private final Server server;

  /**
   * Simplest constructor.
   * @param port the port on which to run
   */
  public TravellingSalesmanServer(int port) {
    this(ServerBuilder.forPort(port), port);
  }

  /**
   * Constructor with a {@link ServerBuilder}.
   * @param serverBuilder an instance of {@link ServerBuilder}
   * @param port the port on which to run
   */
  public TravellingSalesmanServer(ServerBuilder<?> serverBuilder, int port) {
    this.port = port;
    this.server = serverBuilder.addService(new TravellingSalesmanProblemService())
      .build();
  }

  /**
   * Starts the server and registers a shutdown hook.
   * @throws IOException on {@link Server#start()}
   */
  public void start() throws IOException {
    server.start();
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        TravellingSalesmanServer.this.stop();
      }
    });
  }

  /**
   * Shuts down the server.
   */
  public void stop() {
    if (server != null) {
      server.shutdown();
    }
  }

  /**
   * Waits for termination.
   * @throws InterruptedException on {@link Server#awaitTermination()}
   */
  public void blockUntilShutdown() throws InterruptedException {
    if ( server != null) {
      server.awaitTermination();
    }
  }
}
