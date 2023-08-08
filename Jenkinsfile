pipeline {
    agent any

    stages {
        stage('Give Gradle Permission') {
            steps {
                script {
                    sh 'chmod +X ./gradlew'
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    sh './gradlew clean build --no-daemon'
                }
            }
        }
    }
}