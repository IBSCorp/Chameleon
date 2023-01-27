pipeline {
    agent { label 'QualIT-linux' }
    stages {
        stage('Clean workspace') {
            steps{
                cleanWs()
            }
        }
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build Maven') {
            steps {
                withMaven(maven: 'Maven3') {
                    sh 'mvn clean package -Dmaven.test.skip=true -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true -Dmaven.wagon.http.ssl.ignore.validity.dates=true'
                }
            }
        }
        stage('Deploy to Nexus') {
            steps {
                withMaven(maven: 'Maven3') {
                    sh 'mvn deploy -Dmaven.test.skip=true -Dmaven.wagon.http.ssl.insecure=true -Dmaven.wagon.http.ssl.allowall=true -Dmaven.wagon.http.ssl.ignore.validity.dates=true'
                }
            }
        }
    }
}