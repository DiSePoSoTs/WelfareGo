
Ext.require('wcs.view.anagrafica.StatoRemoteCombo');
Ext.require('wcs.view.anagrafica.ProvinciaRemoteCombo');
Ext.require('wcs.view.anagrafica.ComuneRemoteCombo');
Ext.require('wcs.view.anagrafica.ViaRemoteCombo');
Ext.require('wcs.view.anagrafica.CivicoRemoteCombo');

Ext.define('wcs.view.anagrafica.IndirizzoFieldSet',{
    extend: 'Ext.form.FieldSet',
    alias: 'widget.wcs_anagraficaindirizzofieldset',
    collapsible: true,
    defaultType: 'textfield',
    layout: 'anchor',
    allowBlank:false,
    initComponent: function() {
        var ns=this.fieldNameSpace;
        var allowBlank=this.allowBlank;
        var labelSuffix=allowBlank?'':'*';
        var tabOffset=this.tabOffset-1;
        var codProv,codStato,codCom,codVia;
        var rawLoad=function(field){
            var newValue=field.getValue();
            if(field.store && field.store.find(field.valueField,newValue)<0){
                field.setRawValue(newValue);
            }
        };
        this.items=[{
            xtype: 'container',
            layout:'column',
            items: [{
                xtype: 'container',
                columnWidth:.5,
                layout: 'anchor',
                items: [{
                    xtype:'wcs_statoremotecombo',
                    hiddenName:'codStato',
                    valueField:'codStato',
                    tabIndex: tabOffset+1,
                    displayField:'desStato',
                    store:Ext.create('wcs.store.StatoStore',{}),
                    allowBlank: allowBlank,
                    blankText: 'Questo campo è obbligatorio',
                    fieldLabel: 'Stato'+labelSuffix,
                    name: ns+'Stato',
                    listeners: {
                        change:function(combo){
                            codStato=combo.getValue();
                            rawLoad(combo);
                        }
                    },
                    anchor:'95%'
                },{
                    xtype:'wcs_comuneremotecombo',
                    hiddenName:'codComune',
                    valueField:'codComune',
                    tabIndex: tabOffset+3,
                    displayField:'desComune',
                    allowBlank: allowBlank,
                    blankText: 'Questo campo è obbligatorio',
                    fieldLabel: 'Comune'+labelSuffix,
                    name: ns+'Comune',
                    listeners:{
                        change:function(combo){
                            codCom=combo.getValue();
                            rawLoad(combo);
                        },
                        beforequery:function(obj,options) {
                            this.store.removeAll();
                            this.store.proxy.extraParams = {
                                codStato: codStato,
                                codProv: codProv
                            };
                        }
                    },
                    anchor:'95%'
                },{
                    tabIndex: tabOffset+5,
                    xtype: 'textfield',
                    fieldLabel: 'CAP',
                    name: ns+'Cap',
                    anchor:'95%'
                }]
            }, {
                xtype: 'container',
                columnWidth:.5,
                layout: 'anchor',
                items: [{
                    xtype:'wcs_provinciaremotecombo',
                    hiddenName:'codProvincia',
                    valueField:'codProvincia',
                    tabIndex: tabOffset+2,
                    displayField:'desProvincia',
                    allowBlank: allowBlank,
                    blankText: 'Questo campo è obbligatorio',
                    fieldLabel: 'Provincia'+labelSuffix,
                    name: ns+'Provincia',
                    listeners:{
                        change:function(combo){
                            codProv=combo.getValue();
                            rawLoad(combo);
                        },
                        beforequery:function() {
                            this.store.removeAll();
                            this.store.proxy.extraParams = {
                                codStato: codStato
                            };
                        }
                    },
                    anchor:'95%'
                },{
                    xtype:'wcs_viaremotecombo',
                    hiddenName:'codVia',
                    tabIndex: tabOffset+4,
                    valueField:'codVia',
                    displayField:'desVia',
                    fieldLabel: 'Via'+labelSuffix,
                    allowBlank: allowBlank,
                    blankText: 'Questo campo è obbligatorio',
                    name: ns+'Via',
                    listeners:{
                        change:function(combo){
                            codVia=combo.getValue();
                            rawLoad(combo);
                        },
                        beforequery:function() {
                            this.minChars= 2;
                            this.store.removeAll();
                            this.store.proxy.extraParams = {
                                codStato: codStato,
                                codProv: codProv,
                                codComune: codCom
                            };
                        }
                    },
                    anchor:'95%'
                },{
                    xtype:'wcs_civicoremotecombo',
                    hiddenName:'codCivico',
                    tabIndex: tabOffset+6,
                    valueField:'codCivico',
                    displayField:'desCivico',
                    fieldLabel: 'Civico'+labelSuffix,
                    allowBlank: allowBlank,
                    blankText: 'Questo campo è obbligatorio',
                    name: ns+'Civico',
                    listeners:{
                        change:function(combo){
                            rawLoad(combo);
                        },
                        beforequery:function() {
                            this.minChars=1;
                            this.store.removeAll();
                            this.store.proxy.extraParams = {
                                codStato: codStato,
                                codProv: codProv,
                                codComune: codCom,
                                codVia: codVia
                            };
                        }
                    },
                    anchor:'95%'
                }]
            }]
        }];
        
        this.callParent(arguments);
    }
});