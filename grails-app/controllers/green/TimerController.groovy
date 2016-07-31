package green

import java.util.Random  
import groovy.json.*

class TimerController {


    def q = new Object[12]

    def index() { }

    def randomNumbers() {
        Random rand = new Random()  
        int max = 4
        def randomIntegerList = []  
        (1..12).each {  
            randomIntegerList << rand.nextInt(max+1)  
        }  
        randomIntegerList
    }

    def work() {
        def randoms = randomNumbers()
        def NUM_COLORFUL_DISPLAY_DOTS = 1
        for (int i=0; i<NUM_COLORFUL_DISPLAY_DOTS; i++) {
            q[i] = new TestVector()
        }
        (1..4).each {
            println "work() big loop $it"
            for (int i=0; i<NUM_COLORFUL_DISPLAY_DOTS; i++) {
                println "work() i is $i"
                q[i].nextstep()
            }
        }
        render "C.work()999"
    }

    def url() { }

    def status() {
        def now = new Date()
        render now.format("yyyyMMdd-HH:mm:ss.SSS", TimeZone.getTimeZone('UTC')) + ' ' + q.id1 + q.id2
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

    void nextstep() {
        if ((id4 == null) && id3) {
            println "TestVector.nextstep() will execute pz4()"
            id4 = pz4()
            println "pz4() resultm is $id4"
        }
        if ((id3 == null) && id2) {
            println "TestVector.nextstep() will execute pz3, pz4()"
            id3 = pz3()
            id4 = pz4()
            println "pz4() resultn is $id4"
        }
        if ((id2 == null) && id1) {
            println "TestVector.nextstep() will execute pz2, pz3, pz4()"
            id2 = pz2(); sleep(5000)
            id3 = pz3()
            id4 = pz4()
            println "pz4() resulto is $id4"
        }
        if (id1 == null) {
            println "TestVector.nextstep() will execute pz1, pz2, pz3, pz4()"
            id1 = pz1()
            id2 = pz2(); sleep(5000)
            id3 = pz3()
            id4 = pz4()
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
        println "pz3() is starting"
	assert id2
        println "id2 is $id2"
        def myprocess4 = [ 'bash', '-c', "curl -v -k -X GET -H \"Content-Type: application/json\" http://$PIAZZA_PRIME_BOX:8081/job/${id2.data.jobId}" ].execute()
        String myprocess4AsText =  myprocess4.text
        def result4AsJson = new JsonSlurper().parseText(myprocess4AsText)

        result4AsJson
    }

    def pz4() {
        println "pz4() is starting"
	assert id3
        println "id3 is $id3"
        if (id3?.data) {
            def myprocess5 = [ 'bash', '-c', "curl -v -k -X GET -H \"Content-Type: application/json\" http://$PIAZZA_PRIME_BOX:8081/data/${id3.data.result.dataId}" ].execute()
            String myprocess5AsText = myprocess5.text
            myprocess5AsText
        }
    }
}
