
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questo è il model che rappresenta una entry nella lista dei risultati della
 * ricerca delle fatture da generare nella sezione Fatturazioni
 */

Ext.define('Wp.model.fatturazioni.FatturazioniDaGenerare', {
    extend: 'Ext.data.Model',
    fields: ['id', 'cognome', 'nome', 'fascia', 'tipo_intervento', 'importo', 'riscosso', 'n_fattura', 'fattura_nota_credito', 'stato']
});

