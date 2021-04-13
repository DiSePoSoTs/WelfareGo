package it.wego.welfarego.persistence.dao;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import it.wego.persistence.PersistenceAdapter;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
//import it.wego.welfarego.persistence.entities.AnagrafeSoc.Luogo;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 *
 * @author giuseppe
 */
public class AnagrafeSocDao extends PersistenceAdapter {

    public AnagrafeSocDao(EntityManager em) {
        super(em);
    }

    public Long countAll() {
        return findOne(Long.class, "SELECT COUNT(a) FROM  AnagrafeSoc a");
    }

    public AnagrafeSoc findByCodAna(String codAna) {
        return findByCodAna(Integer.valueOf(codAna));
    }

    public AnagrafeSoc findByCodAna(int codAna) {
        TypedQuery<AnagrafeSoc> query = getEntityManager().createNamedQuery("AnagrafeSoc.findByCodAna", AnagrafeSoc.class);
        query.setParameter("codAna", codAna);
        return PersistenceAdapter.getSingleResult(query);
    }

    public AnagrafeSoc findByCodAnaCom(String codAnaCom) {
        TypedQuery<AnagrafeSoc> query = getEntityManager().createNamedQuery("AnagrafeSoc.findByCodAnaCom", AnagrafeSoc.class);
        query.setParameter("codAnaCom", codAnaCom);
        return getSingleResult(query);
    }

    public AnagrafeSoc findByCodFisc(String codFisc) {
        TypedQuery<AnagrafeSoc> query = getEntityManager().createNamedQuery("AnagrafeSoc.findByCodFisc", AnagrafeSoc.class);
        query.setParameter("codFisc", codFisc);
        return getSingleResult(query);
    }

    public AnagrafeSoc findByPartIva(String partIva) {
        TypedQuery<AnagrafeSoc> query = getEntityManager().createNamedQuery("AnagrafeSoc.findByPartIva", AnagrafeSoc.class);
        query.setParameter("partIva", partIva);
        return getSingleResult(query);
    }

    public AnagrafeSoc findByCodFiscCodAnaCom(String codFisc, String codAnaCom) {
        TypedQuery<AnagrafeSoc> query = getEntityManager().createQuery("SELECT a "
                + "FROM AnagrafeSoc a "
                + "WHERE a.codFisc = :codFisc "
                + "OR a.codAnaCom = :codAnaCom", AnagrafeSoc.class);
        query.setParameter("codFisc", codFisc);
        query.setParameter("codAnaCom", codAnaCom);
        return getSingleResult(query);
    }
    
   /**
    * 
    * @param codIban
    * @return
    */
    public AnagrafeSoc findByCodIban(String codIban){
    	TypedQuery<AnagrafeSoc> query = getEntityManager().createQuery("SELECT a "
    			+"FROM AnagrafeSoc a "
    			+"WHERE a.ibanPagam LIKE :ibanPagam",AnagrafeSoc.class);
    	query.setParameter("ibanPagam", "%"+codIban+"%");
    	List<AnagrafeSoc> lista = query.getResultList();
    	if(lista.isEmpty() || lista.size()>1){
    		return null;
    	}
    	else {
    		return lista.get(0);
    	}
    	
    }
    
    /**
     * Ritorna la lista delle persone giuridiche presenti in db
     * @return
     */
    public List<AnagrafeSoc> findPersoneGiuridiche(){
    	   TypedQuery<AnagrafeSoc> query = getEntityManager().createQuery("SELECT a "
    			    + "FROM AnagrafeSoc a "
                    + "WHERE a.flgPersFg = :flgPers",AnagrafeSoc.class);
    	   query.setParameter("flgPers", AnagrafeSoc.PERSONA_FISICA_G);
    	   return query.getResultList();
    }

    private String convertForDb(String property) {
        String result;
        if (property.equals("dataNascita")) {
            result = "dtNasc";
        } else if (property.equals("codiceFiscale")) {
            result = "codFisc";
        } else {
            result = property;
        }
        return result;
    }

    /**
     *
     * @return @deprecated do not use!! memory drain!!
     */
    @Deprecated
    public List<AnagrafeSoc> findAll() {
        return find(AnagrafeSoc.class, "SELECT a FROM AnagrafeSoc a");
    }

    public List<AnagrafeSocKeys> findAllKeys() {
        return Lists.newArrayList(Iterables.transform(getEntityManager().createQuery("SELECT a.codAna,a.codFisc,a.codAnaCom,a.codAnaFamCom,a.partIva FROM AnagrafeSoc a").getResultList(), new Function<Object, AnagrafeSocKeys>() {
            public AnagrafeSocKeys apply(final Object input) {
                try {
                    return new AnagrafeSocKeys() {
                        final Object[] objectArray = (Object[]) input;

                        public AnagrafeSoc getAnagrafeSocReference() {
                            return getEntityManager().getReference(AnagrafeSoc.class, getCodAna());
                        }

                        public AnagrafeSoc getAnagrafeSoc() {
                            return getEntityManager().find(AnagrafeSoc.class, getCodAna());
                        }

                        public String getCodFisc() {
                            return (String) objectArray[1];
                        }

                        public String getCodAnaCom() {
                            return (String) objectArray[2];
                        }

                        public String getCodAnaFamCom() {
                            return (String) objectArray[3];
                        }

                        public Integer getCodAna() {
                            return (Integer) objectArray[0];
                        }

                        public String getPiva() {
                            return (String) objectArray[4];
                        }
                    };

                } catch (RuntimeException ex) {
                    getLogger().error("errror while parsing anagrafeSocKey row = {}", input);
                    throw ex;
                }
            }
        }));
    }

    public void checkDuplicateCfPiva(AnagrafeSoc anagrafeSoc) {
        if (!Strings.isNullOrEmpty(anagrafeSoc.getCodFisc())) {
            Preconditions.checkArgument(findByCodFisc(anagrafeSoc.getCodFisc()) == null, "e' gia' presente un record anagrafe con codice fiscale = '%s'", anagrafeSoc.getCodFisc());
        }
        if (!Strings.isNullOrEmpty(anagrafeSoc.getPartIva())) {
            Preconditions.checkArgument(findByPartIva(anagrafeSoc.getPartIva()) == null, "e' gia' presente un record anagrafe con partita iva = '%s'", anagrafeSoc.getPartIva());
        }
    }

    public static interface AnagrafeSocKeys {

        public AnagrafeSoc getAnagrafeSocReference();

        public AnagrafeSoc getAnagrafeSoc();

        public String getCodFisc();

        public String getCodAnaCom();

        public String getCodAnaFamCom();

        public Integer getCodAna();
    }
}
