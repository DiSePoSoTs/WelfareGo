Ext.define('wcs.model.EducatoreModel', {
    extend: 'Ext.data.Model',
    proxy: {
        type: 'ajax',
            url : '/CartellaSociale/AssistentiSociali',
            extraParams :{
            	educatori:'educatori'
            },
            reader: {
                type: 'json',
                rootProperty: 'rows'
            }
        },

        fields: [
            {name: 'name', mapping: 'name'},
            {name: 'value', mapping: 'value'}
        ]
});