
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questo è il model che rappresenta una entry nella lista dei risultati della
 * ricerca nella sezione Acquisizione dati
 */

Ext.define('Wp.model.acquisizioni.Acquisizioni', {
    extend: 'Ext.data.Model',
    fields: ['id', 'cognome', 'nome', 'uot_struttura', 'tipo_intervento', 'data_inizio','data_avvio', 'data_fine','durata_mesi','previsto_euro','previsto_quantita','fascia','tipo_pagfat','data_isee','data_apertura', 'delegato']
});

