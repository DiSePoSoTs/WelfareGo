
/*
 * @author Luca Battistin - Dotcom S.r.l.
 * @date Settembre 2011
 *
 * Questo è il model per le l'autocomplete delle persone in nuova fattura
 */

Ext.define('Wp.model.AutocompletePersone', {
        extend: 'Ext.data.Model',

        fields: [
            {name: 'id', mapping: 'id'},
            {name: 'nome', mapping: 'nome'},
            {name: 'cognome', mapping: 'cognome'},
            {name: 'codice_fiscale', mapping: 'codice_fiscale'}
        ]

});

