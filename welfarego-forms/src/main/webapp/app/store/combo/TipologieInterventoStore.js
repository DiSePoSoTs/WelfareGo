Ext.define('wf.store.combo.TipologieInterventoStore',{
    extend: 'wf.store.combo.ComboStore',
    constructor:function(){
        this.callParent(arguments);
        this.setStoreName("tipologia_intervento");
    }
});