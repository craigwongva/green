node { 
    def mvn = tool 'M3.0.5' 
    def craigt42_InstanceID 
    stage('checkout') {
	println "cheez-it " + 'hello'.indexOf('e')
        checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/craigwongva/green']]]) 
    } 
    stage('cf') {
        sh "aws cloudformation create-stack --stack-name craigt44 --template-url https://s3.amazonaws.com/venicegeo-devops-dev-gocontainer-project/cf-nexus-java.json --region us-west-2 --parameters ParameterKey=nexususername,ParameterValue=unused ParameterKey=nexuspassword,ParameterValue=unused ParameterKey=tomcatmgrpassword,ParameterValue=unused"
        sh "sleep 60"

        def x = sh(script: "aws cloudformation describe-stacks --stack-name craigt44 --region us-west-2", returnStdout: true)
        def temp = (x =~ /"OutputValue": "(.*)"/)
        craigt42_InstanceID = temp[0][1]
        println "alpha $craigt42_InstanceID"
	println "triscuit " + 'hello'.indexOf('e')
    }
    stage('cf-shell1') {
//sh "echo sleep 25*60 = 25 minutes"
sh "sleep 1500"
sh "echo starting to invoke $craigt42_InstanceID"
sh "cat invoke-phantom.js"
sh "#BUILD_ID=dontKillMe ./invoke-phantom ${craigt42_InstanceID} &"
sh "BUILD_ID=dontKillMe ./invoke-phantom $craigt42_InstanceID &"
sh "cat invoke-phantom.js"
sh "echo finished invoke"
sh "sleep 60"
    }
    stage('cf-groovy1') {
//sleep(1000*60*2) why is this a day plus? Overridden Groovy sleep???
//println System.getenv('craigt42_InstanceID')
println "$craigt42_InstanceID burgers"
//println "oreo1 " + 'hello'.indexOf('e')
//sleep(60)
def mickey = [
 "curl",  
////  "${System.getenv('craigt42_InstanceID')}:8080/green/timer/status"]
  "$craigt42_InstanceID:8080/green/timer/status"]
 .execute().text
//println "oreo2 " + 'hello'.indexOf('e')
println "Mickey is $mickey"
def ARBITRARY_SUCCESS_PCT = 0.95
def NUM_GREEN_DOTS = 100
def GREEN_DOT_STATUS_DONE = '4'

//fails: if (mickey.indexOf(GREEN_DOT_STATUS_DONE.multiply(ARBITRARY_SUCCESS_PCT*NUM_GREEN_DOTS)) < 0) {
//succeeds: if (mickey.indexOf('4'.multiply(95)) < 0) {

//println "licorice " + 'hello'.indexOf('e')
if (mickey.indexOf('4444444444444444444444444444444444444444444444444444444444') < 0) {
//if (mickey.indexOf(GREEN_DOT_STATUS_DONE.multiply(ARBITRARY_SUCCESS_PCT*NUM_GREEN_DOTS)) < 0) {
    error "red rover" 
}

//somehow next shell this: pkill -f phantomjs

    }

    stage('build') { 
        sh """/usr/local/apache-maven/bin/mvn clean -DskipTests install""" 
    } 
    stage('deploy') { 
        sh "whoami" 
        sh "pwd" 
        sh """scp -i /home/jenkins/craigradiantblueoregon.pem -o StrictHostKeyChecking=no /var/lib/jenkins/.m2/repository/com/demo/green/1.0-SNAPSHOT/green-1.0-SNAPSHOT.war ec2-user@34.210.232.195:/usr/share/tomcat7/webapps/green.war""" 
    }
}
