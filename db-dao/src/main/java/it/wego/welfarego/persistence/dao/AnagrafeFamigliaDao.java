/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.wego.welfarego.persistence.dao;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import it.wego.persistence.ConditionBuilder;
import it.wego.persistence.PersistenceAdapter;
import it.wego.welfarego.persistence.entities.AnagrafeFam;
import it.wego.welfarego.persistence.entities.AnagrafeFamPK;
import it.wego.welfarego.persistence.entities.AnagrafeSoc;
import it.wego.welfarego.persistence.entities.Parametri;
import it.wego.welfarego.persistence.entities.VistaAnagrafe;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * @author giuseppe
 */
public class AnagrafeFamigliaDao extends PersistenceAdapter {

    public AnagrafeFamigliaDao(EntityManager em) {
        super(em);
    }

    public List<AnagrafeFam> findByCodAnaQualifica(int codAna, int qualifica, int limit, int offset) {
        Query query = getEntityManager().createQuery("SELECT a "
                + "FROM AnagrafeFam a JOIN a.codQual p "
                + "WHERE a.anagrafeFamPK.codAna = :codAna "
                + "AND p.idParam = :idParam");
        query.setParameter("codAna", codAna);
        query.setParameter("idParam", qualifica);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        List<AnagrafeFam> anagrafe = query.getResultList();
        return anagrafe;
    }

    public int countCodAnaQualifica(int codAna, int qualifica) {
        Query query = getEntityManager().createQuery("SELECT COUNT(a) "
                + "FROM AnagrafeFam a JOIN a.codQual p "
                + "WHERE a.anagrafeFamPK.codAna = :codAna "
                + "AND p.idParam = :idParam");
        query.setParameter("codAna", codAna);
        query.setParameter("idParam", qualifica);
        int count = 0;
        Integer result = (Integer) query.getSingleResult();
        if (result != null) {
            count = result.intValue();
        }
        return count;
    }

    public List<VistaAnagrafe> find_Familiari_Da_AnagrafeComunale(AnagrafeSoc anagrafeSoc) {
        Preconditions.checkNotNull(anagrafeSoc);
        if (!Strings.isNullOrEmpty(anagrafeSoc.getCodAnaFamCom())) {
            return new VistaAnagrafeDao(getEntityManager()).findByNumeroFamiglia(Integer.valueOf(anagrafeSoc.getCodAnaFamCom()));
        } else {
            return Collections.emptyList();
        }
    }

    public List<VistaAnagrafe> findFamigliareAnagrafeComunaleNonMorti(int codAna) {
        AnagrafeSoc as = new AnagrafeSocDao(getEntityManager()).findByCodAna(codAna);
        Preconditions.checkNotNull(as);
        if (!Strings.isNullOrEmpty(as.getCodAnaFamCom())) {
            return new VistaAnagrafeDao(getEntityManager()).findByNumeroFamigliaNonMorti(Integer.valueOf(as.getCodAnaFamCom()));
        } else {
            return Collections.emptyList();
        }
    }


    public AnagrafeFam findByKey(int codAna, int codAnaFam) {
    	
    	AnagrafeFamPK key = new AnagrafeFamPK(codAna, codAnaFam);

    	return getEntityManager().find(AnagrafeFam.class, key);
    }

    public List<AnagrafeFam> findByCodAna(int codAna, int limit, int offset) {
        Query query = getEntityManager().createNamedQuery("AnagrafeFam.findByCodAna");
        query.setParameter("codAna", codAna);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        List<AnagrafeFam> anagrafe = query.getResultList();
        return anagrafe;
    }

    public List<AnagrafeFam> findByCodAna(String codAna) {
        return findByCodAna(Integer.valueOf(codAna));
    }

    public List<AnagrafeFam> findByCodAna(int codAna) {
        Query query = getEntityManager().createNamedQuery("AnagrafeFam.findByCodAna");
        query.setParameter("codAna", codAna);
        List<AnagrafeFam> anagrafe = query.getResultList();
        return anagrafe;
    }

    public int countByCodAna(int codAna) {
        Query query = getEntityManager().createQuery("SELECT COUNT(a) "
                + "FROM AnagrafeFam a");
        query.setParameter("codAna", codAna);
        int count = 0;
        Integer result = (Integer) query.getSingleResult();
        if (result != null) {
            count = result.intValue();
        }
        return count;
    }

    /**
     * restituisce un merge dei record famiglia dritti e rovesci, raddrizzandoli
     * dove necessario
     *
     * @param anagrafeSoc
     * @return
     */
    public List<AnagrafeFam> getAnagrafeFamListMerge(final AnagrafeSoc anagrafeSoc) {
        List<AnagrafeFam> anagrafeFamListAsSource = anagrafeSoc.getAnagrafeFamListAsSource();
        List<AnagrafeFam> anagrafeFamListAsTarget = anagrafeSoc.getAnagrafeFamListAsTarget();

        Function<AnagrafeFam, AnagrafeFam> anagrafeFamReverserFunction = getAnagrafeFamReverserFunction();
        Iterable<AnagrafeFam> transform = Iterables.transform(anagrafeFamListAsTarget, anagrafeFamReverserFunction);
        Iterable<AnagrafeFam> concat = Iterables.concat(anagrafeFamListAsSource, transform);
        HashSet<AnagrafeFam> elements = Sets.newHashSet(concat);
        return Lists.newArrayList(elements);
    }

    public List<AnagrafeFam> getAnagrafeFamListMerge(String codAna) {
        return getAnagrafeFamListMerge(new AnagrafeSocDao(getEntityManager()).findByCodAna(codAna));
    }

    public List<AnagrafeFam> getAnagrafeFamListMerge(int codAna, @Nullable Predicate<AnagrafeFam> filter) {
        EntityManager entityManager = getEntityManager();
        AnagrafeSocDao anagrafeSocDao = new AnagrafeSocDao(entityManager);
        AnagrafeSoc anagrafeSoc = anagrafeSocDao.findByCodAna(codAna);
        List<AnagrafeFam> anagrafeFamListMerge = getAnagrafeFamListMerge(anagrafeSoc);
        return filter != null ? Lists.newArrayList(Iterables.filter(anagrafeFamListMerge, filter)) : anagrafeFamListMerge;
    }

    public List<AnagrafeFam> getAnagrafeFamListMerge(int codAna) {
        return getAnagrafeFamListMerge(codAna, null);
    }

    public Function<AnagrafeFam, AnagrafeFam> getAnagrafeFamReverserFunction() {
        return new Function<AnagrafeFam, AnagrafeFam>() {
            public AnagrafeFam apply(AnagrafeFam input) {
                return getAnagrafeFamReverse(input);
            }
        };
    }

    public AnagrafeFam getAnagrafeFamReverse(AnagrafeFam anagrafeFam) {
        return new AnagrafeFam(
                anagrafeFam.getAnagrafeSocTarget(),
                anagrafeFam.getAnagrafeSocSource(),
                MoreObjects.firstNonNull(new ParametriIndataDao(getEntityManager()).getReverse(anagrafeFam.getCodQual()), anagrafeFam.getCodQual()));
    }

    public Function<AnagrafeFam, AnagrafeSoc> getAnagrafeSocOtherFunction(final int codAna) {
        return new Function<AnagrafeFam, AnagrafeSoc>() {
            public AnagrafeSoc apply(AnagrafeFam input) {
                return input.getAnagrafeSocSource().getCodAna() == codAna ? input.getAnagrafeSocTarget() : input.getAnagrafeSocSource();
            }
        };
    }

    /**
     * filtra per relazioni di parentela (un'anagrafe fam rappresenta una
     * relazione di parentela se corrisponde al parametro generico
     * qualifica(qo):.*parente.*, oppure e' uno dei parametri
     * gradoDiParentela(gp)
     *
     * @return
     */
    public static Predicate<AnagrafeFam> getRelazioneDiParentelaFilter() {
    	
        return new Predicate<AnagrafeFam>() {
            public boolean apply(AnagrafeFam input) {
                if (input.getCodQual() == null) {
                    return false;
                }
                if (input.getCodQual().getDesParam() != null && input.getCodQual().getDesParam().matches(".*parente.*")) {
                    return true;
                }
                if (Objects.equal(input.getCodQual().getIdParam().getTipParam().getTipParam(), Parametri.GRADO_DI_PARENTELA)) {
                    return true;
                }
                return false;
            }

            public boolean test(AnagrafeFam input) {
                return apply(input); //To change body of generated methods, choose Tools | Templates.
            }
        };
    }

    public static Predicate<AnagrafeFam> getNotRelazioneDiParentelaFilter() {
        return Predicates.not(getRelazioneDiParentelaFilter());
    }

    public void removeAnagrafeFam(Integer codAnag1, Integer codAnag2) {
        initTransaction();
        executeUpdate(AnagrafeFam.class, "DELETE FROM AnagrafeFam f", ConditionBuilder.and(
                ConditionBuilder.isIn("f.anagrafeSocTarget.codAna", codAnag1, codAnag2),
                ConditionBuilder.isIn("f.anagrafeSocSource.codAna", codAnag1, codAnag2)));
        commitTransaction();
    }

    public List<AnagrafeFam> findAll() {
        return find(AnagrafeFam.class, "SELECT f FROM AnagrafeFam f");
    }

    public List<AnagrafeFamPK> findAllPk() {
        return find(AnagrafeFamPK.class, "SELECT f.anagrafeFamPK FROM AnagrafeFam f");
    }

    public Long countAll() {
        return findOne(Long.class, "SELECT COUNT(f) FROM  AnagrafeFam f");
    }
}
