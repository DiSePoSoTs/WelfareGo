Ext.define('wf.model.InterventoModel', {          //definizione modello Intervento
    extend: 'Ext.data.Model',
    fields: [{
        name: 'id',
        type: 'int'
    },'pai_intervento_pk', 'intervento', 'data_apertura', 'costo_prev', 'budget_disp','motivazione']
});