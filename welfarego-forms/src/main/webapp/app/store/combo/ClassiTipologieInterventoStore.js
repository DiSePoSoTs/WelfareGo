Ext.define('wf.store.combo.ClassiTipologieInterventoStore',{
    extend: 'wf.store.combo.ComboStore',
    constructor:function(){
        this.callParent(arguments);
        this.setStoreName("classe_tipologia_intervento");
    }
});