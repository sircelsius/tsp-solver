package com.celsius.tsp;

import com.celsius.tsp.grpc.TravellingSalesmanServer;

import java.io.IOException;

public class TravellingSalesmanSolverApplication {
  /**
   * Runs a {@link TravellingSalesmanServer}. Blocks until shutdown
   * @param args the arguments
   * @throws InterruptedException thrown by {@link TravellingSalesmanServer}
   * @throws IOException thrown by {@link TravellingSalesmanServer}
   */
  public static void main(String[] args) throws InterruptedException, IOException {
    TravellingSalesmanServer server = new TravellingSalesmanServer(8980);
    server.start();
    server.blockUntilShutdown();
  }
}
