
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questo è il model che rappresenta una entry nella lista dei risultati della
 * ricerca nella sezione Pagamenti
 */

Ext.define('Wp.model.pagamenti.Pagamenti', {
    extend: 'Ext.data.Model',
    fields: ['id', 'cognome', 'nome', 'fascia', 'tipo_intervento', 'importo', 'n_mandato', 'stato', 'codice_stato', 'azione', 'delegato']
});

