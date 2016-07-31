package green

import java.util.Random  
import groovy.json.*

class TimerController {


    def q = new TestVector[12]

    def index() { }

    def randomNumbers() {
        Random rand = new Random()  
        int max = 8
        def randomIntegerList = []  
        (1..12).each {  
            randomIntegerList << rand.nextInt(max+1)  
        }  
        randomIntegerList
    }

    def work() {
        def NUM_COLORFUL_DISPLAY_DOTS = 2
        def NUM_ITERATIONS_TO_CALL_TEST_VECTOR = 10

        //s/m: this can be made more groovy, right? spread operator?
        for (int i=0; i<NUM_COLORFUL_DISPLAY_DOTS; i++) {
            q[i] = new TestVector()
        }

        (1..NUM_ITERATIONS_TO_CALL_TEST_VECTOR).each {
            println "work() big loop $it"
            for (int i=0; i<NUM_COLORFUL_DISPLAY_DOTS; i++) {
                println "work() i is $i"
                q[i].nextstep()
            }
            sleep(1000)
        }
        render "C.work()999"
    }

    def url() { }

    def status() {
        String s = '<br>' +
            new Date().format("yyyyMMdd-HH:mm:ss.SSS", TimeZone.getTimeZone('UTC')) + '<br>' +
            q[0]?.status() + '<br>' +
            q[1]?.status() 
        render s
    }

    def external() {
        def randoms = randomNumbers()
        sleep(randoms[0]*1000)
        render(contentType: 'text/json') {[
            'results': results,
            'status': results ? "OK" : "Nothing present"
        ]}
    }
}

class TestVector {
    static def PIAZZA_PRIME_BOX = '52.42.114.223'
    static def EXTERNAL_USER_SERVICE = 'http://52.42.147.229:8080/green/timer/external'

    def id1
    def id2
    def id3
    def id4
    def id5

    void nextstep() {
        if ((id5 == null) && id4) {
            id5 = pz5()
            println "pz5() resultm is $id5"
        }
        if ((id4 == null) && id3) {
            println "TestVector.nextstep() will execute pz4()"
            id4 = pz4()
            println "pz4() resultm is $id4"
        }
        if ((id3 == null) && id2) {
            println "TestVector.nextstep() will execute pz3, pz4()"
            id3 = pz3()
            if (id3) id4 = pz4()
            println "pz4() resultn is $id4"
        }
        if ((id2 == null) && id1) {
            println "TestVector.nextstep() will execute pz2, pz3, pz4()"
            id2 = pz2()//; sleep(5000)
            if (id2) id3 = pz3()
            if (id3) id4 = pz4()
            println "pz4() resulto is $id4"
        }
        if (id1 == null) {
            println "TestVector.nextstep() will execute pz1, pz2, pz3, pz4()"
            id1 = pz1()
            if (id1) id2 = pz2()//;  sleep(5000)
            if (id2) id3 = pz3()
            if (id3)  id4 = pz4()
            println "pz4() resultp is $id4"
        }
    }

    def pz1() {
        println "pz1() is starting"
        def body2 = '{"url":"REPLACEME","method":"GET","contractUrl":"REPLACEME/","resourceMetadata":{"name":"Hello World Test","description":"Hello world test","classType":{"classification":"UNCLASSIFIED"}}}'
        body2 = body2.replaceAll('REPLACEME', EXTERNAL_USER_SERVICE)

        def myprocess2 = [ 'bash', '-c', "curl -v -k -X POST -H \"Content-Type: application/json\" -d '${body2}' http://$PIAZZA_PRIME_BOX:8081/service" ].execute()
        myprocess2.waitFor()
        String myprocess2AsText =  myprocess2.text

        def result2AsJson = new JsonSlurper().parseText(myprocess2AsText)

        result2AsJson
    }

    def pz2() {
        println "pz2() is starting"
	assert id1
        println "id1 is $id1"
        def body3 = '{"type":"execute-service","data":{"serviceId":"REPLACEME","dataInputs":{},"dataOutput":[{"mimeType":"application/json","type":"text"}]}}'
        body3 = body3.replaceAll('REPLACEME', id1.data.serviceId)

        def myprocess3 = [ 'bash', '-c', "curl -v -k -X POST -H \"Content-Type: application/json\" -d '${body3}' http://$PIAZZA_PRIME_BOX:8081/job" ].execute()
        String myprocess3AsText =  myprocess3.text

        def result3AsJson = new JsonSlurper().parseText(myprocess3AsText)

        result3AsJson
    }

    def pz3() {
        def returnval

        println "pz3() is starting"
	assert id2
        println "id2 is $id2"
        def myprocess4 = [ 'bash', '-c', "curl -v -k -X GET -H \"Content-Type: application/json\" http://$PIAZZA_PRIME_BOX:8081/job/${id2.data.jobId}" ].execute()
        def myprocess4AsText = myprocess4.text
        def result4AsJson = new JsonSlurper().parseText(myprocess4AsText)
        //println "pz3 result4AsJson.getClass() is ${result4AsJson.getClass()}"
        //println "pz3 result4AsJson is $result4AsJson"
        //println 'pz3l ' + result4AsJson.data
        //println 'pz3m ' + result4AsJson.data.result
        try {
            result4AsJson.data.result.dataId
            returnval = result4AsJson
        }
        catch(e) {
            returnval = null
        }
        println "pz3 returnval is $returnval"
        returnval
    }

    def pz4() {
        println "pz4() is starting"
	assert id3
        println "id3 is $id3"
        if (id3?.data) {
            def myprocess5 = [ 'bash', '-c', "curl -v -k -X GET -H \"Content-Type: application/json\" http://$PIAZZA_PRIME_BOX:8081/data/${id3.data.result.dataId}" ].execute()
            String myprocess5AsText = myprocess5.text
            def result5AsJson = new JsonSlurper().parseText(myprocess5AsText)

            result5AsJson
        }
    }

    def pz5() {
        assert id4
        println "pz5: id4.getClass() is " + id4.getClass()
        println "pz5: id4 is $id4"
        id4?.data?.dataType?.content
    }

    String status() {
     (id1? '1': '0') + (id2? '2': '0') + (id3? '3': '0') + (id4? '4': '0')
    }
}
