mvn clean install
docker build -f Dockerfile.ssl -t housewrecker/gaps:latest .
docker push housewrecker/gaps:latest
docker build -f Dockerfile.no-ssl -t housewrecker/gaps:latest-no-ssl .
docker push housewrecker/gaps:latest-no-ssl