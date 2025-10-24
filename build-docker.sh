#!/bin/bash
# Build script for Gaps Docker image

# Add Docker binaries to PATH
export PATH="/Applications/Docker.app/Contents/Resources/bin:$PATH"

# Navigate to project directory
cd "$(dirname "$0")"

# Build the Docker image
echo "Building Docker image from Dockerfile.dev..."
docker build -f Dockerfile.dev -t gaps:dev .

# Check if build was successful
if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Docker image 'gaps:dev' built successfully!"
    echo ""
    echo "To run the container:"
    echo "  docker run -d -p 8484:8484 --name gaps-dev -v \$(pwd)/data:/usr/data gaps:dev"
else
    echo ""
    echo "❌ Docker build failed. Check the error messages above."
    exit 1
fi
