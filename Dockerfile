FROM java:openjdk-8-jdk
MAINTAINER <Marc Bramaud m.duboucheron@gmail.com>

WORKDIR /app

COPY gradle gradle
COPY \
  build.gradle \
  gradlew \
  settings.gradle \
  ./

RUN ./gradlew

RUN ./gradlew assemble

ADD . ./

EXPOSE 8980
CMD ./build/install/tsp-solver/bin/travelling-salesman-server
