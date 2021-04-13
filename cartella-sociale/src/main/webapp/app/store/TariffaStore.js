Ext.define('wcs.store.TariffaStore',{
    extend: 'Ext.data.Store',
    model: 'wcs.model.TariffaModel',
    pageSize: 20,

	proxy: {
        type: 'ajax',
        url: '/CartellaSociale/tariffa',
        extraParams: {
            action: 'list'
        },
        reader: {
            type: 'json',
            rootProperty: 'data',
            successProperty: 'success'
        }
    }
});