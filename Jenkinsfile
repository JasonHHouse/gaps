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
        withSonarQubeEnv(credentialsId: 'c273ab1594d36799589bff7512b5cfcafff617b1', installationName: 'SonarQube') { // You can override the credential to be used
        sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.7.0.1746:sonar'
      }
    }
  }
}