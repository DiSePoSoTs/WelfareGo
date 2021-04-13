package it.wego.welfarego.bre.utils;


import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import it.wego.welfarego.persistence.entities.PaiIntervento;
import it.wego.welfarego.utils.WelfaregoUtils;
import java.util.*;
import java.util.Map.Entry;
import javax.persistence.EntityManager;

import org.drools.agent.KnowledgeAgent;
import org.drools.agent.KnowledgeAgentFactory;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.event.knowledgeagent.AfterChangeSetAppliedEvent;
import org.drools.event.knowledgeagent.AfterChangeSetProcessedEvent;
import org.drools.event.knowledgeagent.AfterResourceProcessedEvent;
import org.drools.event.knowledgeagent.BeforeChangeSetAppliedEvent;
import org.drools.event.knowledgeagent.BeforeChangeSetProcessedEvent;
import org.drools.event.knowledgeagent.BeforeResourceProcessedEvent;
import org.drools.event.knowledgeagent.KnowledgeAgentEventListener;
import org.drools.event.knowledgeagent.KnowledgeBaseUpdatedEvent;
import org.drools.event.knowledgeagent.ResourceCompilationFailedEvent;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatelessKnowledgeSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author aleph
 */
public class BreUtils {

    public static final String DROOLS_CONFIG_PARAM = "it.wego.welfarego.bre.DroolsConfig";
    private static final Logger logger = LoggerFactory.getLogger(BreUtils.class);

    public static void startServices() {
        logger.debug("starting drools services");
        try {
            getDroolsKnowledgeAgent();
        } catch (Exception ex) {
            logger.error("error while initializing drools knowledge agent", ex);
        }
        
        ResourceFactory.getResourceChangeScannerService().setInterval(10);
        ResourceFactory.getResourceChangeScannerService().start();
        ResourceFactory.getResourceChangeNotifierService().start();
        logger.debug("started drools services");
    }

    public static void stopServices() {
        logger.debug("stopping drools services");
        ResourceFactory.getResourceChangeNotifierService().stop();
        ResourceFactory.getResourceChangeScannerService().stop();
        logger.debug("stopped drools services");
    }

    public static void reloadAll() {
        stopServices();
        knowledgeAgent = null;
        startServices();
    }
    
    private static KnowledgeAgent knowledgeAgent = null;

    public static KnowledgeAgent getDroolsKnowledgeAgent() throws Exception {
        if (knowledgeAgent == null) {
            logger.debug("initializing drools knowledge agent");

            String changeSetXml = WelfaregoUtils.getConfig(DROOLS_CONFIG_PARAM);
            logger.debug("drools config = {}", changeSetXml);

            knowledgeAgent = KnowledgeAgentFactory.newKnowledgeAgent("WelfaregoAzione");
            knowledgeAgent.addEventListener(new KnowledgeAgentEventListener() {
				
				@Override
				public void resourceCompilationFailed(ResourceCompilationFailedEvent event) {
					logger.error("resourceCompilationFailed event {}", event);
                    for (KnowledgeBuilderError error : event.getKnowledgeBuilder().getErrors()) {
                        logger.error("got error {} at lines {}", error, error.getLines());
                    }
				}
				
				@Override
				public void knowledgeBaseUpdated(KnowledgeBaseUpdatedEvent event) {
					logger.info("knowledgeBaseUpdated event {}", event);
					
				}
				
				@Override
				public void beforeResourceProcessed(BeforeResourceProcessedEvent event) {
					logger.debug("beforeResourceProcessed event {}", event);
					
				}
				
				@Override
				public void beforeChangeSetProcessed(BeforeChangeSetProcessedEvent event) {
					logger.debug("beforeChangeSetProcessed event {}", event);
					
				}
				
				@Override
				public void beforeChangeSetApplied(BeforeChangeSetAppliedEvent event) {
					logger.debug("beforeChangeSetApplied event {}", event);
					
				}
				
				@Override
				public void afterResourceProcessed(AfterResourceProcessedEvent event) {
					logger.debug("afterResourceProcessed event {}", event);
					
				}
				
				@Override
				public void afterChangeSetProcessed(AfterChangeSetProcessedEvent event) {
					logger.debug("afterChangeSetProcessed event {}", event);
					
				}
				
				@Override
				public void afterChangeSetApplied(AfterChangeSetAppliedEvent event) {
					logger.debug("afterChangeSetApplied event {}", event);
				}
            });

            knowledgeAgent.applyChangeSet(ResourceFactory.newByteArrayResource(changeSetXml.getBytes()));

            logger.debug("initialized drools knowledge agent");
        }
        return knowledgeAgent;
    }

    public static StatelessKnowledgeSession getDroolsStatelessKnowledgeSession() throws Exception {
        return (StatelessKnowledgeSession) getDroolsKnowledgeAgent().newStatelessKnowledgeSession();
    }

    public static List<BreMessage> getBreMessages(Collection<Object> list) throws Exception {
        return getBreMessages(null, list);
    }

    public static List<BreMessage> getBreMessages(Object... list) throws Exception {
        return getBreMessages(null, list);
    }

    public static List<BreMessage> getBreMessages(EntityManager em, Object... list) throws Exception {
        return getBreMessages(em, Arrays.asList(list));
    }

    public static List<BreMessage> getBreMessages(EntityManager em, Collection<Object> list) throws Exception {
        Map<String, String> codIntToDesInt = Maps.newHashMap();
        if (logger.isDebugEnabled()) {
            logger.debug("triggering bre rules on " + list.size() + " items:");
            for (Object obj : list) {
                logger.debug("\t\t" + obj);
                if (obj instanceof PaiIntervento) {
                    PaiIntervento paiIntervento = (PaiIntervento) obj;
                    codIntToDesInt.put(paiIntervento.getTipologiaIntervento().getCodTipint(), paiIntervento.getTipologiaIntervento().getDesTipint());
                }
            }
        }
        List<BreMessage> messages = Lists.newArrayList();
        {
            StatelessKnowledgeSession statelessKnowledgeSession = BreUtils.getDroolsStatelessKnowledgeSession();
            List<BreMessage> droolsMessages = Lists.newArrayList();
            statelessKnowledgeSession.setGlobal("messages", droolsMessages);
            if (em != null) {
                statelessKnowledgeSession.setGlobal("em", em);
            }
            for (Object obj : list) {
                try {
                    statelessKnowledgeSession.execute(obj);
                    Preconditions.checkArgument(!droolsMessages.isEmpty(), "errore durante il caricamento delle regole");
                    messages.addAll(droolsMessages);
                    droolsMessages.clear();
                } catch (Exception e) {
                    String message = "errore drools : " + e.toString();
                    if (message.matches(".*Not possible to coerce .false. from class.*Boolean.*")) {
                        message = "valore obbligatorio mancante durante l'esecuzione di una regola";
                    } else {
                        message = "errore tecnico o dati di origine incompleti";
                    }
                    messages.add(new BreMessage(obj.toString(), "WARN", message));
                    logger.error("drools error", e);
                }
            }
        }
        for(BreMessage breMessage : messages) {
            String currentSubject = Strings.nullToEmpty(breMessage.getSubject());
            breMessage.setCode(currentSubject);
            for (Entry<String, String> entry : codIntToDesInt.entrySet()) {
                if (currentSubject.matches("paiIntervento_" + entry.getKey() + ":.*")) {
                    currentSubject = entry.getValue();
                }
            }
            breMessage.setSubject(currentSubject);
        }

        messages = Lists.newArrayList(Sets.newLinkedHashSet(messages));
        logger.debug("got " + messages.size() + " messages from bre");
        return messages;
    }
}
