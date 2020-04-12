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
                }
            }
        }
    }
}
