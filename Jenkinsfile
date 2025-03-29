pipeline{
    agent any

    stages {
        stage('Build Docker image'){
           steps {
            sh 'echo Executando o comando docker build'
            script {
                dockerapp = docker.build("gustavolucen4/events:${env.BUILD_ID}", '-f ./Dockerfile ./')
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
           environment {
               tag_version = "${env.BUILD_ID}"
           }
           steps{
            sh 'echo Executando o comando kubectl apply'
            withKubeConfig([credentialsId: 'kubeconfig']){
                sh 'sed -i "s/{{tag}}/$tag_version/g" ./k8s/deployment.yaml'
                sh 'kubectl apply -f k8s/deployment.yaml'
            }
           }
        }
    }
}