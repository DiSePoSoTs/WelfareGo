package it.wego.welfarego.servlet.pai;

import it.wego.welfarego.model.json.JsonImporto;
import it.wego.welfarego.persistence.dao.TipologiaInterventoDao;
import org.powermock.api.mockito.PowerMockito;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class InterventiServletTest {

    @Test
    public void testCalcolaCosto() throws Exception {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPUTest");
        EntityManager entityManager = factory.createEntityManager();

//        given

        TipologiaInterventoDao tipologiaInterventoDao = new TipologiaInterventoDao(entityManager);
        InterventiServlet  interventiServlet = PowerMockito.spy(new InterventiServlet());
        boolean empty = false;
        String codPai = "22703995";

        HttpServletRequest request = mock(HttpServletRequest.class);

//        when(request.getParameter("tipo")).thenReturn("MI104");
        when(request.getParameter("quantita")).thenReturn("181");
        when(request.getParameter("durataMesi")).thenReturn("1");
        when(request.getParameter("tipo")).thenReturn("EC003");
        when(request.getParameter("dataAvvio")).thenReturn("26/03/2019");
        when(request.getParameter("dataFine")).thenReturn("");
        when(request.getParameter("struttura")).thenReturn("");
        when(request.getParameter("forfait")).thenReturn("false");


//        when
        JsonImporto costo = (JsonImporto) interventiServlet.calcolaCosto(request, tipologiaInterventoDao, codPai, empty);

//        then
        System.out.println(costo.getImporto());

    }

}