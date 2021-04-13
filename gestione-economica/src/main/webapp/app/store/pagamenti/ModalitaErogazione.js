
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questo è lo store utilizzato per visualizzare la lista di d ella ComboBox
 * "Modalità di erogazione" dei pagamenti.
 */

Ext.define('Wp.store.pagamenti.ModalitaErogazione', {
    extend: 'Ext.data.Store',
    requires: ['Wp.model.ComboBox'],

    autoload: false,
    model: 'Wp.model.ComboBox',
    data: [
        { id: 'PER_CASSA',  label: 'PER CASSA'},
        { id: 'ACCREDITO',  label: 'ACCREDITO'}
    ]
});

