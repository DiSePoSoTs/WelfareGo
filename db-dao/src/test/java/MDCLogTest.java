import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.testng.annotations.Test;

public class MDCLogTest {


//    @Test
    public void aa() {
        String uno = "";
        Logger logger = LoggerFactory.getLogger(HelloThread.class);
        MDC.put("userid", "admin2");

        for (int i = 0; i < 20; i++) {
            String s = "asdfasdf";
            for (int j = 0; j < 20; j++) {
                s = s + s;
            }
            logger.info(uno + " log x admin2 1" + s);
        }
        MDC.remove("userid");

        for (int i = 0; i < 20; i++) {
            String s = "asdfasdf";
            for (int j = 0; j < 20; j++) {
                s = s + s;
            }
            logger.info(uno + " log x altri 1" + s);
        }
    }

    @Test
    public void bb() {
        int i = 10;
        while (i > 0) {
            HelloThread uno = new HelloThread(i);
            uno.run();
            i--;
        }
    }


    class HelloThread extends Thread {

        int i;

        public HelloThread(int i) {
            this.setName("_" + i + "_");
            this.i = i;
            System.out.println(i);
        }


        public void run() {
            Logger logger = LoggerFactory.getLogger(HelloThread.class);
            if (i % 2 == 0) {
                MDC.put("userid", "admin2");
                for (int j = 0; j < 3; j++) {
                    logger.info(i + " log x admin2 1");
                }
                MDC.remove("userid");
            } else {
                for (int k = 0; k < 3; k++) {
                    logger.info(i + " log x altri 1");
                }
            }

        }
    }
}
