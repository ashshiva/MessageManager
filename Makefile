gradle = $(GRADLE_HOME)/bin/gradle
port ?= 8080
version ?= 1.0.0

clean:
	$(gradle) clean
	
build: clean
	$(gradle) -PprojVersion=$(version) bootJar
	
run: build
	java -jar build/libs/message-manager-$(version).jar
	
docker:
	DOCKER_BUILDKIT=1 docker build --build-arg version=$(version) -t message-manager:$(version) .
	
docker-run:
	(docker rm `docker ps -qa`) || true
	docker volume prune -f
	docker image prune -f
	docker run --rm -p $(port):8080 message-manager:$(version)
	
