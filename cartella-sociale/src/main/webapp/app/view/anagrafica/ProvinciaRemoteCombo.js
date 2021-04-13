Ext.define('wcs.view.anagrafica.ProvinciaRemoteCombo',{
    extend: 'Ext.form.field.ComboBox',
    alias: 'widget.wcs_provinciaremotecombo',
    typeAhead : true,
    hideTrigger: true,
    triggerAction : 'all',
    loadingText: 'Cerca...',
    //emptyText: 'Digitare per iniziare la ricerca... ',
    minChars: 3,

    mode:'local',
//    queryParam:'codice',
    initComponent: function() {
        this.store = Ext.create('wcs.store.ProvinciaStore'),
        //        this.mode =  'remote';
        this.callParent(arguments);
    }
    ,
    setValue: function(value) {
         if(Number.parseInt(value)   && !value[0].raw  && !(this.getValue() === value)){
                                         this.store.load({
                                              params: {
                                                  codice: value
                                              },
                                              scope: this,
                                              synchronous: true,
                                              callback: function(records, operation, success) {
                                               Ext.form.field.ComboBox.prototype.setValue.call(this, records[0].data.codProvincia);
                                              }
                                              });
        } else if (value && value[0] && value[0].raw) {
            var codProvincia = value[0].raw.codProvincia;
            this.store.load({
                params: {
                    codice: codProvincia
                },
                scope: this,
                synchronous: true,
                callback: function(records, operation, success) {
                    //wcs.view.anagrafica.ProvinciaRemoteCombo.superclass.setValue.call(this, value);
                    if(success){
                        Ext.form.field.ComboBox.prototype.setValue.call(this, codProvincia);
                       // così vedo il codice e non la descrizione
                       // Ext.form.field.ComboBox.prototype.setRawValue.call(this, records[0].data.codVia);
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
        //wcs.view.anagrafica.ProvinciaRemoteCombo.superclass.setValue.call(this, '');
        this.superclass.setValue.call(this, '');
    }
});