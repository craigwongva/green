package green

import java.util.Random  
import groovy.json.*

class TimerController {
    def piazzaBox
    def externalUserService
    def workers = 0

    def NUM_COLORFUL_DISPLAY_DOTS = 100

    def q = new TestVector[NUM_COLORFUL_DISPLAY_DOTS]
    def qhealth = new HealthArray[1]

    def index() { }

    def randomNumbers() {
        Random rand = new Random()  
        int max = 16
        def randomIntegerList = []  
        (1..12).each {  
            randomIntegerList << rand.nextInt(max+1)  
        }  
        randomIntegerList
    }

    def myIP() {
        def myprocess = [ 'bash', '-c', "curl http://169.254.169.254/latest/meta-data/public-ipv4" ].execute()
        myprocess.waitFor()
        String myprocessAsText = myprocess.text

        myprocessAsText
    }

    def work() {

        //work() is invoked from the gsp page each time the browser is refreshed.
        //we don't want multiple workers, so each worker gets a worker number.
        //if the worker number doesn't match the number of workers, then
        //the worker exits.
        def iamworker = ++workers

        //s/m: this can be made more groovy, right? spread operator?
        for (int i=0; i<NUM_COLORFUL_DISPLAY_DOTS; i++) {
            q[i] = new TestVector(piazzaBox, externalUserService)
        }

        //This is an arbitrarily large number. We might as well loop forever.
        def MAX_ITERATION_TO_CALL_TEST_VECTOR = 64000

        (0..MAX_ITERATION_TO_CALL_TEST_VECTOR).each {
            if (iamworker == workers) {
                def HEALTH_CHECK_SERVICES_EVERY_SO_OFTEN = 10
                if (it % HEALTH_CHECK_SERVICES_EVERY_SO_OFTEN == 0) {
                    qhealth[0] = new HealthArray()
                }
                for (int i=0; i<NUM_COLORFUL_DISPLAY_DOTS; i++) {
                    q[i].nextstep()
                }
            }
        }
        render "work()#$iamworker exits (num workers is $workers)"
    }

    def dots() { 
        piazzaBox = (params.containers) ?: myIP()
        externalUserService = myIP()
    }

    String stringOfDotStatusEachRepresentsAPiazzaJob() {
       String s = ''

       for (int i=0; i<NUM_COLORFUL_DISPLAY_DOTS; i++) {
            s += q[i]?.status()
       }
       s
    }

    String stringOfDotDurationEachRepresentsAPiazzaJob() {
       String s = ''
       for (int i=0; i<NUM_COLORFUL_DISPLAY_DOTS; i++) {

            if (q[i]?.id5) {
                def cat = new JsonSlurper().parseText(q[i].id5)
                int durationAsInteger = cat.results
                def temp
                if (durationAsInteger <= 16) temp = '3'
                if (durationAsInteger <= 12) temp = '2'
                if (durationAsInteger <= 8) temp = '1'
                if (durationAsInteger <= 4) temp = '0'
                s += temp
            }
            else {
                s += '5'
            }
       }
       s
    }

    String stringOfSquareHealthEachRepresentsAContainerOrProcess() {
        Random rand = new Random()  
       
       String s = ''

       if (qhealth[0]) {
          //For healthy services, return a random number (because their
          //continual updating indicates that monitoring activity is
          //taking place). That eliminates the need to create a fancy
          //page GUI design.
          s += (qhealth[0].port8079)? rand.nextInt(1111) : 'nexus?'
          s += ','
          s += (qhealth[0].port8081)? rand.nextInt(1111) : 'gateway?'
          s += ','
          s += (qhealth[0].port8083)? rand.nextInt(1111) : 'jobmanager?'
          s += ','
          s += (qhealth[0].port8084)? rand.nextInt(1111) : 'ingest?'
          s += ','
          s += (qhealth[0].port8085)? rand.nextInt(1111) : 'access?'
          s += ','
          s += (qhealth[0].port8088)? rand.nextInt(1111) : 'servicecontroller?'
       }
       s
    }

    def status() {
        render(contentType: 'text/json') {[
            'dotStatus': stringOfDotStatusEachRepresentsAPiazzaJob(),
            'dotDuration': stringOfDotDurationEachRepresentsAPiazzaJob(),
            'squareHealth': stringOfSquareHealthEachRepresentsAContainerOrProcess()
        ]}
    }

    def external() {
        def randoms = randomNumbers()
        sleep(randoms[0]*1000)
        render(contentType: 'application/json') {[
            'results': randoms[0],
            'status': results ? "OK" : "Nothing present"
        ]}
    }
}

class HealthArray {
    def myip
    def port8079
    def port8081
    def port8083
    def port8084
    def port8085
    def port8088

    HealthArray() {
       myip = myIP()
       port8079 = test8079()
       port8081 = test8081()
       port8083 = test8083()
       port8084 = test8084()
       port8085 = test8085()
       port8088 = test8088()
    }
    
    def myIP() {
        def myprocess = [ 'bash', '-c', "curl http://169.254.169.254/latest/meta-data/public-ipv4" ].execute()
        myprocess.waitFor()
        String myprocessAsText = myprocess.text

        myprocessAsText
    }

    boolean test8079() {
        def myprocess = [ 'bash', '-c', "curl --max-time 3 http://$myip:8079" ].execute()
        myprocess.waitFor()
        String myprocessAsText = myprocess.text
        myprocessAsText.indexOf('Nexus') > 0
    }

    boolean test8081() {
        def myprocess = [ 'bash', '-c', "curl --max-time 3 http://$myip:8081" ].execute()
        myprocess.waitFor()
        String myprocessAsText = myprocess.text
        myprocessAsText.indexOf('pz-gateway') > 0
    }

    boolean test8083() {
        def myprocess = [ 'bash', '-c', "curl --max-time 3 http://$myip:8083" ].execute()
        myprocess.waitFor()
        String myprocessAsText = myprocess.text
        myprocessAsText.indexOf('pz-jobmanager') > 0
    }

    boolean test8084() {
        def myprocess = [ 'bash', '-c', "curl --max-time 3 http://$myip:8084" ].execute()
        myprocess.waitFor()
        String myprocessAsText = myprocess.text
        myprocessAsText.indexOf('Loader') > 0
    }

    boolean test8085() {
        def myprocess = [ 'bash', '-c', "curl --max-time 3 http://$myip:8085" ].execute()
        myprocess.waitFor()
        String myprocessAsText = myprocess.text
        myprocessAsText.indexOf('pz-access') > 0
    }

    boolean test8088() {
        def myprocess = [ 'bash', '-c', "curl --max-time 3 http://$myip:8088" ].execute()
        myprocess.waitFor()
        String myprocessAsText = myprocess.text
        myprocessAsText.indexOf('Piazza Service Controller') > 0
    }
}

class TestVector {
    def PIAZZA_PRIME_BOX 
    def EXTERNAL_USER_SERVICE

    def id1
    def id2
    def id3
    def id4
    def id5

    TestVector(dummy1, dummy2) {
        EXTERNAL_USER_SERVICE = "http://$dummy2:8078/green/timer/external"
        PIAZZA_PRIME_BOX = dummy1
    }

    void nextstep() {
        if ((id5 == null) && id4) {
            id5 = pz5()
        }
        if ((id4 == null) && id3) {
            id4 = pz4()
        }
        if ((id3 == null) && id2) {
            id3 = pz3()
            if (id3) id4 = pz4()
        }
        if ((id2 == null) && id1) {
            id2 = pz2()
            if (id2) id3 = pz3()
            if (id3) id4 = pz4()
        }
        if (id1 == null) {
            id1 = pz1()
            if (id1) id2 = pz2()
            if (id2) id3 = pz3()
            if (id3) id4 = pz4()
        }
    }

    def pz1() {
        def body2 = '{"url":"REPLACEME","method":"GET","contractUrl":"REPLACEME/","resourceMetadata":{"name":"Hello World Test","description":"Hello world test","classType":{"classification":"UNCLASSIFIED"}}}'
        body2 = body2.replaceAll('REPLACEME', EXTERNAL_USER_SERVICE)

        def myprocess2 = [ 'bash', '-c', "curl -v -k -X POST -H \"Content-Type: application/json\" -d '${body2}' http://$PIAZZA_PRIME_BOX:8081/service" ].execute()
        myprocess2.waitFor()
        String myprocess2AsText =  myprocess2.text

        def result2AsJson = null
        try { result2AsJson = new JsonSlurper().parseText(myprocess2AsText) } catch(e) {}

        result2AsJson
    }

    def pz2() {
	assert id1

        def result3AsJson = null
        def body3 = '{"type":"execute-service","data":{"serviceId":"REPLACEME","dataInputs":{},"dataOutput":[{"mimeType":"application/json","type":"text"}]}}'
        body3 = body3.replaceAll('REPLACEME', id1.data.serviceId)

        def myprocess3 = [ 'bash', '-c', "curl -v -k -X POST -H \"Content-Type: application/json\" -d '${body3}' http://$PIAZZA_PRIME_BOX:8081/job" ].execute()

        try {
           String myprocess3AsText =  myprocess3.text

           result3AsJson = new JsonSlurper().parseText(myprocess3AsText) 
        } catch(e) {}

        result3AsJson
    }

    def pz3() {
	assert id2

        def returnval = null
        def myprocess4 = [ 'bash', '-c', "curl -v -k -X GET -H \"Content-Type: application/json\" http://$PIAZZA_PRIME_BOX:8081/job/${id2?.data?.jobId}" ].execute()
        def myprocess4AsText
        try {
            myprocess4AsText = myprocess4.text
            def result4AsJson = new JsonSlurper().parseText(myprocess4AsText)
            result4AsJson.data.result.dataId
            returnval = result4AsJson
        } catch(e) {}
        returnval
    }

    def pz4() {
	assert id3
        if (id3?.data) {
            def myprocess5 = [ 'bash', '-c', "curl -v -k -X GET -H \"Content-Type: application/json\" http://$PIAZZA_PRIME_BOX:8081/data/${id3.data.result.dataId}" ].execute()
            String myprocess5AsText = myprocess5.text
            def result5AsJson = new JsonSlurper().parseText(myprocess5AsText)
            id5 = result5AsJson?.data?.dataType?.content //this eliminates the need for an extra iteration to populate id5
            result5AsJson
        }
    }

    def pz5() {
        assert id4
        id4?.data?.dataType?.content
    }

    String status() {
     id4? 4 : (id3? 3 : (id2? 2 : (id1? 1 : 0)))
    }
}
