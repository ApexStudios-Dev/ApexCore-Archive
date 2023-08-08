pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                script {
                    sh 'chmod +X ./gradlew'
                    sh './gradlew clean build --no-daemon'
                }
            }
        }
    }
}