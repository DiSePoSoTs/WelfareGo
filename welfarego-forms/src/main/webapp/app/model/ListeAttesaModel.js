Ext.define('wf.model.ListeAttesaModel', {
    extend: 'Ext.data.Model',
    fields: ['cod_pai','cod_tip_int','cnt_tip_int', 'intervento', 'cognome', 'nome', 'data_richiesta', 'isee', 'stato','approvato','respinto']
});