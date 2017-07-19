node { 
    def mvn = tool 'M3.0.5' 
    def TEST_STACK_NAME = 'craigt44'
    def PRODUCTION_STACK_IP = '35.161.244.46'
    def craigt42_InstanceID  //TEST_STACK_IP

    parameters {
        choice(
            // choices are a string of newline separated values
            // https://issues.jenkins-ci.org/browse/JENKINS-41180
            choices: 'greeting\nsilence',
            description: '',
            name: 'REQUESTED_ACTION')
    }

    stage('checkout') {
        checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/craigwongva/green']]]) 
    } 
    stage('buildTestInstanceAndApp') {
        when {
            // Only say hello if a "greeting" is requested
            expression { params.REQUESTED_ACTION == 'greeting' }
        }
        steps {
            echo "You said 'greeting' so I'll build a test instance"
        sh "aws cloudformation create-stack --stack-name ${TEST_STACK_NAME} --template-url https://s3.amazonaws.com/venicegeo-devops-dev-gocontainer-project/cf-nexus-java.json --region us-west-2 --parameters ParameterKey=nexususername,ParameterValue=unused ParameterKey=nexuspassword,ParameterValue=unused ParameterKey=tomcatmgrpassword,ParameterValue=unused"
        sh "sleep 60"

        def x = sh(script: "aws cloudformation describe-stacks --stack-name ${TEST_STACK_NAME} --region us-west-2", returnStdout: true)
        def temp = (x =~ /"OutputValue": "(.*)"/)
        craigt42_InstanceID = temp[0][1]
        }

        when {
            // Only say hello if silence is requested
            expression { params.REQUESTED_ACTION != 'greeting' }
        }
        steps {
            craigt42_InstanceID = params.REQUESTED_ACTION
        }
    }
    stage('triggerTestServices') {
	sh "sleep 1500"
	sh "cat invoke-phantom.js"
	sh "BUILD_ID=dontKillMe ./invoke-phantom $craigt42_InstanceID &"
	sh "cat invoke-phantom.js"
	sh "sleep 60"
    }
    stage('testAndInterpret') {
	//sleep(1000*60*2) why is this a day plus? Overridden Groovy sleep???
	def mickey = [
	 "curl",  
	  "$craigt42_InstanceID:8080/green/timer/status"]
	 .execute().text
	def ARBITRARY_SUCCESS_PCT = 0.95
	def NUM_GREEN_DOTS = 100
	def GREEN_DOT_STATUS_DONE = '4'

	//fails: if (mickey.indexOf(GREEN_DOT_STATUS_DONE.multiply(ARBITRARY_SUCCESS_PCT*NUM_GREEN_DOTS)) < 0) {
	//succeeds: if (mickey.indexOf('4'.multiply(95)) < 0) {

	//if (mickey.indexOf(GREEN_DOT_STATUS_DONE.multiply(ARBITRARY_SUCCESS_PCT*NUM_GREEN_DOTS)) < 0) {
	if (mickey.indexOf('4444444444444444444444444444444444444444444444444444444444') < 0) {
    	 error "red rover" 
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
        sh "aws cloudformation delete-stack --stack-name ${TEST_STACK_NAME}  --region us-west-2"
    }
}
