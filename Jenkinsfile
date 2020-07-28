pipeline {
  agent any
  stages {
    stage('Minify') {
      steps {
        sh 'npm install'
        sh 'npm ci'
        sh '''npm run minify-input-css
npm run uglifyjs-index-js
npm run uglifyjs-configuration-js
npm run uglifyjs-libraries-js
npm run uglifyjs-recommended-js
npm run uglifyjs-common-js
npm run uglifyjs-payload-js
npm run uglifyjs-mislabeled-js'''
      }
    }

    stage('Build Jars') {
      steps {
        sh 'mvn clean install pmd:pmd checkstyle:checkstyle spotbugs:spotbugs'
      }
    }

  }
}