FROM openjdk:11
WORKDIR /app
COPY target/my_auction_rest-1.0.0-SNAPSHOT.jar ./my_auction_rest-1.0.0.jar
ENTRYPOINT [ "java", "-jar", "./my_auction_rest-1.0.0.jar" ]