package it.wego.welfarego.pagamenti.pagamenti;

import it.wego.extjs.json.JsonBuilder;
import org.powermock.api.mockito.PowerMockito;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import java.io.PrintWriter;
import java.util.HashMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

//@RunWith(PowerMockRunner.class)
//@PrepareForTest(AjaxPagamento.class)
public class AjaxPagamentoTest {


    @Test
    public void cercaPagamenti_per_periodo_su_singolo_mese() throws Exception {

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();

        AjaxPagamento ajaxPagamento = PowerMockito.spy(new AjaxPagamento());
        ResourceRequest request = mock(ResourceRequest.class);
        ResourceResponse response = mock(ResourceResponse.class);

        when(ajaxPagamento.getEntityManager()).thenReturn(entityManager);
        when(request.getParameter("tipo_intervento")).thenReturn("MI102");
        when(request.getParameter("stato_pagamenti")).thenReturn("da_emettere");
        when(request.getParameter("periodo_considerato_dal_mese")).thenReturn("9");
        when(request.getParameter("periodo_considerato_al_mese")).thenReturn("9");
        when(request.getParameter("periodo_considerato_dal_anno")).thenReturn("2018");
        when(request.getParameter("periodo_considerato_al_anno")).thenReturn("2018");


        // metti qualcosa tanto per evitare errori  a runtime...
        HashMap<String, String[]> value = new HashMap<String, String[]>();
        value.put("4", new String[]{"4"});
        when(request.getParameterMap()).thenReturn(value);


        PrintWriter printWriter = new PrintWriter(System.out, true);
        when(response.getWriter()).thenReturn(printWriter);
        JsonBuilder jsonBuilder = new JsonBuilder();
        PowerMockito.doReturn(jsonBuilder).when(ajaxPagamento, "getJsonBuilder", printWriter);


        //when
        ajaxPagamento.cercaPagamenti(request, response);

        //        then
        assertEquals(jsonBuilder.buildListResponse().size(), 3);

    }

    @Test
    public void cercaPagamenti_per_mese_competenza() throws Exception {

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();

        AjaxPagamento ajaxPagamento = PowerMockito.spy(new AjaxPagamento());
        ResourceRequest request = mock(ResourceRequest.class);
        ResourceResponse response = mock(ResourceResponse.class);

        when(ajaxPagamento.getEntityManager()).thenReturn(entityManager);
        when(request.getParameter("tipo_intervento")).thenReturn("MI102");
        when(request.getParameter("stato_pagamenti")).thenReturn("da_emettere");

        when(request.getParameter("mese_di_competenza_mese")).thenReturn("9");
        when(request.getParameter("mese_di_competenza_anno")).thenReturn("2018");


        // metti qualcosa tanto per evitare errori  a runtime...
        HashMap<String, String[]> value = new HashMap<String, String[]>();
        value.put("4", new String[]{"4"});
        when(request.getParameterMap()).thenReturn(value);


        PrintWriter printWriter = new PrintWriter(System.out, true);
        when(response.getWriter()).thenReturn(printWriter);
        JsonBuilder jsonBuilder = new JsonBuilder();
        PowerMockito.doReturn(jsonBuilder).when(ajaxPagamento, "getJsonBuilder", printWriter);


        //when
        ajaxPagamento.cercaPagamenti(request, response);

        //        then
        assertEquals(jsonBuilder.buildListResponse().size(), 3);

    }

    @Test
    public void cercaPagamenti_per_periodo() throws Exception {

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();

        AjaxPagamento ajaxPagamento = PowerMockito.spy(new AjaxPagamento());
        ResourceRequest request = mock(ResourceRequest.class);
        ResourceResponse response = mock(ResourceResponse.class);

        when(ajaxPagamento.getEntityManager()).thenReturn(entityManager);
        when(request.getParameter("tipo_intervento")).thenReturn("MI102");
        when(request.getParameter("stato_pagamenti")).thenReturn("da_emettere");
        when(request.getParameter("periodo_considerato_dal_mese")).thenReturn("1");
        when(request.getParameter("periodo_considerato_al_mese")).thenReturn("12");
        when(request.getParameter("periodo_considerato_dal_anno")).thenReturn("2018");
        when(request.getParameter("periodo_considerato_al_anno")).thenReturn("2020");


        // metti qualcosa tanto per evitare errori  a runtime...
        HashMap<String, String[]> value = new HashMap<String, String[]>();
        value.put("4", new String[]{"4"});
        when(request.getParameterMap()).thenReturn(value);


        PrintWriter printWriter = new PrintWriter(System.out, true);
        when(response.getWriter()).thenReturn(printWriter);
        JsonBuilder jsonBuilder = new JsonBuilder();
        PowerMockito.doReturn(jsonBuilder).when(ajaxPagamento, "getJsonBuilder", printWriter);


        //when
        ajaxPagamento.cercaPagamenti(request, response);

        //        then
        assertEquals(jsonBuilder.buildListResponse().size(), 23);

    }

}