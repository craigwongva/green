package green

import java.util.Random  
import groovy.json.*

class TimerController {

    def NUM_COLORFUL_DISPLAY_DOTS = 16

    def q = new TestVector[NUM_COLORFUL_DISPLAY_DOTS]

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
        def NUM_ITERATIONS_TO_CALL_TEST_VECTOR = 200

        //s/m: this can be made more groovy, right? spread operator?
        for (int i=0; i<NUM_COLORFUL_DISPLAY_DOTS; i++) {
            q[i] = new TestVector()
        }

        (1..NUM_ITERATIONS_TO_CALL_TEST_VECTOR).each {
            if (it % 20 == 0) println "big loop $it"
            for (int i=0; i<NUM_COLORFUL_DISPLAY_DOTS; i++) {
                q[i].nextstep()
            }
        }
        render "C.work()999"
    }

    def url() { }

    String xxx() {
       String s = ''

       for (int i=0; i<NUM_COLORFUL_DISPLAY_DOTS; i++) {
            s += q[i]?.status()
       }
       s
    }

    def status() {
        String s = '<br>' +
            xxx() //+ ' ' + new Date().format("yyyyMMdd-HH:mm:ss.SSS", TimeZone.getTimeZone('UTC'))
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
