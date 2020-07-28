pipeline {
  agent any
  stages {
    stage('Minify') {
      steps {
        sh './minify'
      }
    }

    stage('Build Jars') {
      steps {
        sh 'mvn clean install'
      }
    }

  }
}