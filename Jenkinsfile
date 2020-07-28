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

  }
}