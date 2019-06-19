FROM gradle:5.4 as builder
ARG version
WORKDIR /message-manager
ADD . .
RUN gradle clean; gradle -PprojVersion=$version bootJar
RUN mkdir /jar; cp build/libs/message-manager-$version.jar /jar/

FROM openjdk:8-jre-alpine as main
COPY --from=builder /jar/message-manager-*.jar /message-manager/message-manager-*.jar
WORKDIR /message-manager
RUN addgroup -S appuser && adduser -S appuser -G appuser
USER appuser
EXPOSE 8080
ENTRYPOINT java -jar message-manager-*.jar