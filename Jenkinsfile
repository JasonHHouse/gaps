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
        withSonarQubeEnv('SonarQube') {
           sh 'mvn clean install pmd:pmd checkstyle:checkstyle spotbugs:spotbugs sonar:sonar'
        }
      }
    }
  }
  post {
      success {
        telegramSend "SUCCESSFUL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})"
      }

      failure {
        telegramSend "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})"
      }
    }
}