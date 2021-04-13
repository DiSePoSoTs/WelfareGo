
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questo è il model che rappresenta una entry nella lista delle voci di una
 * nuova fattura
 */

Ext.define('Wp.model.fatturazioni.VociFatturazioni', {
    extend: 'Ext.data.Model',
    fields: [
        { name: 'id', type: 'string' },
        { name: 'id_fattura', type: 'string' },
        { name: 'tipo_servizio', type: 'string' },
        { name: 'tipo_servizio_value', type: 'string' },
        { name: 'unita_di_misura', type: 'string' },
        { name: 'quantita', type: 'float', useNull: true },
        { name: 'importo_unitario', type: 'float', useNull: true },
        { name: 'importo_dovuto', type: 'float', useNull: true },
        { name: 'importoSenzaIva', type: 'float' },
        { name: 'importo_fascia', type: 'float' },
        { name: 'importoConIva', type: 'float' },
        { name: 'ivaTotale', type: 'float' },
        { name: 'aliquotaIva', type: 'float' },
        { name: 'totaleVariazioniConIva', type: 'float' },
        { name: 'riduzione', type: 'float', useNull: true },
        { name: 'aumento', type: 'float', useNull: true },
        { name: 'variazione_straordinaria', type: 'float', useNull: true },
        { name: 'mese', type: 'int', useNull: true },
        { name: 'descrizioneRiduzione', type: 'string'},
        { name: 'percentualeRiduzione', type: 'float' },
        { name: 'idFascia', type: 'int', useNull: true },
        { name: 'contributoRiga', type: 'float', useNull: true }
    ]
});

