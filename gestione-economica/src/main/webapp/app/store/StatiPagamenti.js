
/*
 * @author Fabio Bonaccorso
 * @date 
 *
 * Store utilizzato per scegliere gli stati dei pagamenti 
 * 
 */

Ext.define('Wp.store.StatiPagamenti', {
    extend: 'Ext.data.Store',
    requires: ['Wp.model.ComboBox'],

    model: 'Wp.model.ComboBox',

    data: [
      
            {id:"da_generare", label: "Da generare"},
            {id:"da_emettere", label: "Da emettere"},
            {id:"inviate", label: "Emessi"}/*,//TODO al momento lo stato 'Emesse' corrisponde al vecchio stato 'Inviate'
            {id:"da_inviare", label: "Da inviare"},
            {id:"inviate", label: "Inviati"},
            {id:"pagate", label: "Liquidati"},
            */
         

        
]

});

