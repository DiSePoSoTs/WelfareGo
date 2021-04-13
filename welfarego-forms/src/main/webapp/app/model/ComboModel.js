Ext.define('wf.model.ComboModel', {
    extend: 'Ext.data.Model',
    fields: ['name','value','class_tipint','imp_std_costo','rinnovo','misura', 'testo_autorizzazione'] // classTipint serve solo per le combo tipint, ma e' decisamente piu' semplice metterlo qui . . e anche l'importo standard di costo...'
});