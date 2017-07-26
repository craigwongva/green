node { 
    def mvn = tool 'M3.0.5' 
    def TEST_STACK_NAME = 'craigj35'
    def test_stack_status = 'build'
    def TEST_STACK_IP
    def PRODUCTION_STACK_IP = '52.42.108.124'

   properties([
     parameters([
       string(
         defaultValue: 'default',
         description: 'Test Stack IP',
         name: 'TEST_STACK_IP'
       ),
       string(
         defaultValue: 'default',
         description: 'Production Stack IP',
         name: 'PRODUCTION_STACK_IP'
       ),
     ])
   ])

    stage('checkout') {
        checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/craigwongva/green']]]) 
    } 

    stage('buildTestInstanceWithApp') {
	if (params.TEST_STACK_IP == '') {
            sh "aws cloudformation create-stack --stack-name ${TEST_STACK_NAME} --template-url https://s3.amazonaws.com/venicegeo-devops-dev-gocontainer-project/cf-nexus-java.json --region us-west-2 --parameters ParameterKey=nexususername,ParameterValue=unused ParameterKey=nexuspassword,ParameterValue=unused ParameterKey=tomcatmgrpassword,ParameterValue=unused"
            sh "sleep 60"
        }
        else {
            TEST_STACK_IP = params.TEST_STACK_IP
        }
    }

    stage('describeTestInstance') {
	if (params.TEST_STACK_IP == '') {
            test_stack_status = 'build'
            def x = sh(script: "aws cloudformation describe-stacks --stack-name ${TEST_STACK_NAME} --region us-west-2", returnStdout: true)
            def temp = (x =~ /"OutputValue": "(.*)"/)
            TEST_STACK_IP = temp[0][1]
	    //sh "sleep 1500": this statement seems to cause a Jenkins failure
        }
    }

    stage('waitThenInvokePhantomOnApp') {
        if (test_stack_status == 'build') {
	sh "sleep 1500"
        }
	sh "cat invoke-phantom.js"
	//sh "BUILD_ID=dontKillMe ./invoke-phantom $anceID &"
	sh "BUILD_ID=dontKillMe ./invoke-phantom ${TEST_STACK_IP} &"
	sh "cat invoke-phantom.js"
	sh "sleep 60"
    }

    stage('curlAndInterpretAppStatus') {
	//sleep(1000*60*2) why is this a day plus? Overridden Groovy sleep???
	def mickey = [
	 "curl",  
	  "${TEST_STACK_IP}:8080/green/timer/status"]
	 .execute().text
	def ARBITRARY_SUCCESS_PCT = 0.95
	def NUM_GREEN_DOTS = 100
	def GREEN_DOT_STATUS_DONE = '4'

	//fails: if (mickey.indexOf(GREEN_DOT_STATUS_DONE.multiply(ARBITRARY_SUCCESS_PCT*NUM_GREEN_DOTS)) < 0) {
	//succeeds: if (mickey.indexOf('4'.multiply(95)) < 0) {

	//if (mickey.indexOf(GREEN_DOT_STATUS_DONE.multiply(ARBITRARY_SUCCESS_PCT*NUM_GREEN_DOTS)) < 0) {
	if (mickey.indexOf('4444444444444444444444444444444444444444444444444444444444') < 0) {
    	 error "red rover3 ${TEST_STACK_IP}:8080/green/timer/status $mickey" 
	}
	//somehow next shell this: pkill -f phantomjs
    }
    stage('rebuildApp') { 
        sh "/usr/local/apache-maven/bin/mvn clean -DskipTests install"
    } 
    stage('deployAppToProd') { 
        sh "whoami" 
        sh "pwd" 
        sh "scp -i /home/jenkins/craigradiantblueoregon.pem -o StrictHostKeyChecking=no /var/lib/jenkins/.m2/repository/com/demo/green/1.0-SNAPSHOT/green-1.0-SNAPSHOT.war ec2-user@${PRODUCTION_STACK_IP}:/usr/share/tomcat7/webapps/green.war" 
    }
    stage('cleanup') { 
        sh "pkill -f phantomjs"
        //sh "aws cloudformation delete-stack --stack-name ${TEST_STACK_NAME}  --region us-west-2"
    }
}
