package it.wego.welfarego.azione.utils;

import com.google.common.base.Objects;
import it.wego.extjs.json.JsonBuilder;
import it.wego.persistence.PersistenceAdapterFactory;
import it.wego.persistence.PersistenceAdapter;
import it.wego.unique.intalio.completeTask.Output;
import it.wego.unique.intalio.processmanagement.TInstanceInfo;
import  it.wego.unique.intalio.IntalioManager;
import it.wego.welfarego.intalio.WelfareGoIntalioManager;
import it.wego.welfarego.persistence.dao.TaskDao;
import it.wego.welfarego.persistence.entities.UniqueTasklist;
import it.wego.welfarego.utils.WelfaregoUtils;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.poi.ss.formula.IStabilityClassifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author aleph
 */
public class IntalioAdapter {
    
    private static ExecutorService executorService = Executors.newSingleThreadExecutor(new ThreadFactory() {
        public Thread newThread(Runnable r) {
            return new Thread(r, "IntalioAdapter");
        }
    });
    private static final Logger logger = LoggerFactory.getLogger(IntalioAdapter.class);
//    private static Thread thread = null;
    private static final Object lock = new Object();
    private static boolean isRunning = false;
//    private static int threadCounter = 0;

    public static void executeJob() {
        synchronized (lock) {
            if (!isRunning) {
                executorService.submit(job);
            }
//            if (thread == null || !thread.isAlive()) {
//                thread = new Thread(job, "IntalioAdapter-" + (threadCounter = ((threadCounter + 1) % 100)));
//                thread.start();
//            }
        }
    }
    
    public static void shutdown() {
//        if (thread != null && thread.isAlive()) {
//            thread.stop();
//        }
        executorService.shutdownNow();
    }
    private static Runnable job = new Runnable() {
        public void run() {
            logger.debug("starting job");
            try {
                PersistenceAdapter persistenceAdapter = PersistenceAdapterFactory.getPersistenceAdapter();
                TaskDao taskDao = new TaskDao(persistenceAdapter.getEntityManager());
                WelfareGoIntalioManager welfareGoIntalioManager = new WelfareGoIntalioManager();
                welfareGoIntalioManager.setProperty(IntalioManager.AUTHENTICATION_TOKEN, WelfaregoUtils.getConfig(IntalioManager.AUTHENTICATION_TOKEN));
                welfareGoIntalioManager.setProperty(IntalioManager.AUTHENTICATION_URL, WelfaregoUtils.getConfig("PROPERTY_AUTHENTICATION_URL"));
                welfareGoIntalioManager.setProperty(IntalioManager.TMS_URL, WelfaregoUtils.getConfig("PROPERTY_TMS_URL"));
                welfareGoIntalioManager.setProperty(IntalioManager.COMPLETE_TASK_URL, WelfaregoUtils.getConfig("PROPERTY_COMPLETE_TASK_URL"));
                //FIX ME             
              //  welfareGoIntalioManager.setProperty(IntalioManager.INTALIO_URL,WelfaregoUtils.getConfig("PROPERTY_INTALIO_URL") );
                welfareGoIntalioManager.setProperty(WelfareGoIntalioManager.START_PROCESS_URL, WelfaregoUtils.getConfig("PROPERTY_START_PROCESS_URL"));
                List<UniqueTasklist> scheduledTask;
                while (true) {
                    synchronized (lock) {
                        if ((scheduledTask = taskDao.findScheduledTask()).isEmpty()) {
                            logger.debug("stopping job");
                            isRunning = false;
//                            thread = null;
                            persistenceAdapter.close();
                            return;
                        }
                    }
                    for (UniqueTasklist task : scheduledTask) {
                        Output output = null;
                        try {
                            logger.debug("scheduling task : '" + task.getTaskid() + "'");
                            persistenceAdapter.initTransaction();
                            output = new Output();
                            output.setCampoForm1(task.getCampoForm1());
                            output.setCampoForm2(task.getCampoForm2());
                            output.setCampoForm3(task.getCampoForm3());
                            output.setCampoForm4(task.getCampoForm4());
                            output.setCampoForm5(task.getCampoForm5());
                            output.setCampoForm6(task.getCampoForm6());
                            output.setCampoForm7(task.getCampoForm7());
                            output.setCampoForm8(task.getCampoForm8());
                            output.setEsito(task.getEsito());
                            if (task.getTaskid() == null || task.getTaskid().length() < 10) {
                                logger.debug("task id too short, treating as dummy -> de-queuing task : '" + task.getTaskid() + "' , " + JsonBuilder.getGsonPrettyPrinting().toJson(output));
                                task.setFlgEseguito(TaskDao.FLAG_NO);
                                persistenceAdapter.commitTransaction();
                            } else if (Objects.equal(UniqueTasklist.STANDALONE_TASK_ID, task.getTaskid())) {
                                logger.debug("standalone task, dequeuing and marking as done task = {}", task.getId());
                                task.setFlgEseguito(TaskDao.FLAG_SI);
                                persistenceAdapter.commitTransaction();
                            }/* else if (Objects.equal(task.getEsito(), "DELETE")){
                            	logger.info("Attenzione provo a cancellare un task id da intalio");
                            //	welfareGoIntalioManager.terminateTaskAndProcessInstance(task.getTaskid());
                                 TInstanceInfo instanceInfo =  welfareGoIntalioManager.getInstanceByTaskId(task.getTaskid());
                            	welfareGoIntalioManager.getInstanceManagementService().terminate(Long.valueOf(instanceInfo.getIid()).longValue());
                            	taskDao.delete(task);
                            	
                            	persistenceAdapter.commitTransaction();
                            }*/
                            else {
                                String rootNs = "http://www.intalio.com/bpms/workflow/ib4p_20051115";
                                String newNs = convertNs(task.getCampoFlow8());
                                logger.debug("sending task to Intalio : '" + task.getTaskid() + "'");
                            //intalio toltoooooooo
                                //    welfareGoIntalioManager.completeTask(task.getTaskid(), output, new SoapHelper(rootNs, newNs));
                                logger.debug("Intalio completed task : '" + task.getTaskid() + "'");
                                task.setFlgEseguito(TaskDao.FLAG_SI);
                                persistenceAdapter.commitTransaction();
                                logger.debug("task completed : '" + task.getTaskid() + "'");
                            }
                        } catch (Exception e) {
                            if (e instanceof InterruptedException) {
                                persistenceAdapter.rollbackTransaction();
                                throw e;
                            }
                            if (e instanceof SOAPFaultException && e.getMessage().matches(".*Timeout.*waiting.*MEX.*")) {
                                logger.warn("timeout while executing job", e);
                                logger.warn("sent data : " + JsonBuilder.getGsonPrettyPrinting().toJson(output));
                                task.setFlgEseguito(TaskDao.FLAG_SI);
                                persistenceAdapter.commitTransaction();
                                logger.debug("task completed with timeout : '" + task.getTaskid() + "'");
                            } else {
                                logger.error("error while executing job", e);
                                logger.warn("sent data : " + JsonBuilder.getGsonPrettyPrinting().toJson(output));
                                persistenceAdapter.rollbackTransaction();
                            }
                            Thread.sleep(1000);// sleep 1s, avoid fast loop around runtime error
                        }
                    }
                }
            } catch (Exception ex) {
                logger.error("exception while executing job", ex);
                synchronized (lock) {
                    isRunning = false;
                    return;
                }
            }
        }
    };
    
    public static String convertNs(String campoFlow) {
        return campoFlow.replaceFirst("^oxf://WelfareGO/(.+)[.]xform$", "http://example.com/$1/xform");
    }
    
    public static class SoapHelper implements SOAPHandler<SOAPMessageContext> {
        
        private String rootNs, newNs;
        
        public SoapHelper(String rootNs, String newNs) {
            this.rootNs = rootNs;
            this.newNs = newNs;
        }
        private static Logger logger = LoggerFactory.getLogger(SoapHelper.class);
        
        public Set<QName> getHeaders() {
            return null;
        }
        
        public boolean handleMessage(SOAPMessageContext smc) {
            String messageStr = null;
            try {
                Boolean outboundProperty = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
                if (!outboundProperty) {
                    logger.debug("soap helper catch inbound message, skipping");
                    return true;
                }
                
                SOAPMessage message = smc.getMessage();
                logger.debug("soap helper handle message, adding ns : " + newNs);
                
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                message.writeTo(out);
                messageStr = out.toString();
                
                SOAPBody body = message.getSOAPBody();
                SOAPBodyElement completeTaskRequest = (SOAPBodyElement) body.getChildElements(new QName(rootNs, "completeTaskRequest")).next();
                SOAPElement taskOutput = (SOAPElement) completeTaskRequest.getChildElements(new QName(rootNs, "taskOutput")).next();
                SOAPElement output = (SOAPElement) taskOutput.getChildElements(new QName(rootNs, "output")).next();
                
                Stack<SOAPElement> stack = new Stack<SOAPElement>();
                stack.push(output);
                while (!stack.isEmpty()) {
                    SOAPElement soapElement = stack.pop();
                    Iterator childElements = soapElement.getChildElements();
                    while (childElements.hasNext()) {
                        Object obj = childElements.next();
                        if (obj instanceof SOAPElement) {
                            stack.push((SOAPElement) obj);
                        }
                    }
                    QName name = soapElement.getElementQName();
                    soapElement.setElementQName(new QName(newNs, name.getLocalPart()));
                }
                
                smc.setMessage(message);
                logger.debug("soap helper handled message");
            } catch (Throwable ex) {
                logger.error("error while handling soap message", ex);
                if (messageStr != null) {
                    logger.error("original message : " + messageStr);
                }
            }
            return true;
        }
        
        public boolean handleFault(SOAPMessageContext smc) {
            return true;
        }
        
        public void close(MessageContext messageContext) {
        }
    }
}
