pipeline {
    agent any
    stages {
        stage("Build Maven") {
            steps {
               bat 'mvn -B clean package'
            }
        }
        stage("Run Gatling") {
            steps {
               bat 'mvn gatling:test'
            }
            post {
                always {
                    gatlingArchive()
                    step([$class: 'GatlingPublisher', enabled: true])
                    publishHTML([allowMissing: false, alwaysLinkToLastBuild: true, keepAll: false, reportDir: 'simulations', reportFiles: 'index.html', reportName: 'Load Test', reportTitles: ''])
                }
            }
        }
    }
}
