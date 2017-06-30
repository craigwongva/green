node { 
    def mvn = tool 'M3.0.5' 
    def craigt42_InstanceID 
    stage('checkout') {
        checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/craigwongva/green']]]) 
    } 
    stage('build') { 
        sh """/usr/local/apache-maven/bin/mvn clean -DskipTests install""" 
    } 
    stage('deploy') { 
        sh "whoami" 
        sh "pwd" 
        sh """scp -i /home/jenkins/craigradiantblueoregon.pem -o StrictHostKeyChecking=no /var/lib/jenkins/.m2/repository/com/demo/green/1.0-SNAPSHOT/green-1.0-SNAPSHOT.war ec2-user@35.160.37.166:/usr/share/tomcat7/webapps/green.war""" 
    }
}
