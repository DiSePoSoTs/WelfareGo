package it.wego.welfarego.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * @author aleph
 */
public class WelfaregoScheduler {

    private static final WelfaregoScheduler WELFAREGO_SCHEDULER = new WelfaregoScheduler();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();


    private WelfaregoScheduler() {
    }

    public static WelfaregoScheduler getInstance() {
        return WELFAREGO_SCHEDULER;
    }

    public void start() {
        logger.info("\n\n\n\n____start InterventiScheduler");
        EXECUTOR_SERVICE.scheduleAtFixedRate(new InterventiScheduler(), 1, 1, TimeUnit.HOURS);
    }


    public void stop() {
        EXECUTOR_SERVICE.shutdownNow();
        logger.info("\n\n\n\n____stop InterventiScheduler");
    }

}


