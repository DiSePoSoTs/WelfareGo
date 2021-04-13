// may be useless
Ext.define('wcs.view.pai.InterventiImpegniBar',{
    extend: 'Ext.PagingToolbar',
    alias: 'widget.wcs_interventoimpegnibar',
    store: 'InterventiImpegniStore',
    displayMsg: 'Visualizzo gli impegni da {0} a {1} di {2}',
    emptyMsg: 'Nessun impegno',
    items: ['-',{
        xtype: 'textfield',
        readOnly: true,
        minValue: 0,
        fieldLabel: 'Differenza &euro;',
        itemId: 'totaleField',
        name: 'totale',
        value: '0.00',
        validator: function(value){
            if (value != '0.00' && value != '-0.00'){
                return 'Non hai inserito l\'impegno corretto';
            } else {
                return true;
            }
        }
    }]
});