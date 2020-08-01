node {

    stage('Clone sources') {
        git url: 'https://github.com/shmakvlad/api-tests.git'
    }

    stage("Build") {
        sh "./gradlew clean api:assemble"
    }

    stage("Run api test") {
        sh "./gradlew cleanTest test"
    }

    stage('Allure results') {
        allure([
            includeProperties: false,
            jdk              : '',
            properties       : [],
            reportBuildPolicy: 'ALWAYS',
            results          : [[path: 'api/build/allure-results']]
        ])
    }
}