# TSP solver [![Build Status](https://travis-ci.org/sircelsius/tsp-solver.svg?branch=master)](https://travis-ci.org/sircelsius/tsp-solver) [![codecov](https://codecov.io/gh/sircelsius/tsp-solver/branch/master/graph/badge.svg)](https://codecov.io/gh/sircelsius/tsp-solver)

A Java gRPC server that solves the Travelling Salesman Problem.

*NB* A lot of the gRPC server code and build configurations (gradle) comes from [grpc/grpc-java](http://www.github.com/grpc/grpc-java).

## Quickstart

You can build the server with the following command:

```` bash
./gradlew installDist
````

This will compile the code and generate a run script in `./build/install/tsp-solver/bin/travelling-salesman-server`.

You can run this server with the following:

```` bash
./build/install/tsp-solver/bin/travelling-salesman-server
````

## About

The idea behind this project is to try the following libraries:

  * [`gRPC`](http://www.grpc.io/).
  * [`rxjava2`](https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0).

As well as to show (non optimal) implementations of different heuristics used to solve the [Travelling Salesman Problem](https://en.wikipedia.org/wiki/Travelling_salesman_problem).
