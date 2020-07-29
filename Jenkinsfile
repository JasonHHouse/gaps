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
        curl -s -X POST https://api.telegram.org/bot1302374772:AAFdOH2GGdRsOxuBREQfKo9fU0IQNl6Q7sY/sendMessage -d chat_id=housewrecker -d text="SUCCESSFUL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})"
      }

      failure {
        curl -s -X POST https://api.telegram.org/bot1302374772:AAFdOH2GGdRsOxuBREQfKo9fU0IQNl6Q7sY/sendMessage -d chat_id=housewrecker -d text="FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})"
      }
    }
}