Ext.define('wcs.store.PaiStore', {
    extend: 'Ext.data.Store',
    model: 'wcs.model.PaiModel',
    remoteSort: false,
    pageSize: 50,
    proxy: {
        type: 'ajax',
        url: '/CartellaSociale/pai',
        reader: {
            type: 'json',
            rootProperty: 'data',
            totalProperty: 'total',
            successProperty: 'success'
        }
    },
    sorters: [{
            property:'statoPai',
            transform: (function() {
                var map={
                  'A':1,
                  'S':2,
                  'C':3
                };
                return function(value) {
                    return map[value]||0;
                };
            })(),
            direction: 'ASC'
        }, {
            property: 'dtApePai',
            direction: 'DESC'
        }, {
            property: 'dtChiusPai',
            direction: 'DESC'
        }]
});