Ext.define('wcs.model.TipologiaInterventoModel', {
    extend: 'Ext.data.Model',
    proxy: {
        type: 'ajax',
        url : '/CartellaSociale/TipologiaIntervento',
        reader: {
            type: 'json',
            rootProperty: 'data'
        }
    },
    fields: ['name','value',  'label', 'impStdCosto',  'flgFineDurata', 'maxDurataMesi','cntTipint' ,'codClasse']
});