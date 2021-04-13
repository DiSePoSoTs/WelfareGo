Ext.define('wcs.view.anagrafica.ViaRemoteCombo',{
    extend: 'Ext.form.field.ComboBox',
    alias: 'widget.wcs_viaremotecombo',
    typeAhead : true,
    hideTrigger: true,
    triggerAction : 'all',
    loadingText: 'Cerca...',
    //emptyText: 'Digitare per iniziare la ricerca... ',
    minChars: 2,

    mode:'local',
//    queryParam:'codice',
    initComponent: function() {
        this.store = Ext.create('wcs.store.ViaStore'),
//        this.mode =  'remote';
        this.callParent(arguments);
    }
    ,
    setValue: function(value) {
        if(Number.parseInt(value)  && !value[0].raw && !(this.getValue() === value)){
                                         this.store.load({
                                              params: {
                                                  codice: value
                                              },
                                              scope: this,
                                              synchronous: true,
                                              callback: function(records, operation, success) {
                                              var codVia = records[0].data.codVia;
                                              if(success){
                                               Ext.form.field.ComboBox.prototype.setValue.call(this, codVia);
                                              }
                                               // così vedo il codice e non la descrizione
                                               // Ext.form.field.ComboBox.prototype.setRawValue.call(this, records[0].data.codVia);
                                              }
                                              });
        } else if (value && value[0].data) {
            var codVia = value[0].data.codVia;
            this.store.load({
                params: {
                    codice: codVia
                },
                scope: this,
                synchronous: true,
                callback: function(records, operation, success) {
                    //wcs.view.anagrafica.ViaRemoteCombo.superclass.setValue.call(this, value);

                    if(success){
                        Ext.form.field.ComboBox.prototype.setValue.call(this, codVia);
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
        //wcs.view.anagrafica.ViaRemoteCombo.superclass.setValue.call(this, '');
        this.superclass.setValue.call(this, '');
    }
});