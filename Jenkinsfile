pipeline {
    agent any

    tools {
        jdk   'JDK21'
        maven 'Maven3.9'
    }

    environment {
        APP_NAME     = 'spring-boot-cicd'
        DOCKER_IMAGE = "myusername/${APP_NAME}"
        DOCKER_TAG   = "${BUILD_NUMBER}"
        NAMESPACE    = 'production'
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timeout(time: 45, unit: 'MINUTES')
        timestamps()
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
                sh 'git log --oneline -5'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean compile -q'
            }
        }

        stage('Unit Tests') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                sh 'mvn package -DskipTests'
            }
        }

        stage('Docker Build') {
            steps {
                sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
            }
        }
    }

    post {
        success {
            echo "Deployed ${DOCKER_IMAGE}:${DOCKER_TAG} successfully!"
        }
        failure {
            mail to: 'devteam@company.com',
                 subject: "FAILED: ${JOB_NAME} #${BUILD_NUMBER}",
                 body: "${BUILD_URL}"
        }
        always {
            sh 'docker logout || true'
            cleanWs()
        }
    }
}
