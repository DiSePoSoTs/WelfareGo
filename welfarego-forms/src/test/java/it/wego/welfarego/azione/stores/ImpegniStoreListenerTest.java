package it.wego.welfarego.azione.stores;

import it.wego.extjs.beans.Order;
import it.wego.persistence.PersistenceAdapter;
import it.wego.persistence.PersistenceAdapterFactory;
import it.wego.welfarego.abstracts.AbstractForm;
import it.wego.welfarego.azione.forms.VerificaDatiForm;
import org.testng.annotations.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.*;

import static org.testng.Assert.*;

public class ImpegniStoreListenerTest {
    @Test
    public void testLoad() throws Exception {
        //setup test

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        PersistenceAdapterFactory.setPersistenceUnit("welfaregoPUTest");

        PersistenceAdapter persistenceAdapter = PersistenceAdapterFactory.createPersistenceAdapter();


        VerificaDatiForm verificaDatiForm = new VerificaDatiForm();
        verificaDatiForm.setAttributes(getAttributes());
        verificaDatiForm.setParameters(getParameters());
        ImpegniStoreListener impegniStoreListener = new ImpegniStoreListener(verificaDatiForm);
        verificaDatiForm.setPersistenceAdapter(persistenceAdapter);
        impegniStoreListener.setPersistenceAdapter(persistenceAdapter);

        //given

        //when
        Collection impegni = impegniStoreListener.load(1, 30, 1, Collections.EMPTY_LIST);
        Object[] objects = impegni.toArray();


        //then

    }

    private Map<String,String> getParameters() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(AbstractForm.PARAM_TASKID_KEY, "24657431");

        return map;
    }

    private Map<String,Object> getAttributes() {
        return new HashMap<String, Object>();
    }

}