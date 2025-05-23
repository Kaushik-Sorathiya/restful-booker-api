pipeline {
    agent any

    tools {
        maven 'Maven 3.8.1'
    }

    stages {
        stage('Build') {
            steps {
                bat 'mvn clean compile'
            }
        }
        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }
        stage('Report') {
            steps {
                publishHTML(target: [
                    reportDir: 'test-output',
                    reportFiles: 'ExtentReport.html',
                    reportName: 'Extent Report'
                ])
            }
        }
        stage('Archive Report') {
            steps {
                archiveArtifacts artifacts: 'test-output/ExtentReport.html'
            }
        }
    }
}


