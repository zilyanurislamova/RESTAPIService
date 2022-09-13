FROM openjdk:17
COPY ./target/classes/com/example/service/ /tmp
WORKDIR /tmp
ENTRYPOINT ["java","ServiceApplication"]