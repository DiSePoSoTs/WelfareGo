
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questo è il model che rappresenta una entry nella lista delle voci di un
 * nuovo pagamento
 */

Ext.define('Wp.model.pagamenti.VociPagamenti', {
    extend: 'Ext.data.Model',
    fields: [
    {
        name: 'id', 
        type: 'string'
    },
    {
        name: 'id_pagam', 
        type: 'string'
    },
    {
        name: 'tipo_servizio', 
        type: 'string'
    },
    {
        name: 'tipo_servizio_value', 
        type: 'string'
    },
    {
        name: 'causale', 
        type: 'string'
    },
    {
        name: 'note', 
        type: 'string'
    },
    {
        name: 'unita_di_misura', 
        type: 'string'
    },
    {
        name: 'quantita', 
        type: 'float', 
        useNull: true
    },
    {
        name: 'assenze_mensili', 
        type: 'float', 
        useNull: true
    },
    {
        name: 'assenze_totali', 
        type: 'float', 
        useNull: true
    },
    {
        name: 'importo_unitario', 
        type: 'float', 
        useNull: true
    },
    {
        name: 'importo_dovuto', 
        type: 'float', 
        useNull: true
    },
    {
        name: 'importo_preventivato', 
        type: 'float', 
        useNull: true
    },
    {
        name: 'importo_consuntivato', 
        type: 'float', 
        useNull: true
    },
    {
        name: 'riduzione', 
        type: 'float', 
        useNull: true
    },
    {
        name: 'variazione_straordinaria', 
        type: 'float', 
        useNull: true
    },
    {
        name: 'causale_variazione_straordinaria', 
        type: 'string'
    },
    {
        name: 'aumento', 
        type: 'float', 
        useNull: true
    },
    {
        name: 'mese', 
        type: 'int', 
        useNull: true
    },
    'codImp','anno'
    ]
});

