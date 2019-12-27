mvn clean install
docker build -f Dockerfile.ssl -t housewrecker/gaps:v0.1.0 .
docker push housewrecker/gaps:v0.1.0
docker build -f Dockerfile.no-ssl -t housewrecker/gaps:v0.1.0-no-ssl .
docker push housewrecker/gaps:v0.1.0-no-ssl