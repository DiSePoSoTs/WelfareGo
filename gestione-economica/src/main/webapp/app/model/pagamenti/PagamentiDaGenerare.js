
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questo è il model che rappresenta una entry nella lista dei risultati della
 * ricerca dei pagamenti da generare nella sezione Pagamenti
 */

Ext.define('Wp.model.pagamenti.PagamentiDaGenerare', {
    extend: 'Ext.data.Model',
    fields: ['id', 'cognome', 'nome', 'fascia', 'tipo_intervento', 'mandato', 'n_mandato','data_chiusura', 'stato', 'delegato']
});

