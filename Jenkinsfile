pipeline{
    agent any

    stages {
        stage('Build Docker image'){
           steps{
            sh 'echo Executando o comando docker build'
            script {
                dockerapp = docker.build("gustavolucen4/events:${env.BUILD_ID}", '-f ./src/Dockerfile ./src')
            }
           }
        }

        stage('Push Docker image'){
           steps{
            sh 'echo Executando o comando docker push'
            script {
                docker.withRegistry('https://registry.hub.docker.com', 'dockerhub'){
                    dockerapp.push('latest')
                    dockerapp.push("${env.BUILD_ID}")
                }
            }
           }
        }

        stage('Deploy no kubernets'){
           steps{
            sh 'echo Executando o comando kubectl apply'
           }
        }
    }
}