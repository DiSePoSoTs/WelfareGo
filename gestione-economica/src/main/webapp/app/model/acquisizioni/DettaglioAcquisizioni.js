
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questo è il model che rappresenta una entry nella lista di dettaglio della
 * ricerca per la sezione Acquisizione dati
 */

Ext.define('Wp.model.acquisizioni.DettaglioAcquisizioni', {
    extend: 'Ext.data.Model',
    fields: [
    {
        name: 'id', 
        type: 'string'
    },
    {
    	name:'cod_tip_int'
    },
{
        name: 'tipo_servizio', 
        type: 'string'
    },
{
        name: 'unita_di_misura', 
        type: 'string'
    },
{
        name: 'qt_prevista', 
        type: 'string'
    },
{
        name: 'qt_erogata', 
        type: 'float', 
        useNull: true
    },
{
        name: 'qt_beneficiata', 
        type: 'float', 
        useNull: true
    },
{
        name: 'bdgPrevEur', 
        type: 'float'
    },
{
        name: 'bdgConsEur', 
        type: 'float', 
        useNull: true
    },
{
        name: 'assenze', 
        type: 'float', 
        useNull: true
    },
    {
        name: 'assenze_totali', 
        type: 'float', 
        useNull: true
    },
{
        name: 'liquidazione', 
        type: 'string'
    },
{
        name: 'fatturazione', 
        type: 'string'
    },
{
        name: 'variazione_straordinaria', 
        type: 'float', 
        useNull: true
    },
{
        name: 'causale', 
        type: 'string'
    },
{
        name: 'motivazioneVariazioneSpesa', 
        type: 'string'
    },
{
        name: 'mese_eff', 
        type: 'int', 
        useNull: true
    },
{
        name: 'note', 
        type: 'string'
    },
    'anno','codImp'
    ]
});

