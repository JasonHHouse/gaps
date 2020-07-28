pipeline {
  agent any
  stages {
    stage('Minify') {
      steps {
        sh 'npm install'
        sh 'npm ci'
        sh './minify'
      }
    }

    stage('Build Jars') {
      steps {
        sh 'mvn clean install pmd:pmd checkstyle:checkstyle spotbugs:spotbugs'
      }
    }

    stage('Build Docker and Test') {
      steps {
        sh 'docker build -f Dockerfile -t gaps-dev .'
        sh 'docker run -p 8484:8484 --name gaps-dev gaps-dev'
        sh 'cypress run'
      }
    }

  }
}