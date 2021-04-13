Ext.define('wcs.model.AssistenteSocialeModel', {
    extend: 'Ext.data.Model',
    proxy: {
        type: 'ajax',
            url : '/CartellaSociale/AssistentiSociali',
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