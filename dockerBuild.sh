mvn clean install -DskipTests
docker build -f Dockerfile.ssl -t housewrecker/gaps:v0.1.2 .
docker push housewrecker/gaps:v0.1.2
docker build -f Dockerfile.no-ssl -t housewrecker/gaps:v0.1.2-no-ssl .
docker push housewrecker/gaps:v0.1.2-no-ssl