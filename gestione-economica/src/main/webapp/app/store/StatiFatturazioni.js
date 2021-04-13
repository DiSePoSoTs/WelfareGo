
/*
 * @author Fabio Bonaccorso
 * @date 
 *
 * Store utilizzato per scegliere gli stati delle fatturazioni
 * 
 */

Ext.define('Wp.store.StatiFatturazioni', {
    extend: 'Ext.data.Store',
    requires: ['Wp.model.ComboBox'],

    model: 'Wp.model.ComboBox',

    data: [
      
            {id:"da_generare", label: "Da generare"},
            {id:"da_emettere", label: "Da emettere"},
            //in realtà qui lo stato dovrebbe essere "emesse" ma per non andare in contrasto con quanto fatto precedentemente dagli sviluppatori di we go lo lasciamo cosi
            
            {id:"inviate", label: "Emessi"}
          /*  {id:"da_inviare", label: "Da inviare"},
            {id:"inviate", label: "Inviati"},
            {id:"pagate", label: "Riscosse"},*/
            
         

        
]

});

