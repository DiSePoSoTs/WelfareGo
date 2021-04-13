Ext.define('wcs.view.anagrafica.StatoRemoteCombo',{
    extend: 'Ext.form.field.ComboBox',
    alias: 'widget.wcs_statoremotecombo',
    typeAhead : true,
    hideTrigger: true,
    triggerAction : 'all',
    loadingText: 'Cerca...',
    //emptyText: 'Digitare per iniziare la ricerca... ',
    minChars: 3,
    mode:'local',
//    queryParam:'codice',

    initComponent: function() {
        this.store = Ext.create('wcs.store.StatoStore'),
        //        this.mode =  'remote';
        this.callParent(arguments);
    }
    ,

    setValue: function(value) {

        if(Number.parseInt(value) && !value[0].raw  && !(this.getValue() === value)){
                      this.store.load({
                           params: {
                               codice: value
                           },
                           scope: this,
                           synchronous: true,
                           callback: function(records, operation, success) {
                            Ext.form.field.ComboBox.prototype.setValue.call(this, records[0].raw.codStato);
                           }
                           });


        } else if (value && value[0].raw) {
            var codStato = value[0].raw.codStato;
            this.store.load({
                params: {
                    codice: codStato
                },
                scope: this,
                synchronous: true,
                callback: function(records, operation, success) {
                    //wcs.view.anagrafica.StatoRemoteCombo.superclass.setValue.call(this, value);
                    if(success){
                        Ext.form.field.ComboBox.prototype.setValue.call(this, codStato);
                    }
                    this.clearInvalid();
                    wcs_isModified = '';
                }
            });
        } else {
            this.callParent(arguments);
        }
    },
    reset: function(){
        this.store.removeAll(false);
        //This is too dirty :)
        //wcs.view.anagrafica.StatoRemoteCombo.superclass.setValue.call(this, '');
        this.superclass.setValue.call(this, '');
    }
});