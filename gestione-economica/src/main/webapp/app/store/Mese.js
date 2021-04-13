
/*
 * @author Gabriele Rabbiosi - Dotcom S.r.l.
 * @date Luglio 2011
 *
 * Questo è lo store utilizzato per recuperare la lista di option da inserire
 * nelle ComboBox che rappresentano la selezione di un mese
 */

Ext.define('Wp.store.Mese', {
    extend: 'Ext.data.Store',
    requires: ['Wp.model.ComboBox'],

    model: 'Wp.model.ComboBox',
    data: [
        { id: '1',  label: 'Gennaio'},
        { id: '2',  label: 'Febbraio'},
        { id: '3',  label: 'Marzo'},
        { id: '4',  label: 'Aprile'},
        { id: '5',  label: 'Maggio'},
        { id: '6',  label: 'Giugno'},
        { id: '7',  label: 'Luglio'},
        { id: '8',  label: 'Agosto'},
        { id: '9',  label: 'Settembre'},
        { id: '10',  label: 'Ottobre'},
        { id: '11', label: 'Novembre'},
        { id: '12', label: 'Dicembre'}
    ]

});

