Ext.define('wf.model.LogCassaModel', {
    extend: 'Ext.data.Model',
    fields: ['id', 'data_operazione', 'tipo', 'dettagli', 'importo', 'totale','flgRicevuta','pai','intervento']
});