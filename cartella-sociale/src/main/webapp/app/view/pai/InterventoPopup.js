Ext.define('wcs.view.pai.InterventoPopup', {
    extend: 'Ext.window.Window',
    alias : 'widget.wcs_interventoPopup',
    title : 'Intervento',
    layout: 'fit',
    modal: true,
    width: 900,
    autoShow: true,

    initComponent: function() {
        this.items= [{
            xtype: 'wcs_interventiform',
            id: 'wcs_interventiForm'
        }];

        this.callParent(arguments);
    }
});