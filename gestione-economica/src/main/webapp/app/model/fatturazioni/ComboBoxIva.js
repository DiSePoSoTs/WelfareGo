
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Agosto 2011
 *
 * Questo è il model per tutti lo store che recupera dal server le option del
 * ComboBox con i valori dell'IVA
 */

Ext.define('Wp.model.fatturazioni.ComboBoxIva', {
    extend: 'Ext.data.Model',
    fields: ['id', 'label', 'valore']
});

