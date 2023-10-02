pipeline {
    agent { label 'QualIT-win' }
    stages {
        stage('Clean workspace') {
            steps {
                cleanWs()
            }
        }
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Package & Test') {
            steps {
                withMaven(maven: 'Maven3') {
                    bat encoding: 'UTF-8', script: 'mvn clean package test "-Dcucumber.filter.tags=not @ignore" -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true -Dmaven.wagon.http.ssl.ignore.validity.dates=true'
                }
            }
        }
        stage('Deploy to Nexus') {
            steps {
                withMaven(maven: 'Maven3') {
                    bat encoding: 'UTF-8', script: 'mvn deploy -Dmaven.test.skip=true -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true -Dmaven.wagon.http.ssl.ignore.validity.dates=true'
                }
            }
        }
    }
}