#!/usr/bin/env groovy

pipeline {
    agent any
    tools {
        jdk 'Java 17'
    }
    stages {
        stage('Clean') {
            steps {
                echo 'Cleaning Project'
                sh 'chmod +x gradlew'
                sh './gradlew clean'
            }
        }
        stage('Build') {
            steps {
                echo 'Generating Resources'
                sh './gradlew fabric:runData'

                echo 'Building'
                sh './gradlew build'
            }
        }
    }
}