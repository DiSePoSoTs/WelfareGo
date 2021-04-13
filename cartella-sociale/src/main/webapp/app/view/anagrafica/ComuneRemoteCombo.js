Ext.define('wcs.view.anagrafica.ComuneRemoteCombo',{
    extend: 'Ext.form.field.ComboBox',
    alias: 'widget.wcs_comuneremotecombo',
    typeAhead : true,
    hideTrigger: true,
    triggerAction : 'all',
    loadingText: 'Cerca...',
    //emptyText: 'Digitare per iniziare la ricerca... ',
    minChars: 3,

    mode:'local',
//    queryParam:'codice',
    initComponent: function() {
        //        this.mode =  'remote';
        this.store = Ext.create('wcs.store.ComuneStore'),
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
                                               Ext.form.field.ComboBox.prototype.setValue.call(this, records[0].data.codComune);
                                               // questa riga per le persone fisiche
                                               // Ext.form.field.ComboBox.prototype.setRawValue.call(this, records[0].data.codComune);
                                              }
                                              });
        } else if (value && value[0].raw) {
            var codComune = value[0].raw.codComune;
            this.store.load({
                params: {
                    codice: codComune
                },
                scope: this,
                synchronous: true,
                callback: function(records, operation, success) {
                    //wcs.view.anagrafica.ComuneRemoteCombo.superclass.setValue.call(this, value);
                    if(success){
                        Ext.form.field.ComboBox.prototype.setValue.call(this, codComune);
                    }
                    this.clearInvalid();
                    wcs_isModified = '';
                }
            });
        }else {
            this.callParent(arguments);
        }
    },
    reset: function(){
        this.store.removeAll(false);
        //This is too dirty :)
        //wcs.view.anagrafica.ComuneRemoteCombo.superclass.setValue.call(this, '');
        this.superclass.setValue.call(this, '');
    }
});