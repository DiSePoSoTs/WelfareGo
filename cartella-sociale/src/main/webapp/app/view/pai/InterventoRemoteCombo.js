Ext.define('wcs.view.pai.InterventoRemoteCombo',{
    extend: 'Ext.form.field.ComboBox',
    alias: 'widget.wcs_interventoremotecombo',
//    typeAhead : true,
    triggerAction : 'all',
    initComponent: function() {
        this.store = Ext.create('wcs.store.TipoInterventoStore'),
        this.callParent(arguments);
    }
//    ,
//    setValue: function(value) {
//        var classe = Ext.getCmp('wcs_interventoClasse').getValue();
//        this.store.load({
//            params: {
//                codClasse: classe
//            },
//            scope: this,
//            synchronous: true,
//            callback: function(records, operation, success) {
//                //wcs.view.pai.InterventoRemoteCombo.superclass.setValue.call(this, value);
//                this.superclass.setValue.call(this, value);
//                this.clearInvalid();
//            }
//        });
//    },
//    reset: function(){
//        this.store.getProxy().extraParams.codClasse = null;
//        this.store.removeAll();
//        //wcs.view.pai.InterventoRemoteCombo.superclass.setValue.call(this, '');
//        this.superclass.setValue.call(this, '');
//    }
});