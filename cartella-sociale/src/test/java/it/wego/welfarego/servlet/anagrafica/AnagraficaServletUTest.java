package it.wego.welfarego.servlet.anagrafica;
import it.wego.welfarego.persistence.dao.VistaAnagrafeDao;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.Associazione;
import it.wego.welfarego.persistence.entities.Utenti;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.testng.Assert.*;

public class AnagraficaServletUTest {

    @Test
    public void preparaMessaggioEsteso_con_utente_creazione_mancante(){
        AnagraficaServlet anagraficaServlet = new AnagraficaServlet();
        String messaggioEsteso = "";
        messaggioEsteso = anagraficaServlet.preparaMessaggioEsteso(new ArrayList(), new Date(), null);
        assertTrue(messaggioEsteso.contains("nominativo non disponibile"));
        assertTrue(messaggioEsteso.contains("associazione non disponibile"));

        messaggioEsteso = anagraficaServlet.preparaMessaggioEsteso(new ArrayList(), new Date(), getUserSenzaAssociazione());
        assertTrue(messaggioEsteso.contains("nominativo non disponibile"));
        assertTrue(messaggioEsteso.contains("associazione non disponibile"));
    }


    @Test
    public void preparaMessaggioEsteso_con_data_creazione_mancante(){
        AnagraficaServlet anagraficaServlet = new AnagraficaServlet();

        String messaggioEsteso = anagraficaServlet.preparaMessaggioEsteso(new ArrayList(), null, getCreationUserDiCooperativa());
        System.out.println(messaggioEsteso);
        assertTrue(messaggioEsteso.contains("data non presente"));
        assertTrue(messaggioEsteso.contains("rossi mario"));
        assertTrue(messaggioEsteso.contains("cooperativa qualunque"));
    }


    @Test
    public void preparaMessaggioEsteso_completo(){
        AnagraficaServlet anagraficaServlet = new AnagraficaServlet();
        ArrayList<VistaAnagrafeDao.VistaAnagrafeToAnagrafePropertyDifference> differencesList = new ArrayList<VistaAnagrafeDao.VistaAnagrafeToAnagrafePropertyDifference>();

        differencesList.add(new VistaAnagrafeDao.VistaAnagrafeToAnagrafePropertyDifference(){
            @Override
            public String getVistaAnagrafeStringValue() {
                return "comune";
            }

            @Override
            public String getAnagrafeSocStringValue() {
                return "wellfargo";
            }

            @Override
            public String getPropertyName() {
                return "zzPropNome";
            }

            public String toString(){
                return this.getPropertyName();
            }
        });
        differencesList.add(new VistaAnagrafeDao.VistaAnagrafeToAnagrafePropertyDifference(){
            @Override
            public String getVistaAnagrafeStringValue() {
                return "comune";
            }

            @Override
            public String getAnagrafeSocStringValue() {
                return "wellfargo";
            }

            @Override
            public String getPropertyName() {
                return "zzPropCognome";
            }

            public String toString(){
                return this.getPropertyName();
            }
        });
        String messaggioEsteso = anagraficaServlet.preparaMessaggioEsteso(differencesList, new Date(), getCreationUserDiCooperativa());
//        System.out.println(messaggioEsteso);
        assertTrue(messaggioEsteso.contains("rossi mario"));
        assertTrue(messaggioEsteso.contains("cooperativa qualunque"));
        assertTrue(messaggioEsteso.contains("zzPropNome"));
        assertTrue(messaggioEsteso.contains("zzPropCognome"));

    }


    @Test
    public  void devoSincronizzareAnagrafeSoc(){
        AnagraficaServlet anagraficaServlet = new AnagraficaServlet();
        AnagrafeSoc anagrafeSoc = new AnagrafeSoc();
        anagrafeSoc.setDataUltimaRetifica(new Date());
        anagrafeSoc.setCreationUser(getCreationUserDiCooperativa());
        assertFalse(anagraficaServlet.devoCreareNotaCondivisa(anagrafeSoc));

        anagrafeSoc = new AnagrafeSoc();
        anagrafeSoc.setDataUltimaRetifica(new Date());
        anagrafeSoc.setCreationUser(getCreationUserComune());
        assertFalse(anagraficaServlet.devoCreareNotaCondivisa(anagrafeSoc));

        anagrafeSoc = new AnagrafeSoc();
        anagrafeSoc.setDataUltimaRetifica(null);
        anagrafeSoc.setCreationUser(getCreationUserComune());
        assertFalse(anagraficaServlet.devoCreareNotaCondivisa(anagrafeSoc));

        anagrafeSoc = new AnagrafeSoc();
        anagrafeSoc.setDataUltimaRetifica(null);
        anagrafeSoc.setCreationUser(null);
        assertFalse(anagraficaServlet.devoCreareNotaCondivisa(anagrafeSoc));
    }

    private Utenti getCreationUserDiCooperativa() {
        Utenti creationUser = new Utenti();
        creationUser.setCognome("rossi");
        creationUser.setNome("mario");
        Associazione associazione = new Associazione();
        associazione.setNome("cooperativa qualunque");
        creationUser.setAssociazione(associazione);
        return creationUser;
    }

    private Utenti getCreationUserComune() {
        Utenti creationUser = new Utenti();
        Associazione associazione = new Associazione();
        associazione.setNome(AnagraficaServlet.COMUNE_DI_TRIESTE);
        creationUser.setAssociazione(associazione);
        return creationUser;
    }

    private Utenti getUserSenzaAssociazione() {
        Associazione associazione = new Associazione();
        associazione.setNome("   ");

        Utenti creationUser = new Utenti();
        creationUser.setAssociazione(associazione);
        return creationUser;
    }
}