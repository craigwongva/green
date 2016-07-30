package green

import java.util.Random  

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

        for (int i=0; i<12; i++) {
            q[i] = new TestVector()
        }
        (1..2).each {
            for (int i=0; i<12; i++) {
                q[i].nextstep(randoms[i])
            }
        }
        render "eleven"
    }
/*
    def workOneTestCase(sleeptime) {
        sleep(1000*sleeptime)
        new TestVector(sleeptime)
    }
*/
    def url() { }

    def status() {
        def now = new Date()
        render now.format("yyyyMMdd-HH:mm:ss.SSS", TimeZone.getTimeZone('UTC')) + ' ' + q.id1 + q.id2
    }

    def external() {
        sleep(1000)
render(contentType: 'text/json') {[
    'results': results,
    'status': results ? "OK" : "Nothing present"
]}
    }
}

class TestVector {
    String id1
    String id2
    String id3
    String id4

    void nextstep(int n) {
        sleep(1000*n)
        if (id1) {
            id2 = 'something2'
        }
        if (id1 == null) {
            id1 = 'something'
        }
    }
}
