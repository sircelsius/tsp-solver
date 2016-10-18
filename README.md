# TSP solver

A Java gRPC server that solves the Travelling Salesman Problem.

*NB* A lot of the gRPC server code and build configurations (gradle) comes from [grpc/grpc-java](github.com/grpc/grpc-java).

You can build the server with the following command:

```` bash
./gradlew installDist
````

This will compile the code and generate a run script in `./build/install/tsp-solver/bin/travelling-salesman-server`.

You can run this server with the following:

```` bash
./build/install/tsp-solver/bin/travelling-salesman-server
````
