package it.wego.welfarego.persistence.dao;

import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.Fattura;
import it.wego.welfarego.persistence.entities.Luogo;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class FatturaDaoTest {

    @Test
    public void bb(){
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPU_PROD");
        EntityManager entityManager = factory.createEntityManager();
        AnagrafeSocDao aa = new AnagrafeSocDao(entityManager);
        AnagrafeSoc anagrafeSoc = aa.findByCodAna(24899283);
        System.out.println(anagrafeSoc.getLuogoResidenza());
    }

    @Test
    public void aa(){
        System.out.println("fasdf");
        Integer[] ids = {24899283,24897943,24898291,24898493,24898896,24899151,24898799,24898054,24898751,24899178,24898003,24898456,24898761,24898766,24898786,24898938,24898941,24899114,24899617,24899647,24897938,24898965,24898984,24898529,24898856,24899261,24898202,24898586,24898119,24899251,24899717,24898915,24898903,24899156,24897930,24898087,24899570,24898030,24898108,24898488,24898970,24898992,24899606,24899614,24898559,24898926,24899538,24898474,24898116,24899714,24897933,24898562,24898789,24899014,24899293,24898981,24899503,24899592,24899552,24901578,24898870,24898956,24899254,24898226,24898773,24898792,24899542,24899545,24901591,24898025,24898298,24898892,24898216,24898594,24899642,24898059,24898209,24898776,24899117,24899513,24898049,24898221,24898229,24898500,24899531,24899575,24899581,24899730,24901558,24898999,24898035,24898103,24898451,24898944,24899129,24898090,24898461,24898549,24898571,24898758,24897927,24898006,24898887,24899031,24899138,24899518,24899521,24899567,24899589,24898574,24899042,24898469,24899047,24898241,24898882,24899175,24897907,24899286,24899578,24898111,24898149,24898288,24898077,24898539,24898949,24899124,24899168,24898146,24898581,24899024,24899050,24899276,24899508,24899599,24899624,24902040,24898973,24899633,24898133,24899002,24898143,24898515,24898140,24899007,24899017,24899191,24898042,24898921,24898976,24898070,24898779,24899105,24899707,24898522,24898554,24899555,24899584,24899702,24898464,24899298,24898863,24899039,24899163,24897914,24897917,24898124,24898485,24898874,24899183,24899188,24899560,24898082,24898542,24898589,24898879,24898931,24898987,24897924,24898066,24898127,24899147,24897948,24898011,24898908,24899034,24898532,24899524,24899609,24897901,24898130,24898236,24899196};
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("welfaregoPU_PROD");
        EntityManager entityManager = factory.createEntityManager();
        for(int i = 0; i < ids.length; i++){
            Integer id = ids[i];
            Fattura fattura = entityManager.find(Fattura.class, id);

            AnagrafeSoc anagrafeSoc = fattura.getPaiIntervento().getPai().getCartellaSociale().getAnagrafeSoc();
            Luogo luogoResidenza = anagrafeSoc.getLuogoResidenza();
            String cap = luogoResidenza.getCap();

            if(cap==null) {
                System.out.println("cap null " + anagrafeSoc.getCodAna() + ", " + anagrafeSoc.getCognomeNome());
            }
        }

    }


}