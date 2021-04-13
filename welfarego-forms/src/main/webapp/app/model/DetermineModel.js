Ext.define('wf.model.DetermineModel', {
    extend: 'Ext.data.Model',
    fields: ['id', 'cognome', 'nome', 'data_richiesta', 'data_fine_intervento', 'isee', 'stato','uot','costo','quantita','budget']
});