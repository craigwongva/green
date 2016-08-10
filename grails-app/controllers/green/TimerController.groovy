package green

import java.util.Random  
import groovy.json.*

class TimerController {
    def piazzaBox
    def externalUserService

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

        def myprocess2 = [ 'bash', '-c', "curl http://169.254.169.254/latest/meta-data/public-ipv4" ].execute()
        myprocess2.waitFor()
        String myprocess2AsText = myprocess2.text

        myprocess2AsText
    }

    def work() {
        qhealth[0] = new HealthArray()

        def NUM_ITERATIONS_TO_CALL_TEST_VECTOR = 3200

        //s/m: this can be made more groovy, right? spread operator?
        for (int i=0; i<NUM_COLORFUL_DISPLAY_DOTS; i++) {
            q[i] = new TestVector(piazzaBox, externalUserService)
        }

        (1..NUM_ITERATIONS_TO_CALL_TEST_VECTOR).each {
            if (it % 400 == 0) {
            }
            for (int i=0; i<NUM_COLORFUL_DISPLAY_DOTS; i++) {
                q[i].nextstep()
            }
        }
        render "C.work()999"
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
       s += (qhealth[0].port8079)? rand.nextInt(1111)  : 'A'
       s += (qhealth[0].port8081)? '1' : 'B'
       s += (qhealth[0].port8083)? '3' : 'C'
       s += (qhealth[0].port8084)? '4' : 'D'
       s += (qhealth[0].port8085)? '5' : 'E'
       s += (qhealth[0].port8088)? '8' : 'F'
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
    def port8079
    def port8081
    def port8083
    def port8084
    def port8085
    def port8088

    HealthArray() {
    port8079 = true
    port8081 = false
    port8083 = true
    port8084 = false
    port8085 = true
    port8088 = false
    }
    
}

class TestVector {
    def PIAZZA_PRIME_BOX 
    def EXTERNAL_USER_SERVICE //= 'http://prime.piazzageo.io:8078/green/timer/external'

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

        def result2AsJson = new JsonSlurper().parseText(myprocess2AsText)

        result2AsJson
    }

    def pz2() {
	assert id1
        def body3 = '{"type":"execute-service","data":{"serviceId":"REPLACEME","dataInputs":{},"dataOutput":[{"mimeType":"application/json","type":"text"}]}}'
        body3 = body3.replaceAll('REPLACEME', id1.data.serviceId)

        def myprocess3 = [ 'bash', '-c', "curl -v -k -X POST -H \"Content-Type: application/json\" -d '${body3}' http://$PIAZZA_PRIME_BOX:8081/job" ].execute()
        String myprocess3AsText =  myprocess3.text

        def result3AsJson = new JsonSlurper().parseText(myprocess3AsText)

        result3AsJson
    }

    def pz3() {
        def returnval

	assert id2
        def myprocess4 = [ 'bash', '-c', "curl -v -k -X GET -H \"Content-Type: application/json\" http://$PIAZZA_PRIME_BOX:8081/job/${id2.data.jobId}" ].execute()
        def myprocess4AsText = myprocess4.text
        def result4AsJson = new JsonSlurper().parseText(myprocess4AsText)
        try {
            result4AsJson.data.result.dataId
            returnval = result4AsJson
        }
        catch(e) {
            returnval = null
        }
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
