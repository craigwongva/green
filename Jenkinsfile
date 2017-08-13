node { 
    def mvn = tool 'M3.0.5' 
    def TEST_STACK_NAME = 'craigu01'
    def test_stack_status = 'build'
    def TEST_STACK_IP
    def PRODUCTION_STACK_IP = '52.42.108.124'

   properties([
     parameters([
       string(
         defaultValue: '34.211.136.17',
         description: 'Test Stack IP',
         name: 'TEST_STACK_IP'
       ),
       string(
         defaultValue: '34.213.55.117',
         description: 'Production Stack IP',
         name: 'PRODUCTION_STACK_IP'
       ),
     ])
   ])

    stage('checkout') {
        checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/craigwongva/green']]]) 
    } 

    stage('describeTestInstance') {
            test_stack_status = 'build'
            def x = sh(script: "aws cloudformation describe-stacks --stack-name ${TEST_STACK_NAME} --region us-west-2", returnStdout: true)
            def temp = (x =~ /"OutputValue": "(.*)"/)
            TEST_STACK_IP = temp[0][1]
	    //sh "sleep 1500": this statement seems to cause a Jenkins failure
    }
    stage('buildApp') { 
        sh "/usr/local/apache-maven/bin/mvn clean -DskipTests install"
    } 
    stage('deployAppToTest') { 
        sh "whoami" 
        sh "pwd" 
        def s = "scp -i /home/jenkins/craigradiantblueoregon.pem -o StrictHostKeyChecking=no /var/lib/jenkins/.m2/repository/com/demo/green/1.0-SNAPSHOT/green-1.0-SNAPSHOT.war ec2-user@${TEST_STACK_IP}:/usr/share/tomcat7/webapps/green.war" 
        println "s is s"
        sh s
    }

/*
    stage('waitThenInvokePhantomOnApp') {
        println "---waitThenInvokePhantomOnApp---"
	sh "cat invoke-phantom.js"
        sh "whereis phantomjs"
	sh "BUILD_ID=dontKillMe ./invoke-phantom ${TEST_STACK_IP} &"
	sh "cat invoke-phantom.js"
        sh "ps -ef | grep phantom"
	sh "sleep 20"
        sh "ps -ef | grep phantom"
    }

    stage('curlAndInterpretAppStatus') {
        println "---curlAndInterpretAppStatus---"
	//sleep(1000*60*2) why is this a day plus? Overridden Groovy sleep???
	def mickey = [
	 "curl",  
	  "${TEST_STACK_IP}:8080/green/timer/status"]
	 .execute().text
	def ARBITRARY_SUCCESS_PCT = 0.95
	def NUM_GREEN_DOTS = 100
	def GREEN_DOT_STATUS_DONE = '4'

	//fails: if (mickey.indexOf(GREEN_DOT_STATUS_DONE.multiply(ARBITRARY_SUCCESS_PCT*NUM_GREEN_DOTS)) < 0) leftsquigglybracket
	//succeeds: if (mickey.indexOf('4'.multiply(95)) < 0) leftsquigglybracket

	//if (mickey.indexOf(GREEN_DOT_STATUS_DONE.multiply(ARBITRARY_SUCCESS_PCT*NUM_GREEN_DOTS)) < 0) leftsquigglybracket
	if (mickey.indexOf('4444444444444444444444444444444444444444444444444444444444') < 0) {
    	 error "red rover3 http://${TEST_STACK_IP}:8080/green/timer/status $mickey" 
	}
	//somehow next shell this: pkill -f phantomjs
    }
    stage('rebuildApp') { 
        //No need to rebuild, actually, so comment this out:
        //sh "/usr/local/apache-maven/bin/mvn clean -DskipTests install"
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
*/
}
